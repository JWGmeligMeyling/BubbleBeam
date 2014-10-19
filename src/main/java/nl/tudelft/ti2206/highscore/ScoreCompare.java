package nl.tudelft.ti2206.highscore;

import java.util.Comparator;

public class ScoreCompare implements Comparator<ScoreItem>{

	@Override
	public int compare(ScoreItem o1, ScoreItem o2) {
		if(o1.getScore() > o2.getScore()){
			return -1;
		} else if(o1.getScore() < o2.getScore()){
			return 1;
		} else {
			return o1.getDate().compareTo(o2.getDate());
		}
	}

}
