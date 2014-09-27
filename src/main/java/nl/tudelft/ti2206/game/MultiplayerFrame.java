package nl.tudelft.ti2206.game;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.swing.JLabel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.game.backend.GameTick;
import nl.tudelft.ti2206.game.backend.GameTickImpl;
import nl.tudelft.ti2206.game.backend.MasterGameController;
import nl.tudelft.ti2206.game.backend.SlaveGameController;
import nl.tudelft.ti2206.network.Connector;

public class MultiplayerFrame extends SinglePlayerFrame {
	

	private static final Logger log = LoggerFactory.getLogger(SinglePlayerFrame.class);
	private static final long serialVersionUID = 8466074133798558802L;
	
	protected final SlaveGameController slaveGameController;
	protected final GamePanel slaveGamePanel;
	protected final JLabel slaveScoreLabel;
	protected final Connector connector;
	
	public MultiplayerFrame(Connector connector) throws IOException {
		this(Executors.newScheduledThreadPool(2), connector);
	}
	
	private MultiplayerFrame(ScheduledExecutorService executorService, Connector connector) throws IOException {
		this(connector, new GameTickImpl(FRAME_PERIOD, executorService), executorService);
	}
	
	private  MultiplayerFrame(Connector connector, GameTick gameTick, ScheduledExecutorService executorService) throws IOException {
		this(new MasterGameController(BubbleMesh.parse(SinglePlayerFrame.class
				.getResourceAsStream(DEFAULT_BOARD_PATH)), connector, gameTick),
			new SlaveGameController(BubbleMesh.parse(SinglePlayerFrame.class
				.getResourceAsStream(DEFAULT_BOARD_PATH)), connector, gameTick),
			connector, gameTick, executorService);
	}

	MultiplayerFrame(MasterGameController gameController,
			SlaveGameController slaveGameController, Connector connector,
			GameTick gameTick, ScheduledExecutorService executorService) {
		
		super(gameController, gameTick, executorService);
		this.slaveGameController = slaveGameController;
		this.slaveGamePanel = new GamePanel(slaveGameController);
		this.connector = connector;
		
		slaveScoreLabel = new JLabel("Score: 0");
		slaveGameController.getModel().addObserver((a, b) ->
			slaveScoreLabel.setText("Score: " + slaveGameController.getModel().getScore()));
		
		Container contentPane = getContentPane();
		fillSlaveGamePanel(contentPane);
		fillSlaveScoreLabel(contentPane);
		
		getExecutorService().submit(connector);
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
