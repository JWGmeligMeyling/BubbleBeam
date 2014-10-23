package nl.tudelft.ti2206.highscore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.SortedSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import nl.tudelft.ti2206.game.backend.mode.GameMode;

/**
 * Implementation for {@link Highscore}
 * 
 * @author Leon Hoek
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class HighscoreImpl implements Highscore {
	
	private static final long serialVersionUID = 8050892681774221931L;
	private static final Logger log = LoggerFactory.getLogger(HighscoreImpl.class);
	private final static File TEMP_BIN = new File(System.getProperty("user.home"), "BubbleBeam");
	private final static String FILENAME = "HIGHSCORES";
	private final static int CAPACITY = 10;
	
	private final File file;
	
	private final Map<Class<? extends GameMode>, SortedSet<HighscoreItem>> items;

	/**
	 * Construct a new highscore
	 */
	public HighscoreImpl() {
		this(new File(TEMP_BIN, FILENAME), Maps.newHashMap());
	}
	
	/**
	 * Construct a new highscore
	 * @param file file to use
	 */
	public HighscoreImpl(File file) {
		this(file, Maps.newHashMap());
	}
	
	protected HighscoreImpl(File file, Map<Class<? extends GameMode>, SortedSet<HighscoreItem>> items) {
		this.file = file;
		this.items = items;
	}

	@Override
	public File getFile() {
		return file;
	}

	@Override
	public ImmutableSortedSet<HighscoreItem> getScoresForGameMode(
			Class<? extends GameMode> gameMode) {
		
		ImmutableSortedSet<HighscoreItem> result;
		
		if(items.containsKey(gameMode)) {
			result =  ImmutableSortedSet.copyOf(items.get(gameMode));
		}
		else {
			result = ImmutableSortedSet.of();
		}
		
		return result;
	}

	@Override
	public void addScore(Class<? extends GameMode> gameMode, HighscoreItem scoreItem) {
		SortedSet<HighscoreItem> set = items.get(gameMode);
		
		if(set == null) {
			log.debug("Creating new highscore set for game mode {}", gameMode);
			set = Sets.newTreeSet();
			items.put(gameMode, set);
		}
		
		log.info("Inserting highscore {} for {}", scoreItem, gameMode);
		set.add(scoreItem);
		
		cleanUp(set);
		save();
	}
	
	protected void cleanUp(SortedSet<HighscoreItem> set) {
		log.debug("Cleaning up the highscores {}", set);
		
		while(set.size() > CAPACITY) {
			set.remove(set.last());
		}
	}

	@Override
	public void save() {
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
			
			log.debug("Writing the highscores to {}", file);
			output.writeObject(this);
			
		}
		catch (IOException e) {
			log.warn("Failed to write the Highscores to a file", e);
		}
	}
	
	@Override
	public void read() {
		file.getParentFile().mkdirs();
		try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
			
			log.debug("Reading the highscores from {}", file);
			Object object = input.readObject();
			
			if(HighscoreImpl.class.isInstance(object)) {
				HighscoreImpl highscore = HighscoreImpl.class.cast(object);
				items.putAll(highscore.items);
			}
			else {
				log.warn("Read object {} is not of expected type {}", object, HighscoreImpl.class);
			}
		}
		catch (ClassNotFoundException | IOException e) {
			log.info("Failed to read highscores, defaulting to empty highscore", e);
		}
	}
	
	@Override
	public void clear() {
		items.clear();
		
		if(file.exists()) {
			file.delete();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if(HighscoreImpl.class.isInstance(obj)) {
			HighscoreImpl other = HighscoreImpl.class.cast(obj);
			return file.equals(other.file) && items.equals(other.items); 
		}
		return false;
	}
	
	

}
