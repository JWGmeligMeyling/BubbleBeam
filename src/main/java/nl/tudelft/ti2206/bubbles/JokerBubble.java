package nl.tudelft.ti2206.bubbles;

import java.awt.Color;

public class JokerBubble implements DecoratedBubble {
	
	private static final long serialVersionUID = -1415122920739845104L;
	
	protected final ColouredBubble bubble;

	public JokerBubble() {
		bubble = new ColouredBubble(Color.WHITE);
	}
	
	@Override
	public boolean popsWith(Bubble target) {
		return true;
	}
	
	@Override
	public void collideHook(Bubble other) {
		if(Coloured.class.isInstance(other)) {
			Coloured coloured = Coloured.class.cast(other);
			bubble.setColor(coloured.getColor());
		}
	}

	@Override
	public Bubble getBubble() {
		return bubble;
	}
	
}
