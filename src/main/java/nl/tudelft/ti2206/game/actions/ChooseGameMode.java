package nl.tudelft.ti2206.game.actions;

import java.awt.Frame;
import java.awt.GridLayout;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JDialog;

import org.reflections.Reflections;

import com.google.common.collect.Lists;

import nl.tudelft.ti2206.game.backend.mode.GameMode;
import nl.tudelft.ti2206.game.backend.mode.ModeName;

public class ChooseGameMode {

	public ChooseGameMode(final Frame owner, final Consumer<Class<? extends GameMode>> callback) {
		final JDialog dialog = new JDialog(owner, false);	//this makes the dialog always stay on top of the singleplayerframe
		dialog.setTitle("Choose Game Mode");
		
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
		
		//adding buttons for all gamemodes to the dialog
		dialog.setLayout(new GridLayout(2, gameModes.size()));
		
		for(Class<? extends GameMode> gameMode : gameModes) {
			String label = gameMode.getAnnotation(ModeName.class).value();
			JButton button = new JButton(label);
			
			button.addActionListener((a) -> {
				callback.accept(gameMode);
			});
			
			dialog.add(button);
		}
		
		//make it so that the dialog is displayed as it should be
		dialog.setModal(true);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

}
