package nl.tudelft.ti2206.cannon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.tudelft.ti2206.util.mvc.AbstractEventTarget;
import nl.tudelft.util.Vector2f;

public class AbstractCannonController extends
		AbstractEventTarget<CannonListener> implements CannonController {

	private static final Logger log = LoggerFactory.getLogger(AbstractCannonController.class);
	
	protected final CannonModel model;
	
	public AbstractCannonController() {
		this(new CannonModel());
	}
	
	public AbstractCannonController(final CannonModel cannonModel) {
		super();
		this.model = cannonModel;
		
		cannonModel.addObserver((a,b) -> {
			listeners.forEach(listener -> {
				listener.rotate(cannonModel.getAngle());	
			});
		});
	}

	@Override
	public CannonModel getModel() {
		return model;
	}

	@Override
	public void load() {
		model.setLoaded(true);
	}

	@Override
	public void shoot() {
		model.setLoaded(false);
		Vector2f direction = model.getDirection();
		log.info("Shooting in direction {}", direction);
		listeners.forEach(listener -> listener.shoot(direction));
	}

}
