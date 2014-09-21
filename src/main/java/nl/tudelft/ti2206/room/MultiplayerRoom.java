package nl.tudelft.ti2206.room;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.bubbles.ColouredBubble;
import nl.tudelft.ti2206.network.Connector;
import nl.tudelft.ti2206.network.packets.Packet;

public class MultiplayerRoom extends MasterRoom {
	
	private Connector connector;
	
	public MultiplayerRoom(Point cannonPosition, Dimension dimension, BubbleMesh bubbleMesh,
			Connector connector) {
		super(cannonPosition, dimension, bubbleMesh);
		this.connector = connector;
		
		sendMesh();
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
}
