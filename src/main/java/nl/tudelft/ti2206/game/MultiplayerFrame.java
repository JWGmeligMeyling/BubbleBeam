package nl.tudelft.ti2206.game;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.io.IOException;

import javax.swing.JLabel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.tudelft.ti2206.bubbles.factory.DefaultBubbleFactory;
import nl.tudelft.ti2206.bubbles.mesh.BubbleMesh;
import nl.tudelft.ti2206.cannon.SlaveCannonController;
import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.game.backend.GameModel;
import nl.tudelft.ti2206.network.Connector;

public class MultiplayerFrame extends SinglePlayerFrame {
	

	private static final Logger log = LoggerFactory.getLogger(SinglePlayerFrame.class);
	private static final long serialVersionUID = 8466074133798558802L;
	
	protected final GameController slaveGameController;
	protected final GamePanel slaveGamePanel;
	protected final JLabel slaveScoreLabel;
	protected final Connector connector;
	
	public MultiplayerFrame(Connector connector) throws IOException {
		
		super();
		
		BubbleMesh bubbleMesh = BubbleMesh.parse(SinglePlayerFrame.class.getResourceAsStream(DEFAULT_BOARD_PATH));
		GameModel gameModel = new GameModel(bubbleMesh);
		final SlaveCannonController cannonController = new SlaveCannonController();
		DefaultBubbleFactory bubbleFactory = new DefaultBubbleFactory();
		
		this.slaveGameController = new GameController(gameModel, cannonController, gameTick, bubbleFactory, true);
		this.slaveGamePanel = new GamePanel(slaveGameController);
		this.slaveGamePanel.setBackground(new Color(225,225,225));
		this.connector = connector;
		
		slaveScoreLabel = new JLabel("Score: 0");
		slaveGameController.getModel().addObserver((a, b) ->
			slaveScoreLabel.setText("Score: " + slaveGameController.getModel().getScore()));
		
		Container contentPane = getContentPane();
		fillSlaveGamePanel(contentPane);
		fillSlaveScoreLabel(contentPane);

		cannonController.bindConnectorAsSlave(connector);
		slaveGameController.bindConnectorAsSlave(connector);
		super.cannonController.bindConnectorAsMaster(connector);
		this.getScheduledExecutorService().submit(connector);
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
