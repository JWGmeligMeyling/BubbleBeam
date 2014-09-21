package nl.tudelft.ti2206.multiplayer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

import nl.tudelft.ti2206.bubbles.BubbleMesh;

public abstract class Packet implements Serializable {
	
	private static final long serialVersionUID = 4580730266240016922L;
	
	public abstract void work(PacketHandler handler);
	
	public void send(final AsynchronousSocketChannel channel)
			throws IOException {

		try (ByteArrayOutputStream bas = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(bas)) {

			out.writeObject(this);
			ByteBuffer buffer = ByteBuffer.wrap(bas.toByteArray());
			channel.write(buffer);

		}
	}
	
	public static interface PacketHandler {
		
		public void updateMesh(BubbleMesh mesh);
		
		public void updateCannon(double angle);
		
	}

	public static class MeshPacket extends Packet {
		
		private static final long serialVersionUID = -7715566690512079625L;
		
		private BubbleMesh mesh;
		
		protected MeshPacket() {};
		
		public void setMesh(final BubbleMesh mesh) {
			this.mesh = mesh;
		}
		
		@Override
		public void work(final PacketHandler handler) {
			handler.updateMesh(mesh);
		}
		
	}
	
	public static class CannonPacket extends Packet {

		private static final long serialVersionUID = -6973642136575239306L;
		
		private double angle;
		
		@Override
		public void work(PacketHandler handler) {
			handler.updateCannon(angle);
		}
		
		protected CannonPacket() {};
		
		public void setAngle(double angle) {
			this.angle = angle;
		}
		
	}
	
	public static MeshPacket newMeshPacket(final BubbleMesh mesh) {
		MeshPacket packet = new MeshPacket();
		packet.setMesh(mesh);
		return packet;
	}
	
	public static CannonPacket newCannonPacket(double angle) {
		CannonPacket packet = new CannonPacket();
		packet.setAngle(angle);
		return packet;
	}
	
}
