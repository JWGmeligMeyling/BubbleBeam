package nl.tudelft.ti2206.bubbles.decorators;

import java.awt.Color;
import java.util.Iterator;
import java.util.Set;
import javax.swing.Timer;

import nl.tudelft.ti2206.bubbles.Bubble;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.bubbles.DecoratedBubble;

public class RainbowBubble extends DecoratedBubble {

	private static final long serialVersionUID = 1095006974229484904L;
	
	protected Iterator<Color> iterator;
	protected final Timer timer;
	
	public RainbowBubble(final Set<Color> remainingColors) {
		
		super(new ColouredBubble(remainingColors.iterator().next()));
		
		if(remainingColors.isEmpty()) {
			throw new IllegalArgumentException("Set should not be empty");
		}
		
		this.iterator = remainingColors.iterator();
		this.iterator.next();
		
		this.timer = new Timer(600, e -> {
			if(!iterator.hasNext()) {
				iterator = remainingColors.iterator();
			}
			
			if(!iterator.hasNext()) {
				throw new IllegalStateException("Set should not be empty");
			}
			
			((ColouredBubble) bubble).setColor(iterator.next());
		});
		
		this.timer.start();
	}
	
	@Override
	public void collideHook(Bubble target) {
		timer.stop();
		bubble.collideHook(target);
	}
	
}
