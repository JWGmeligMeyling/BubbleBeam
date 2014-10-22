package nl.tudelft.ti2206.network.packets;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

import nl.tudelft.ti2206.game.event.BubbleMeshListener.BubblePopEvent;
import nl.tudelft.ti2206.game.event.BubbleMeshListener.RowInsertEvent;
import nl.tudelft.ti2206.game.event.GameListener.ScoreEvent;
import nl.tudelft.ti2206.game.event.CannonListener.CannonRotateEvent;
import nl.tudelft.ti2206.game.event.CannonListener.CannonShootEvent;
import nl.tudelft.ti2206.game.event.GameListener.GameOverEvent;
import nl.tudelft.ti2206.game.event.GameListener.ShotMissedEvent;
import nl.tudelft.ti2206.game.event.GameListener.AmmoLoadEvent;

import com.google.common.collect.Lists;

/**
 * The {@code PacketListener} listens for {@link Packet Packets}.
 * 
 * @author Jan-Willem Gmelig Meyling
 *
 */
public interface PacketListener extends EventListener {

	/**
	 * Handle a generic {@link Packet} and delegate it to its
	 * {@link PacketHandler}
	 * 
	 * @param packet
	 *            {@code Packet} to be handled
	 */
	default void handlePacket(Packet packet) {
		try {
			Method method = getAnnotatedMethodForArgument(PacketListener.class, packet.getClass(), PacketHandler.class);
			method.invoke(this, packet);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {

			throw new RuntimeException("Failed to invoke handler for packet "
					+ packet.toString(), e);
		}
	}
	
	/**
	 * Handle a generic {@link EventObject} and delegate it to its
	 * {@link EventHandler}
	 * 
	 * @param event
	 *            {@code EventObject} to be triggered
	 */
	default void handleEvent(EventObject event) {
		try {
			Method method = getAnnotatedMethodForArgument(PacketListener.class, event.getClass(), EventHandler.class);
			method.invoke(this, event);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {

			throw new RuntimeException("Failed to invoke handler for event "
					+ event.toString(), e);
		}
	}
	
	/**
	 * Search for an annotated {@link Method} with a specific parameter type.
	 * This is used to find to which {@link EventHandler} a {@link EventObject}
	 * and to which {@link PacketHandler} a {@link Packet} should be delegated.
	 * 
	 * @param type
	 *            {@link Parameter} type the {@code Method} should have
	 * @param annotation
	 *            {@link Annotation} the {@code Method} should have
	 * @return The {@code Method}
	 */
	public static Method getAnnotatedMethodForArgument(Class<?> klass, final Class<?> type,
			final Class<? extends Annotation> annotation) {
		
		while (klass != Object.class) {
			final List<Method> allMethods = Lists.newArrayList(klass
					.getDeclaredMethods());

			for (final Method method : allMethods) {
				Class<?>[] parameters = method.getParameterTypes();
				if (method.isAnnotationPresent(annotation)
						&& parameters.length == 1
						&& parameters[0].equals(type)) {
					return method;
				}
			}

			klass = klass.getSuperclass();
		}
		
		throw new RuntimeException("No method found with the annotation "
				+ annotation.toString() + " and class " + type.getName());
	}
	
	@PacketHandler
	default void handleEventPacket(EventPacket packet) {
		handleEvent(packet.data);
	}

	@PacketHandler
	void handleGameModelPacket(GameModelPacket packet);

	@EventHandler
	void handleCannonRotate(CannonRotateEvent event);

	@EventHandler
	void handleCannonShoot(CannonShootEvent event);

	@EventHandler
	void handleRowInsert(RowInsertEvent event);

	@EventHandler
	void handleBubblePop(BubblePopEvent event);

	@EventHandler
	void handleScoreEvent(ScoreEvent event);

	@EventHandler
	void handleGameOver(GameOverEvent event);
	
	@EventHandler
	void handleShotMissed(ShotMissedEvent event);
	
	@EventHandler
	void handleAmmoLoad(AmmoLoadEvent event);
	
	default void disconnect() {};
	
	@FunctionalInterface
	interface GameModelPacketListener extends PacketListener {
		@Override default void handleCannonRotate(CannonRotateEvent event) {}
		@Override default void handleCannonShoot(CannonShootEvent event) {}
		@Override default void handleRowInsert(RowInsertEvent event) {}
		@Override default void handleBubblePop(BubblePopEvent event) {}
		@Override default void handleScoreEvent(ScoreEvent event) {}
		@Override default void handleGameOver(GameOverEvent event) {}
		@Override default void handleShotMissed(ShotMissedEvent event) {}
		@Override default void handleAmmoLoad(AmmoLoadEvent event) {}
	}
	
	@FunctionalInterface
	interface BubblePopListener extends PacketListener {
		@Override default void handleCannonRotate(CannonRotateEvent event) {}
		@Override default void handleCannonShoot(CannonShootEvent event) {}
		@Override default void handleRowInsert(RowInsertEvent event) {}
		@Override default void handleScoreEvent(ScoreEvent event) {}
		@Override default void handleGameOver(GameOverEvent event) {}
		@Override default void handleShotMissed(ShotMissedEvent event) {}
		@Override default void handleAmmoLoad(AmmoLoadEvent event) {}
		@Override default void handleGameModelPacket(GameModelPacket packet) {};
	}

}
