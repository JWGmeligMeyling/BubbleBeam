package nl.tudelft.ti2206.network.packets;

import java.io.DataInputStream;
import java.io.IOException;

import nl.tudelft.ti2206.exception.UnknownPacketException;

/**
 * The task of the {@code PacketFactory} is to create Packets from data read
 * from a {@link DataInputStream}.
 * 
 * @author Sam Smulders
 */
public class PacketFactory {
	public static Packet readPacket(byte id, DataInputStream in) throws IOException {
		/*
		 * The switch is necessary to detect the difference between the raw data
		 * of packages before we can reconstruct the packages.
		 */
		switch (id) {
		case Packet.CannonRotate.PACKET_ID:
			return new Packet.CannonRotate(in);
		case Packet.CannonShoot.PACKET_ID:
			return new Packet.CannonShoot(in);
		case Packet.LoadNewBubble.PACKET_ID:
			return new Packet.LoadNewBubble(in);
		case Packet.BubbleMeshSync.PACKET_ID:
			return new Packet.BubbleMeshSync(in);
		case Packet.RoomSynRequest.PACKET_ID:
			return new Packet.RoomSynRequest(in);
		default:
			throw (new UnknownPacketException("Packet Identifier is unknown."));
		}
	}
	
}
