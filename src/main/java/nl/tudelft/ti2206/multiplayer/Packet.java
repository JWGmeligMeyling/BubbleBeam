package nl.tudelft.ti2206.multiplayer;

import java.io.Serializable;

import nl.tudelft.ti2206.bubbles.BubbleMesh;

public abstract class Packet implements Serializable {

	private static final long serialVersionUID = 4580730266240016922L;
	
	public abstract void work(PacketHandler handler);
	
	public static interface PacketHandler {
		
		public void updateMesh(BubbleMesh mesh);
		
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
	
	public static MeshPacket newMeshPacket(final BubbleMesh mesh) {
		MeshPacket packet = new MeshPacket();
		packet.setMesh(mesh);
		return packet;
	}
	
}
