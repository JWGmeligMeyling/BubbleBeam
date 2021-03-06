package nl.tudelft.ti2206.cannon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.tudelft.util.Vector2f;
import nl.tudelft.ti2206.game.event.CannonListener.*;

/**
 * The {@link AbstractCannonController} provides a basic implementation for
 * {@link CannonController}
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public class AbstractCannonController implements CannonController {

	private static final Logger log = LoggerFactory.getLogger(AbstractCannonController.class);
			
	protected final CannonModel model;
	
	/**
	 * Construct a new {@link AbstractCannonController}
	 */
	public AbstractCannonController() {
		this(new CannonModel());
		this.load();
	}
	
	/**
	 * Construct a new {@link AbstractCannonController}
	 * 
	 * @param cannonModel
	 *            {@link CannonModel} to use with this {@link CannonController}
	 */
	public AbstractCannonController(final CannonModel cannonModel) {
		super();
		this.model = cannonModel;
		
		this.model.addObserver((a,b) -> {
			CannonRotateEvent event = new CannonRotateEvent(this, cannonModel
					.getDirection(), cannonModel.getAngle());
			this.model.trigger(listener -> listener.rotate(event));
		});
	}

	@Override
	public CannonModel getModel() {
		return model;
	}

	@Override
	public void load() {
		log.info("Loading the cannon");
		model.setCannonState(new CannonLoadedState(this));
	}

	@Override
	public void shoot() {
		model.getCannonState().shoot();
	}

	@Override
	public void setState(CannonState cannonState) {
		model.setCannonState(cannonState);
	}
	
	@Override
	public void setAngle(final Vector2f direction) {
		final CannonModel model = this.getModel();
		
		if (direction.y > -MIN_DIRECTION_Y) {
			direction.x = (float) (MIN_DIRECTION_X * Math.signum(direction.x));
			direction.y = (float) -MIN_DIRECTION_Y;
		}
		
		model.setDirection(direction);
		model.setAngle(Math.atan(direction.x / direction.y) + Math.PI / 2);
		model.notifyObservers();
	}

}
