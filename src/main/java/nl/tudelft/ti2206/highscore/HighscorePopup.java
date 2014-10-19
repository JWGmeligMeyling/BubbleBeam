package nl.tudelft.ti2206.highscore;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class HighscorePopup extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8868501042305536344L;
	protected final Highscore hs;
	
	public HighscorePopup(Highscore hs){
		this.hs = hs;
		displayHighscores();
		this.setTitle(hs.getTitle());
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		this.pack();
	}
	
	private void displayHighscores(){
		int size = hs.getSize();
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 0, 0, 10);
		int i;
		for(i = 1; i <= size;i++){
			ScoreItem item = hs.getPlace(i);
			if(item != null){
				c.gridy = i;
				c.gridx = 0;
				add(new JLabel("" + i),c);
				c.gridx = 1;
				add(new JLabel(item.getName()),c);
				c.gridx = 2;
				add(new JLabel("" + item.getScore()),c);
				c.gridx = 3;
				add(new JLabel(DateFormat.getInstance().format(item.getDate())),c);
			}
		}
		c.gridy = i;
		c.gridx = 3;
		JButton deleteHighscoresButton = new JButton("Delete Highscores");
		deleteHighscoresButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				hs.deleteHighscores();
				HighscorePopup.this.dispose();
			}
		});
		add(deleteHighscoresButton,c);
	}
	
}
