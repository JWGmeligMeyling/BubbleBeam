package nl.tudelft.ti2206.bubbles;

import java.awt.Color;

public class JokerBubble implements DecoratedBubble {
	
	private static final long serialVersionUID = -1415122920739845104L;
	
	protected final ColouredBubble bubble;
	
	protected boolean collided = false;

	public JokerBubble() {
		bubble = new ColouredBubble(Color.WHITE);
	}
	
	@Override
	public boolean popsWith(Bubble target) {
		return !collided || bubble.popsWith(target);
	}
	
	@Override
	public void collideHook(Bubble other) {
		if(Coloured.class.isInstance(other)) {
			Coloured coloured = Coloured.class.cast(other);
			bubble.setColor(coloured.getColor());
			collided = true;
		}
	}

	@Override
	public Bubble getBubble() {
		return bubble;
	}
	
}
