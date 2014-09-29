package nl.tudelft.ti2206.cannon;

import nl.tudelft.util.Vector2f;

public class AbstractCannonController implements CannonController {

	protected final CannonModel model;
	
	public AbstractCannonController() {
		this(new CannonModel());
		this.load();
	}
	
	public AbstractCannonController(final CannonModel cannonModel) {
		super();
		this.model = cannonModel;
	}

	@Override
	public CannonModel getModel() {
		return model;
	}

	@Override
	public void load() {
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
