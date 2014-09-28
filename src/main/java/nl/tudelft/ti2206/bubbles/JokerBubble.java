package nl.tudelft.ti2206.bubbles;

import java.awt.Color;

public class JokerBubble extends ColouredBubble {
	
	private static final long serialVersionUID = -1415122920739845104L;

	public JokerBubble() {
		super(Color.WHITE);
	}
	
	@Override
	public boolean popsWith(Bubble target) {
		return true;
	}
	
	@Override
	public void collideHook(Bubble other) {
		if(Coloured.class.isInstance(other)) {
			Coloured coloured = Coloured.class.cast(other);
			setColor(coloured.getColor());
		}
	}

}
