package nl.tudelft.ti2206.room;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;

import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.cannon.CannonMultiplayerConnector;
import nl.tudelft.ti2206.cannon.MouseCannonController;
import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.packets.Packet;

public class MultiplayerRoom extends MasterRoom {
	
	protected Connector connector;
	protected CannonMultiplayerConnector cannonMultiplayerConnector;
	private Component component;
	
	public MultiplayerRoom(Point cannonPosition, Dimension dimension, BubbleMesh bubbleMesh,
			Connector connector, Component component) {
		super(cannonPosition, dimension, bubbleMesh);
		cannonController = new MouseCannonController(this);
		cannonController.registerObserver(cannon);
		this.component = component;
		component.addMouseListener(cannonController);
		component.addMouseMotionListener(cannonController);
		
		this.cannonMultiplayerConnector = new CannonMultiplayerConnector(connector);
		cannonController.registerObserver(cannonMultiplayerConnector);
		
		component.addMouseListener(cannonController);
		component.addMouseMotionListener(cannonController);
		
		this.connector = connector;
		
		/*
		while(true){			//TODO make this better
			if(connector.isReady()){
				sendMesh();
			} else {
				break;
			}
		}
		*/
	}
	
	// TODO: Sending the mesh somehow..
	protected void sendMesh() {
		connector.sendPacket(new Packet.BubbleMeshSync(""));
	}
	
	@Override
	protected void addBubble() {
		Color color = bubbleMesh.getRandomRemainingColor();
		connector.sendPacket(new Packet.LoadNewBubble(color));
		bubbleQueue.add(new ColouredBubble(color));
	}

	@Override
	public void deconstruct() {
		cannonController.removeObserver(cannon);
		cannonController.removeObserver(cannonMultiplayerConnector);
		component.removeMouseListener(cannonController);
		component.removeMouseMotionListener(cannonController);
	}
}
