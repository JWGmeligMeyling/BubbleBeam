package nl.tudelft.ti2206.game.event;

import java.util.EventListener;
import java.util.EventObject;

import nl.tudelft.ti2206.cannon.CannonController;
import nl.tudelft.ti2206.cannon.CannonModel;
import nl.tudelft.util.Vector2f;

/**
 * A {@link CannonListener} listens for changes to the {@link CannonController}
 * and {@link CannonModel}
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public interface CannonListener extends EventListener {
	
	/**
	 * Abstract base class for {@link CannonEvent CannonEvents}. Extends
	 * {@link EventObject}, but forces to use a {@link CannonController} as event
	 * source.
	 * 
	 * @author Jan-Willem Gmelig Meyling
	 *
	 */
	abstract class CannonEvent extends EventObject {

		private static final long serialVersionUID = 2248495057835681167L;
		
		protected final transient CannonController cannonController;
		
		/**
		 * Construct a new {@link CannonEvent}
		 * 
		 * @param cannonController
		 *            {@link CannonController} that triggered the
		 *            {@link CannonEvent}
		 */
		protected CannonEvent(CannonController cannonController) {
			super(cannonController);
			this.cannonController = cannonController;
		}
		
		@Override
		public CannonController getSource() {
	        return cannonController;
	    }
		
	}
	
	/**
	 * The {@link CannonShootEvent} is triggered when the cannon shoots
	 * 
	 * @author Jan-Willem Gmelig Meyling
	 *
	 */
	class CannonShootEvent extends CannonEvent {
		
		private static final long serialVersionUID = 8995993653866506101L;
		
		protected final Vector2f direction;
		
		/**
		 * Construct a new {@link CannonShootEvent}
		 * 
		 * @param cannonController
		 *            {@link CannonController} that triggered the event
		 * @param direction
		 *            direction in which was shot
		 */
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
	
	/**
	 * Called on the {@link CannonListener} when a {@link CannonShootEvent} was
	 * triggered
	 * 
	 * @param event
	 *            The {@link CannonShootEvent}
	 */
	void shoot(CannonShootEvent event);
	
	/**
	 * The {@link CannonShootEvent} is triggered when the cannon rotates
	 * 
	 * @author Jan-Willem Gmelig Meyling
	 *
	 */
	class CannonRotateEvent extends CannonEvent {
		
		private static final long serialVersionUID = -2197998161546093925L;
		
		protected final Vector2f direction;
		protected final double angle;
		
		/**
		 * Construct a new {@link CannonRotateEvent}
		 * 
		 * @param cannonController
		 *            {@link CannonController} that triggered the event
		 * @param direction
		 *            direction of the cannon
		 * @param angle
		 *            angle of the cannon
		 */
		public CannonRotateEvent(CannonController cannonController, Vector2f direction, double angle) {
			super(cannonController);
			this.direction = direction;
			this.angle = angle;
		}
		
		public Vector2f getDirection() {
			return direction;
		}
		
		public double getAngle() {
			return angle;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(CannonRotateEvent.class.isInstance(obj)) {
				CannonRotateEvent other = CannonRotateEvent.class.cast(obj);
				return direction.equals(other.direction)
						&& cannonController.equals(other.cannonController);
			}
			return super.equals(obj);
		}
		
	}
	
	/**
	 * Called on the {@link CannonListener} when a {@link CannonRotateEvent} was
	 * triggered
	 * 
	 * @param event
	 *            The {@link CannonRotateEvent}
	 */
	void rotate(CannonRotateEvent event);
	
	/**
	 * The {@link CannonShootListener} is a {@link CannonListener} that listens
	 * for {@link CannonShootEvent CannonShootEvents}
	 * 
	 * @author Jan-Willem Gmelig Meyling
	 *
	 */
	@FunctionalInterface
	interface CannonShootListener extends CannonListener {
		@Override default void rotate(CannonRotateEvent event) {};
	}
	
	/**
	 * The {@link CannonRotateListener} is a {@link CannonListener} that listens
	 * for {@link CannonRotateEvent CannonRotateEvents}
	 * 
	 * @author Jan-Willem Gmelig Meyling
	 *
	 */
	@FunctionalInterface
	interface CannonRotateListener extends CannonListener {
		@Override default void shoot(CannonShootEvent event) {};
	}
	
}
