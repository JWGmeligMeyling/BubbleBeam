package nl.tudelft.ti2206.bubbles;

/**
 * Direction
 * 
 * @author Jan-Willem Gmelig Meyling
 */
public enum Direction {
	TOPLEFT, TOPRIGHT, LEFT, RIGHT, BOTTOMLEFT, BOTTOMRIGHT;

	/**
	 * @return the opposite {@code Direction}
	 */
	public Direction opposite() {
		return oppositeFor(this);
	}

	/**
	 * @param direction
	 *            direction for which to return the opposite direction
	 * @return the opposite {@code Direction} for the given
	 *         {@code Direction}
	 */
	public static Direction oppositeFor(final Direction direction) {
		switch (direction) {
		case BOTTOMLEFT:
			return TOPRIGHT;
		case BOTTOMRIGHT:
			return TOPLEFT;
		case LEFT:
			return RIGHT;
		case RIGHT:
			return LEFT;
		case TOPLEFT:
			return BOTTOMRIGHT;
		case TOPRIGHT:
			return BOTTOMLEFT;
		}
		throw new IllegalArgumentException();
	}

}