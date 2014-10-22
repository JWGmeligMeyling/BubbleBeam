package nl.tudelft.ti2206.network.packets;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import java.util.function.Predicate;

import nl.tudelft.ti2206.game.event.BubbleMeshListener.BubblePopEvent;
import nl.tudelft.ti2206.game.event.BubbleMeshListener.RowInsertEvent;
import nl.tudelft.ti2206.game.event.BubbleMeshListener.ScoreEvent;
import nl.tudelft.ti2206.game.event.CannonListener.CannonRotateEvent;
import nl.tudelft.ti2206.game.event.CannonListener.CannonShootEvent;
import nl.tudelft.ti2206.game.event.GameListener.GameOverEvent;
import nl.tudelft.ti2206.game.event.GameListener.ShotMissedEvent;
import nl.tudelft.ti2206.game.event.GameListener.AmmoLoadEvent;

import com.google.common.collect.Lists;

public interface PacketListener extends EventListener {

	default void handlePacket(Packet packet) {
		try {
			Method method = getMethodAnnotatedWith(PacketListener.class,
					PacketHandler.class, annotation -> annotation
							.value().equals(packet.getClass()));
			method.invoke(this, packet);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {

			throw new RuntimeException("Failed to invoke handler for packet "
					+ packet.toString(), e);
		}
	}
	
	default void handleEvent(EventObject event) {
		try {
			Method method = getMethodAnnotatedWith(PacketListener.class,
					EventHandler.class, annotation -> annotation
							.value().equals(event.getClass()));
			method.invoke(this, event);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {

			throw new RuntimeException("Failed to invoke handler for event "
					+ event.toString(), e);
		}
	}
	
	public static <T extends Annotation> Method getMethodAnnotatedWith(
			final Class<?> type, final Class<T> annotation,
			final Predicate<T> predicate) {

		Class<?> klass = type;

		while (klass != Object.class) {
			final List<Method> allMethods = Lists.newArrayList(klass
					.getDeclaredMethods());

			for (final Method method : allMethods) {
				if (method.isAnnotationPresent(annotation)) {
					T annotInstance = method.getAnnotation(annotation);
					if (predicate.test(annotInstance)) {
						return method;
					}
				}
			}

			klass = klass.getSuperclass();
		}

		throw new RuntimeException("No method found with the annotation "
				+ annotation.toString() + " and class " + type.getName());
	}
	
	@PacketHandler(EventPacket.class)
	default void handleEventPacket(EventPacket packet) {
		handleEvent(packet.data);
	}

	@PacketHandler(GameModelPacket.class)
	void handleGameModelPacket(GameModelPacket packet);

	@EventHandler(CannonRotateEvent.class)
	void handleCannonRotate(CannonRotateEvent event);

	@EventHandler(CannonShootEvent.class)
	void handleCannonShoot(CannonShootEvent event);

	@EventHandler(RowInsertEvent.class)
	void handleRowInsert(RowInsertEvent event);

	@EventHandler(BubblePopEvent.class)
	void handleBubblePop(BubblePopEvent event);

	@EventHandler(ScoreEvent.class)
	void handleScoreEvent(ScoreEvent event);

	@EventHandler(GameOverEvent.class)
	void handleGameOver(GameOverEvent event);
	
	@EventHandler(ShotMissedEvent.class)
	void handleShotMissed(ShotMissedEvent event);
	
	@EventHandler(AmmoLoadEvent.class)
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
