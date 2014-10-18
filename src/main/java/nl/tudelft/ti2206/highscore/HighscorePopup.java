package nl.tudelft.ti2206.highscore;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.DateFormat;

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
		this.pack();
		this.setTitle(hs.getTitle());
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		this.setMinimumSize(new Dimension(250,100));
	}
	
	private void displayHighscores(){
		int size = hs.getSize();
		this.setLayout(new GridLayout(size,4));
		for(int i = 1; i <= size;i++){
			ScoreItem item = hs.getPlace(i);
			if(item != null){
				this.add(new JLabel("" + i));
				this.add(new JLabel(item.getName()));
				this.add(new JLabel("" + item.getScore()));
				this.add(new JLabel(DateFormat.getInstance().format(item.getDate())));
			}
		}
	}
	
}
