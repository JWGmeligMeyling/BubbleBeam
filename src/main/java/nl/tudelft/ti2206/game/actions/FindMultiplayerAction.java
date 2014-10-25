package nl.tudelft.ti2206.game.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.tudelft.ti2206.cannon.MouseCannonController;
import nl.tudelft.ti2206.cannon.AbstractCannonController;
import nl.tudelft.ti2206.game.MultiplayerFrame;
import nl.tudelft.ti2206.game.SinglePlayerFrame;
import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.game.backend.GameModel;
import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.GameModelPacket;
import nl.tudelft.ti2206.network.PacketListener.GameModelPacketListener;

public class FindMultiplayerAction extends AbstractAction {

	private static final long serialVersionUID = -6610799628595654049L;
	protected static final String DEFAULT_BOARD_PATH = "/board.txt";

	public final static int PORT = 8989;
	private final SinglePlayerFrame singlePlayerFrame;
	private static final Logger log = LoggerFactory
			.getLogger(FindMultiplayerAction.class);
	
	public FindMultiplayerAction(SinglePlayerFrame singlePlayerFrame) {
		super();
		this.singlePlayerFrame = singlePlayerFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ScheduledExecutorService executor = singlePlayerFrame.getScheduledExecutorService();
		
		final JDialog dialog = new JDialog();
		JButton cancelButton = new JButton("Cancel");
		dialog.add(cancelButton);
		
		executor.submit(() -> {
			try {
				Socket socket = new Socket();

				cancelButton.addActionListener((event2) -> {
					try {
						socket.close();
					} catch (Exception ex) {
						log.info(ex.getMessage(), ex);
					}
				});
				
				log.info("Trying to connect to host at port {}", PORT);
				socket.connect(new InetSocketAddress(singlePlayerFrame.getIpValue(), PORT));
				log.info("Connected to host {}", socket);
				
				final Object lock = new Object();
				Connector connector = new Connector(socket);
				
				AtomicReference<GameModel> masterGameModelRef = new AtomicReference<>();
				AtomicReference<GameModel>  slaveGameModelRef = new AtomicReference<>();
				
				connector.addEventListener(new GameModelPacketListener() {

					@Override
					public void handleGameModelPacket(GameModelPacket packet) {
						GameModel masterGameModel = packet.getMasterGameModel();
						GameModel slaveGameModel = packet.getSlaveGameModel();

						assert masterGameModel != null : "GameModel should not be null";
						assert slaveGameModel != null : "GameModel should not be null";
						assert masterGameModel.getGameMode().equals(slaveGameModel.getGameMode())
							: "GameModels should have equal GameMode's";
						
						log.info("Received GameModels for multiplayer {} with mode {}",
								masterGameModel, masterGameModel.getGameMode());

						masterGameModelRef.set(masterGameModel);
						slaveGameModelRef.set(slaveGameModel);
						
						synchronized(lock) {
							log.info("Notifying lock");
							lock.notifyAll();
						}
						
						connector.removeEventListener(this);
					}

				});
				
				connector.start();
				
				synchronized(lock) {
					log.info("Waiting on lock");
					lock.wait();
					log.info("Lock was notified");
				}
				
				MouseCannonController masterCannonController = new MouseCannonController();
				GameController masterGameController = new GameController(slaveGameModelRef.get(), masterCannonController);
				GameController slaveGameController = new GameController(masterGameModelRef.get(), new AbstractCannonController(), true);
				MultiplayerFrame frame = new MultiplayerFrame(masterCannonController, masterGameController, slaveGameController, connector);
				
				frame.pack();
				frame.setLocationRelativeTo(this.singlePlayerFrame);
				
				SwingUtilities.invokeLater(() -> {
					this.singlePlayerFrame.dispose();
				});
				
				frame.setVisible(true);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.getFindMultiplayerAction().setEnabled(false);
				
			}
			catch (IOException | InterruptedException es) {
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
