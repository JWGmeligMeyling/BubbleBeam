package nl.tudelft.ti2206.game.event;

import java.util.EventListener;
import java.util.EventObject;

import nl.tudelft.ti2206.cannon.CannonController;
import nl.tudelft.util.Vector2f;

public interface CannonListener extends EventListener {
	
	abstract class CannonEvent extends EventObject {

		private static final long serialVersionUID = 2248495057835681167L;
		
		protected final transient CannonController cannonController;
		
		protected CannonEvent(CannonController cannonController) {
			super(cannonController);
			this.cannonController = cannonController;
		}
		
		public CannonController getSource() {
	        return cannonController;
	    }
		
	}
	
	class CannonShootEvent extends CannonEvent {
		
		private static final long serialVersionUID = 8995993653866506101L;
		
		protected final Vector2f direction;

		public CannonShootEvent(CannonController cannonController, Vector2f direction) {
			super(cannonController);
			this.direction = direction;
		}
		
		public Vector2f getDirection() {
			return direction;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(CannonShootEvent.class.isInstance(obj)) {
				CannonShootEvent other = CannonShootEvent.class.cast(obj);
				return direction.equals(other.direction)
						&& cannonController.equals(other.cannonController);
			}
			return super.equals(obj);
		}
		
	}
	
	void shoot(CannonShootEvent event);
	
	@FunctionalInterface
	interface CannonShootListener extends CannonListener {};
	
}
