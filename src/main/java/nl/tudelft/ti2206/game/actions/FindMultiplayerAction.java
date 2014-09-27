package nl.tudelft.ti2206.game.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ScheduledExecutorService;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.tudelft.ti2206.game.MultiplayerFrame;
import nl.tudelft.ti2206.game.SinglePlayerFrame;
import nl.tudelft.ti2206.network.Connector;

public class FindMultiplayerAction extends AbstractAction {

	private static final long serialVersionUID = -6610799628595654049L;

	public final static int PORT = 8989;
	private final SinglePlayerFrame singlePlayerFrame;
	private static final Logger log = LoggerFactory
			.getLogger(FindMultiplayerAction.class);
	
	public FindMultiplayerAction(SinglePlayerFrame singlePlayerFrame) {
		super("Find Multi-Player");
		this.singlePlayerFrame = singlePlayerFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		log.info("Setting up host");
		
		ScheduledExecutorService executor = singlePlayerFrame.getExecutorService();
		
		final JDialog dialog = new JDialog();
		JButton cancelButton = new JButton("Cancel");
		dialog.add(cancelButton);
		
		executor.submit(() -> {
			try {
				Socket socket = new Socket();
//				socket.setSoTimeout(5000);

				cancelButton.addActionListener((event2) -> {
					try {
						socket.close();
					} catch (Exception ex) {
						log.info(ex.getMessage(), ex);
					}
				});
				
				log.info("Trying to connect to host");
				socket.connect(new InetSocketAddress(singlePlayerFrame.getIpValue(), PORT));
				log.info("Connected to host");
				
				Connector connector = new Connector(socket);
				MultiplayerFrame frame = new MultiplayerFrame(connector);
				frame.pack();
				frame.setLocationRelativeTo(this.singlePlayerFrame);
				
				this.singlePlayerFrame.dispose();
				
				frame.setVisible(true);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.getFindMultiplayerAction().setEnabled(false);
			}
			catch (IOException es) {
				log.info(es.getMessage(), es);
			}
			finally {
				dialog.dispose();
				log.debug("We're done with the ServerSocket");
			}
		});
		
		dialog.setModal(true);
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

}
