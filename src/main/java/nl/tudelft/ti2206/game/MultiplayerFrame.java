package nl.tudelft.ti2206.game;

import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.io.IOException;

import javax.swing.JLabel;

import nl.tudelft.ti2206.cannon.MouseCannonController;
import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.game.backend.GameOver;
import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.PacketListener;
import nl.tudelft.ti2206.game.event.BubbleMeshListener.BubblePopEvent;
import nl.tudelft.ti2206.game.event.GameListener.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiplayerFrame extends SinglePlayerFrame {
	

	private static final Logger log = LoggerFactory.getLogger(MultiplayerFrame.class);
	private static final long serialVersionUID = 8466074133798558802L;
	
	protected final GameController slaveGameController;
	protected final GamePanel slaveGamePanel;
	protected final JLabel slaveScoreLabel;
	protected final Connector connector;
	protected int bubblesPopped;
	
	public MultiplayerFrame(final MouseCannonController masterCannonController,
			final GameController masterGameController,
			final GameController slaveGameController,
			final Connector connector) throws IOException {
		
		super(masterCannonController, masterGameController);
		
		this.slaveGameController = slaveGameController;
		slaveGameController.bindGameTick(gameTick);
		
		this.slaveGamePanel = new GamePanel(slaveGameController);
		this.connector = connector;
		
		connector.addEventListener(new PacketListener.BubblePopListener() {
			
			@Override
			public void handleBubblePop(BubblePopEvent event) {
				bubblesPopped += event.getPoppedBubbles().size();
				if (bubblesPopped >= 5) {
					gameController.getGameMode().shotMissed(null);
					bubblesPopped = 0;
				}
			}
		});
		
		slaveGameController.getModel().addEventListener(new GameOverEventListener(){

			@Override
			public void gameOver(GameOverEvent event) {
				// The other player is game over, we have won
				gameController.gameOver(new GameOver(!event.getGameOver().isWin()));
			}
			
		});
		
		gameController.getModel().addEventListener(new GameOverEventListener(){

			@Override
			public void gameOver(GameOverEvent event) {
				// We're game over, the other player has won
				slaveGameController.gameOver(new GameOver(!event.getGameOver().isWin()));
			}
		
		});
		
		slaveScoreLabel = new JLabel("Score: 0");
		slaveGameController.getModel().addObserver((a, b) ->
			slaveScoreLabel.setText("Score: " + slaveGameController.getModel().getScore()));
		
		Container contentPane = getContentPane();
		fillSlaveGamePanel(contentPane);
		fillSlaveScoreLabel(contentPane);

		super.gameController.bindConnectorAsMaster(connector);
		this.slaveGameController.bindConnectorAsSlave(connector);
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
		contentPane.add(slaveGamePanel, new GridBagConstraints(GB_OPPONENTCOLUMN, 1, 1, GB_PANELHEIGHT, 0d, 0d,
				GridBagConstraints.WEST, GridBagConstraints.NONE, PADDED, 0,
				0));
	}
	
	protected void fillSlaveScoreLabel(Container contentPane) {
		slaveScoreLabel.setFont(new Font("Sans",Font.BOLD,40));
		slaveScoreLabel.setForeground(secondaryColor);
		contentPane.add(slaveScoreLabel, new GridBagConstraints(GB_OPPONENTCOLUMN, 1 + GB_PANELHEIGHT, 1, 1, 0d, 0d,
				GridBagConstraints.WEST, GridBagConstraints.NONE, PADDED, 30,
				30));
	}

}
