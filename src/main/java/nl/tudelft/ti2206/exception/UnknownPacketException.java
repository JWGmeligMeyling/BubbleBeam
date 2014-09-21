package nl.tudelft.ti2206.exception;

public class UnknownPacketException extends IllegalArgumentException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public UnknownPacketException(String s) {
        super(s);
    }
}
