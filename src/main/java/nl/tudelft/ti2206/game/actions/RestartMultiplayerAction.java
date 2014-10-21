package nl.tudelft.ti2206.game.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ScheduledExecutorService;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.tudelft.ti2206.bubbles.mesh.BubbleMesh;
import nl.tudelft.ti2206.game.MultiplayerFrame;
import nl.tudelft.ti2206.game.SinglePlayerFrame;
import nl.tudelft.ti2206.game.backend.GameModel;
import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.packets.GameModelPacket;

public class RestartMultiplayerAction extends AbstractAction {
	
	protected static final String DEFAULT_BOARD_PATH = "/board.txt";
	
	private static final Logger log = LoggerFactory
			.getLogger(RestartMultiplayerAction.class);
	private static final long serialVersionUID = -1593501376672995877L;
	public final static int PORT = 8989;
	
	private final SinglePlayerFrame singlePlayerFrame;
	
	public RestartMultiplayerAction(SinglePlayerFrame singlePlayerFrame) {
		super("Restart Multi-Player");
		this.singlePlayerFrame = singlePlayerFrame;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		new ChooseGameMode(singlePlayerFrame, gameMode -> {
			log.info("Setting up host");
			
			ScheduledExecutorService executor = singlePlayerFrame.getScheduledExecutorService();
			
			final JDialog dialog = new JDialog();
			JButton cancelButton = new JButton("Cancel");
			dialog.add(cancelButton);
			
			executor.submit(() -> {
				try(ServerSocket server = new ServerSocket(PORT)) {
					
					cancelButton.addActionListener((event2) -> {
						try {
							server.close();
						} catch (Exception ex) {
							log.info(ex.getMessage(), ex);
						}
					});
					
					log.debug("Accepting an incomming connection");
					Socket socket = server.accept();
					log.debug("Found an incomming connection");
					Connector connector = new Connector(socket);
					
					log.info("Found a connection, building {}", MultiplayerFrame.class);
					
					GameModel masterGameModel = new GameModel(gameMode, BubbleMesh
							.parse(SinglePlayerFrame.class
									.getResourceAsStream(DEFAULT_BOARD_PATH)));
					GameModel slaveGameModel = new GameModel(gameMode, BubbleMesh
							.parse(SinglePlayerFrame.class
									.getResourceAsStream(DEFAULT_BOARD_PATH)));
					
					log.info("Sending game model packet");
					connector.sendPacket(new GameModelPacket(masterGameModel, slaveGameModel));
					
					MultiplayerFrame frame = new MultiplayerFrame(masterGameModel, slaveGameModel, connector);
					frame.pack();
					frame.setLocationRelativeTo(this.singlePlayerFrame);
					
					SwingUtilities.invokeLater(() -> {
						this.singlePlayerFrame.dispose();
					});
					
					frame.setVisible(true);
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					frame.getFindMultiplayerAction().setEnabled(false);
					
				}
				catch (IOException e) {
					log.info(e.getMessage(), e);
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
		});
	}

}
