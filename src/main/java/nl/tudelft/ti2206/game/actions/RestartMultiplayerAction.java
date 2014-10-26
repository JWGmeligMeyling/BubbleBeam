package nl.tudelft.ti2206.game.actions;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import nl.tudelft.ti2206.bubbles.mesh.BubbleMesh;
import nl.tudelft.ti2206.cannon.AbstractCannonController;
import nl.tudelft.ti2206.cannon.MouseCannonController;
import nl.tudelft.ti2206.game.MultiplayerFrame;
import nl.tudelft.ti2206.game.SinglePlayerFrame;
import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.game.backend.GameModel;
import nl.tudelft.ti2206.logger.Logger;
import nl.tudelft.ti2206.logger.LoggerFactory;
import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.GameModelPacket;

public class RestartMultiplayerAction extends AbstractAction {
	
	protected static final String DEFAULT_BOARD_PATH = "/board.txt";
	
	private static final Logger log = LoggerFactory
			.getLogger(RestartMultiplayerAction.class);
	private static final long serialVersionUID = -1593501376672995877L;
	public final static int PORT = 8989;
	
	private final SinglePlayerFrame singlePlayerFrame;
	
	public RestartMultiplayerAction(SinglePlayerFrame singlePlayerFrame) {
		super();
		this.singlePlayerFrame = singlePlayerFrame;
	}
	
	boolean forceShotdown = false;

	@Override
	public void actionPerformed(ActionEvent event) {
		new ChooseGameMode(singlePlayerFrame, gameMode -> {
			log.info("Setting up host at port {}", PORT);
			
			final JDialog dialog = new JDialog(singlePlayerFrame, "Starting Multiplayer Game");
			dialog.setLayout(new FlowLayout());
			JLabel label = new JLabel("Looking for other players");
			JButton cancelButton = new JButton("Cancel");
			
			dialog.add(label);
			dialog.add(cancelButton);
			
			new Thread(() -> {
				try(ServerSocket server = new ServerSocket(PORT)) {
					
					
					dialog.addWindowListener(new WindowAdapter() {

						@Override
						public void windowClosed(WindowEvent e) {
							try {
								forceShotdown = true;
								server.close();
							} catch (Exception ex) {
								log.info(ex.getMessage(), ex);
							}
						}

					});
					
					cancelButton.addActionListener((event2) -> {
						try {
							forceShotdown = true;
							server.close();
						} catch (Exception ex) {
							log.info(ex.getMessage(), ex);
						}
					});
					
					log.debug("Accepting an incomming connection");
					Socket socket = server.accept();
					log.debug("Found an incomming connection");
					Connector connector = new Connector(socket);
					
					log.debug("Preparing GameModels");
					GameModel masterGameModel = new GameModel(gameMode);
					GameModel slaveGameModel = new GameModel(gameMode);
					
					masterGameModel.setBubbleMesh(BubbleMesh
							.parse(SinglePlayerFrame.class
									.getResourceAsStream(DEFAULT_BOARD_PATH)));
					slaveGameModel.setBubbleMesh(BubbleMesh
							.parse(SinglePlayerFrame.class
									.getResourceAsStream(DEFAULT_BOARD_PATH)));
					
					MouseCannonController masterCannonController = new MouseCannonController();
					GameController masterGameController = new GameController(masterGameModel, masterCannonController, GameController.GameControllerType.MULTIPLAYER_MASTER);
					GameController slaveGameController = new GameController(slaveGameModel, new AbstractCannonController(), GameController.GameControllerType.MULTIPLAYER_SLAVE);
					
					connector.start();
					log.info("Sending game model packet");
					connector.sendPacket(new GameModelPacket(masterGameModel, slaveGameModel));
					
					log.info("Found a connection, building {}", MultiplayerFrame.class);
					
					MultiplayerFrame frame = new MultiplayerFrame(masterCannonController,
							masterGameController, slaveGameController, connector);
					frame.pack();
					frame.setLocationRelativeTo(this.singlePlayerFrame);
					frame.setSound(false);
					singlePlayerFrame.dispose();
					frame.setVisible(true);
					frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					frame.getFindMultiplayerAction().setEnabled(false);
				}
				catch (IOException e) {

					log.info(e.getMessage(), e);
					
					if(!forceShotdown) {
						final JDialog warning = new ExceptionWarning(singlePlayerFrame, e);
						warning.setModal(true);
						warning.pack();
						warning.setLocationRelativeTo(null);
						warning.setVisible(true);
					}
					else {
						forceShotdown = false;
					}
					
				}
				finally {
					dialog.dispose();
					log.debug("We're done with the ServerSocket");
					Thread.currentThread().interrupt();
				}
			}, "Multiplayer-Connect").start();
			
			dialog.setModal(true);
			dialog.pack();
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);
		});
	}

}
