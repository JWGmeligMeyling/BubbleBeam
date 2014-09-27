package nl.tudelft.ti2206.cannon;

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

}
