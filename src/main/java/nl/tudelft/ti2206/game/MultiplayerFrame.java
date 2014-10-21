package nl.tudelft.ti2206.game;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.io.IOException;

import javax.swing.JLabel;

import nl.tudelft.ti2206.cannon.SlaveCannonController;
import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.game.backend.GameModel;
import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.packets.PacketListener;
import nl.tudelft.ti2206.network.packets.PoppedPacket;
import nl.tudelft.ti2206.game.event.GameListener.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiplayerFrame extends SinglePlayerFrame {
	

	private static final Logger log = LoggerFactory.getLogger(SinglePlayerFrame.class);
	private static final long serialVersionUID = 8466074133798558802L;
	
	protected final GameController slaveGameController;
	protected final GamePanel slaveGamePanel;
	protected final JLabel slaveScoreLabel;
	protected final Connector connector;
	protected int bubblesPopped;
	
	public MultiplayerFrame(final GameModel masterModel, final GameModel slaveModel, Connector connector) throws IOException {
		
		super(masterModel);
		
		final SlaveCannonController cannonController = new SlaveCannonController();
		this.slaveGameController = new GameController(slaveModel, cannonController, gameTick, true);
		this.slaveGamePanel = new GamePanel(slaveGameController);
		this.slaveGamePanel.setBackground(new Color(225,225,225));
		this.connector = connector;
		
		super.gameController.getModel().getBubbleMesh().getEventTarget().addPopListener((popEvent)->{
			log.info("{} bubbles popped", popEvent.amountOfPoppedBubbles());
			connector.sendPacket(new PoppedPacket(popEvent.amountOfPoppedBubbles()));
		});;
		
		connector.getPacketHandlerCollection()
			.addEventListener(PacketListener.PoppedPacketListener.class, (a) -> {
					bubblesPopped += a.getAmount();
					if (bubblesPopped >= 5) {
						gameController.getGameMode().shotMissed(null);
						bubblesPopped = 0;
					}
				});
		
		slaveGameController.getModel().addEventListener(new GameOverEventListener(){

			@Override
			public void gameOver(GameOverEvent event) {
				log.info("you win");
				gameController.getModel().setWon(true);
				gameController.getModel().setGameOver(true);
			}
			
		});
		
		gameController.getModel().addEventListener(new GameOverEventListener(){

			@Override
			public void gameOver(GameOverEvent event) {
			log.info("you lose");
				slaveGameController.getModel().setWon(true);
			}
		
		});
		
		
		slaveScoreLabel = new JLabel("Score: 0");
		slaveGameController.getModel().addObserver((a, b) ->
			slaveScoreLabel.setText("Score: " + slaveGameController.getModel().getScore()));
		
		Container contentPane = getContentPane();
		fillSlaveGamePanel(contentPane);
		fillSlaveScoreLabel(contentPane);

		cannonController.bindConnectorAsSlave(connector);
		slaveGameController.bindConnectorAsSlave(connector);
		super.cannonController.bindConnectorAsMaster(connector);
		super.gameController.bindConnectorAsMaster(connector);
		
	}
	
	@Override
	protected void repaintPanels() {
		super.repaintPanels();
		slaveGamePanel.repaint();
	}
	
	@Override
	public void dispose() {
		try {
			connector.close();
		} catch (IOException e) {
			log.warn(e.getMessage(), e);
		}
		super.dispose();
	}
	
	protected void fillSlaveGamePanel(Container contentPane) {
		contentPane.add(slaveGamePanel, new GridBagConstraints(3, 0, 1, 4, 0d, 0d,
				GridBagConstraints.WEST, GridBagConstraints.NONE, PADDED, 0,
				0));
	}
	
	protected void fillSlaveScoreLabel(Container contentPane) {
		contentPane.add(slaveScoreLabel, new GridBagConstraints(3, 4, 1, 1, 0d, 0d,
				GridBagConstraints.WEST, GridBagConstraints.NONE, PADDED, 30,
				30));
	}

}
