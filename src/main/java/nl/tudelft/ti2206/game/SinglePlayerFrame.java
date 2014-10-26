package nl.tudelft.ti2206.game;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayer;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.text.MaskFormatter;

import nl.tudelft.ti2206.cannon.MouseCannonController;
import nl.tudelft.ti2206.game.actions.ExitAction;
import nl.tudelft.ti2206.game.actions.FindMultiplayerAction;
import nl.tudelft.ti2206.game.actions.HighscoreAction;
import nl.tudelft.ti2206.game.actions.RestartMultiplayerAction;
import nl.tudelft.ti2206.game.actions.RestartSinglePlayerAction;
import nl.tudelft.ti2206.game.backend.GameController;
import nl.tudelft.ti2206.game.backend.GameModel;
import nl.tudelft.ti2206.game.backend.GameOver;
import nl.tudelft.ti2206.game.backend.GameTick;
import nl.tudelft.ti2206.game.backend.GameTickImpl;
import nl.tudelft.ti2206.logger.Logger;
import nl.tudelft.ti2206.logger.LoggerFactory;
import nl.tudelft.ti2206.util.mvc.View;

public class SinglePlayerFrame extends JFrame implements
		View<GameController, GameModel> {

	private static final Logger log = LoggerFactory.getLogger(SinglePlayerFrame.class);
	private static final long serialVersionUID = 5501239542707746229L;
	protected final static ComponentOrientation ORIENTATION = ComponentOrientation.LEFT_TO_RIGHT;
	protected static final String FRAME_TITLE = "Sponge Shooter";
	protected static final String VERSION_STRING = "Version: 1.0";
	protected static final String DEFAULT_BOARD_PATH = "/board.txt";
	protected static final int FPS = 30;
	protected static final int FRAME_PERIOD = 1000 / FPS;
	
	//Gridbag Constants
	protected static final Insets NO_PADDING = new Insets(0, 0, 0, 0);
	protected static final Insets PADDED = new Insets(10, 10, 10, 10);
	protected static final Insets HORIZONTAL_PADDING = new Insets(0,10,0,10);
	protected static final Insets NO_BOTTOM_PADDING = new Insets(10,10,0,10);
	protected final int GB_PLAYERCOLUMN = 0;
	protected final int GB_MENUCOLUMN = 1;
	protected final int GB_OPPONENTCOLUMN = 3;
	protected final int GB_PANELHEIGHT = 6;

	
	protected final Color mainBackgroundColor = new Color(111,186,241);
	protected final Color secondaryColor = new Color(41,126,181);
	
	
	private final AudioClip music;

	protected final Action
		exitAction = new ExitAction(this),
		restartSinglePlayer = new RestartSinglePlayerAction(this),
		restartMultiplayerAction = new RestartMultiplayerAction(this),
		findMultiplayerAction = new FindMultiplayerAction(this),
		highscoreAction = new HighscoreAction(this);
	
	protected boolean started = false;

	protected final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
	protected final Timer timer;
	protected final GameController gameController;
	protected final GamePanel gamePanel;
	protected final MouseCannonController cannonController;
	protected EffectsLayer layerUI;
	protected final JLabel scoreLabel;
	protected JTextField ipField;
	protected JCheckBoxMenuItem soundCheckbox;
	protected final GameTick gameTick;
	
	public SinglePlayerFrame(final MouseCannonController cannonController, final GameController gameController) throws IOException {

		super(FRAME_TITLE);
		
		this.cannonController = cannonController;
		this.gameTick = new GameTickImpl(FRAME_PERIOD, executor);
   		this.gameController = gameController;
   		gameController.bindGameTick(gameTick);
   		
		gamePanel = new GamePanel(gameController);
		cannonController.bindListenersTo(gamePanel, gamePanel.getCannon());
		music = gameController.getGameMode().getMusic();
		if(sound) music.loop();

		
		GameModel model = getModel();
		scoreLabel = new JLabel("Score: " + model.getScore());
		model.addObserver((a, b) ->
			scoreLabel.setText("Score: " + model.getScore()));

		try {
			MaskFormatter formatter = new MaskFormatter("###.###.###.###");
			JFormattedTextField ipField = new JFormattedTextField(formatter);
			ipField.setValue("127.000.000.001");
			this.ipField = ipField;
		} catch (ParseException e) {
			this.ipField = new JTextField("127.0.0.1");
		}

		timer = new Timer(FRAME_PERIOD, e -> {
			try {
				repaintPanels();
			} catch (GameOver exception) {
				new RestartSinglePlayerAction(this).actionPerformed(null);
			}
		});

		gameController.getModel().addEventListener(new GameOverHighscore(this, gameController));
		
		Container contentPane = this.getContentPane();
		fillBackground(contentPane);
		fillMenubar();
		contentPane.setComponentOrientation(ORIENTATION);
		contentPane.setLayout(new GridBagLayout());
		fillFrame(contentPane);
	}
	
	private boolean sound = true;

	protected void fillBackground(Container contentPane){
		contentPane.setBackground(mainBackgroundColor);
	}
	
	protected void fillMenubar() {
		JMenuBar menubar = new JMenuBar();
		JMenu menu = new JMenu("Settings");
		
		menu.add(new JCheckBoxMenuItem(new AbstractAction("Searchlight") {
			
			private static final long serialVersionUID = -8925014850605930693L;

			@Override
			public void actionPerformed(ActionEvent e) {
				layerUI.setEnabled(!layerUI.getEnabled());
			}
		}));
		
		soundCheckbox = new JCheckBoxMenuItem(new AbstractAction("Sound on") {
			
			private static final long serialVersionUID = -8925014850605930693L;

			@Override
			public void actionPerformed(ActionEvent e) {
				setSound(!sound);
			}
			
		});
		
		soundCheckbox.setSelected(sound);
		menu.add(soundCheckbox);
		menubar.add(menu);
		this.setJMenuBar(menubar);
	}

	protected void fillFrame(Container contentPane) {
		fillGamePanel(contentPane);
		fillMenu(contentPane);
		fillLogo(contentPane);
	}

	protected void fillMenu(Container contentPane) {
		fillHighscoreButton(contentPane);
		fillScoreLabel(contentPane);
		fillExitButton(contentPane);
		fillRestartButton(contentPane);
		fillRestartMultiplayer(contentPane);
		fillFindMultiplayerRestart(contentPane);
		fillIpAddressField(contentPane);
		fillVersionLabel(contentPane);
	}
	
	public boolean hasSound() {
		return sound;
	}
	
	public void setSound(boolean sound) {
		if(this.sound != sound)
			this.sound = sound;
		if(sound)
			music.loop();
		else 
			music.stop();
		soundCheckbox.setSelected(sound);
	}
	
	protected void fillLogo(Container contentPane){
		JLabel logoLabel = new JLabel(getLogoImage("/logo.png"));
		contentPane.add(logoLabel, new GridBagConstraints(0, 0, 3, 1, 0d, 0d,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, PADDED, 0,
				0));
	}
	
	protected void fillGamePanel(Container contentPane) {
		layerUI = new EffectsLayer();
		JLayer<JComponent> jlayer = new JLayer<JComponent>(gamePanel, layerUI);
		gamePanel.setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.LOWERED, mainBackgroundColor,secondaryColor));
		contentPane.add(jlayer, new GridBagConstraints(0, 1, 1, GB_PANELHEIGHT, 0d, 0d,
				GridBagConstraints.EAST, GridBagConstraints.NONE, PADDED, 0,
				0));
	}

	protected void fillScoreLabel(Container contentPane) {
		scoreLabel.setFont(new Font("Sans",Font.BOLD,40));
		scoreLabel.setForeground(secondaryColor);
		contentPane.add(scoreLabel, new GridBagConstraints(0, 1 + GB_PANELHEIGHT, 1, 1, 0d, 0d,
				GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
				HORIZONTAL_PADDING, 40, 30));
	}

	protected void fillHighscoreButton(Container contentPane){
		JButton highscoreButton = new ImagedButton("/highscore_button.png",highscoreAction,mainBackgroundColor);
		contentPane.add(highscoreButton, new GridBagConstraints(GB_MENUCOLUMN, 1, 1, 1, 1d, 0d,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				NO_BOTTOM_PADDING, 30, 30));
	}

	protected void fillExitButton(Container contentPane) {
		JButton exit = new ImagedButton("/exit_button_v2.png",exitAction,mainBackgroundColor);
		
		contentPane.add(exit, new GridBagConstraints(GB_MENUCOLUMN, 2, 1, 1, 0d, 0d,
				GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
				HORIZONTAL_PADDING, 30, 30));
	}

	protected void fillRestartButton(Container contentPane) {
		JButton restartSingle = new ImagedButton("/single_button_v2.png",restartSinglePlayer,mainBackgroundColor);
		contentPane.add(restartSingle, new GridBagConstraints(GB_MENUCOLUMN, 3, 1, 1, 1d, 0d,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				HORIZONTAL_PADDING, 30, 30));
	}

	protected void fillRestartMultiplayer(Container contentPane) {
		JButton restartMulti = new ImagedButton("/host_multi_button.png",restartMultiplayerAction,mainBackgroundColor);
		contentPane.add(restartMulti, new GridBagConstraints(GB_MENUCOLUMN, 4, 1, 1, 1d, 0d,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				HORIZONTAL_PADDING, 30, 30));
	}

	protected void fillFindMultiplayerRestart(Container contentPane) {
		JButton button = new ImagedButton("/find_multi_button.png",findMultiplayerAction,mainBackgroundColor);
		contentPane.add(button, new GridBagConstraints(GB_MENUCOLUMN, 5, 1, 1, 1d, 0d,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				HORIZONTAL_PADDING, 30, 30));
	}

	protected void fillIpAddressField(Container contentPane) {
		contentPane.add(ipField, new GridBagConstraints(GB_MENUCOLUMN, 6, 1, 1, 1d, 0d,
				GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
				HORIZONTAL_PADDING, 30, 30));
	}

	protected void fillVersionLabel(Container contentPane) {
		JLabel version = new JLabel(VERSION_STRING);
		contentPane.add(version, new GridBagConstraints(GB_MENUCOLUMN, 7, 1, 1, 1d, 1d,
				GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL,
				HORIZONTAL_PADDING, 30, 30));
	}
	
	protected void repaintPanels() {
		gamePanel.repaint();
	}

	protected void start() {
		if (!started) {
			this.started = true;
			timer.start();
		}
	}

	protected void stop() {
		if (gameTick != null) {
			gameTick.shutdown();
		}
		if (started) {
			timer.stop();
		}
		executor.shutdown();
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		this.start();
	}

	@Override
	public void dispose() {
		log.info("Disposing window {}", this);
		this.stop();
		if(sound) music.stop();
		super.dispose();
	}

	public String getIpValue() {
		return ipField.getText();
	}

	/**
	 * @return the exitAction
	 */
	public Action getExitAction() {
		return exitAction;
	}

	public void setScore(String score) {
		this.scoreLabel.setText(score);
	}

	/**
	 * @return the restartSinglePlayer
	 */
	public Action getRestartSinglePlayer() {
		return restartSinglePlayer;
	}

	/**
	 * @return the restartMultiplayerAction
	 */
	public Action getRestartMultiplayerAction() {
		return restartMultiplayerAction;
	}

	/**
	 * @return the findMultiplayerAction
	 */
	public Action getFindMultiplayerAction() {
		return findMultiplayerAction;
	}

	@Override
	public GameController getController() {
		return gameController;
	}
	
	public ScheduledExecutorService getScheduledExecutorService() {
		return executor;
	}

	private ImageIcon getLogoImage(String url) {
		try {
			BufferedImage scale = ImageIO.read(ImagedButton.class.getResourceAsStream(url));
			ImageIcon out = new ImageIcon(scale);
			return out;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
