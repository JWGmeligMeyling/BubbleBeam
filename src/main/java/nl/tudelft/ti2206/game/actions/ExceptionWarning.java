package nl.tudelft.ti2206.game.actions;

import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class ExceptionWarning extends JDialog {
	
	private static final long serialVersionUID = 6054518696127124556L;
	protected static final String ERROR_OCCURED = "An error occured";
	protected static final String OK = "Ok";
	
	public ExceptionWarning(Frame owner, Throwable e) {
		super(owner, ERROR_OCCURED);
		
		JLabel warningLabel = new JLabel(e.getMessage());
		JButton okButton = new JButton(OK);
		
		okButton.addActionListener((event) -> {
			dispose();
		});
		
		setLayout(new FlowLayout());
		add(warningLabel);
		add(okButton);
	}

}
