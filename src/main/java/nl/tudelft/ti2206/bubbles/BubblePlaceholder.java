package nl.tudelft.ti2206.bubbles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@code BubblePlaceholder} fills a space in the {@link BubbleMesh} at
 * which a new Bubble can be snapped
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class BubblePlaceholder extends AbstractBubble {
	
	private static final long serialVersionUID = 4254751857830339489L;
	private static final Logger log = LoggerFactory.getLogger(BubblePlaceholder.class);

	@Override
	public BubblePlaceholder getSnapPosition(final Bubble bubble) {
		log.info("Snapping to top: {}", bubble);
		return this;
	}
	
}
