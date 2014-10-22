package nl.tudelft.ti2206.highscore;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import nl.tudelft.ti2206.game.backend.mode.GameMode;
import nl.tudelft.ti2206.game.backend.mode.ModeName;

import org.reflections.Reflections;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;

public class HighscorePopup extends JDialog{

	private static final long serialVersionUID = 8868501042305536344L;
	
	protected final Highscore hs;
	
	public HighscorePopup(Highscore hs, Class<? extends GameMode> selectedGameMode){
		this.hs = hs;
		
		Reflections reflections = new Reflections(GameMode.class);
		List<Class<? extends GameMode>> gameModes = Lists
				.newArrayList(reflections.getSubTypesOf(GameMode.class));
		
		Collections.sort(gameModes, new Comparator<Class<? extends GameMode>>() {

			@Override
			public int compare(Class<? extends GameMode> o1,
					Class<? extends GameMode> o2) {
				return o1
					.getAnnotation(ModeName.class)
					.value()
					.compareTo(o2.getAnnotation(ModeName.class).value());
			}
			
		});

		final JTabbedPane tabbedPane = new JTabbedPane();
		
		gameModes.forEach(gameMode -> {
			JPanel panel = new JPanel(new GridBagLayout());
			ImmutableSortedSet<HighscoreItem> items = hs.getScoresForGameMode(gameMode);
			
			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(0, 0, 0, 10);
			
			int i = 1;
			for(HighscoreItem item : items) {
				c.gridy = i;
				c.gridx = 0;
				panel.add(new JLabel("" + i), c);
				c.gridx = 1;
				panel.add(new JLabel(item.getName()), c);
				c.gridx = 2;
				panel.add(new JLabel("" + item.getScore()), c);
				c.gridx = 3;
				panel.add(new JLabel(DateFormat.getInstance().format(new Date(item.getStart()))), c);
				i++;
			}
			
			tabbedPane.add(gameMode.getAnnotation(ModeName.class).value(), panel);
			if(gameMode.equals(selectedGameMode)) {
				tabbedPane.setSelectedComponent(panel);
			}
		});
		
		this.add(tabbedPane);
		this.setTitle("Highscores");
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}
	
}
