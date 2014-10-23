package nl.tudelft.ti2206.game.backend;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class GameTickImplTest {

	protected GameTickImpl gameTick;
	protected Tickable tickable;
	protected ScheduledExecutorService executor;
	protected Runnable runnable; 
	
	protected ScheduledFuture feature;
	
	@Before
	public void setUp() throws Exception {
		tickable = Mockito.mock(Tickable.class);
		executor = Mockito.mock(ScheduledExecutorService.class);
		feature = Mockito.mock(ScheduledFuture.class);
		
		Mockito.when(
				executor.scheduleAtFixedRate(Mockito.any(), Mockito.anyLong(),
						Mockito.anyLong(), Mockito.any())).thenReturn(feature);
		
		ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
		
		long framePeriod = 200;
		gameTick = new GameTickImpl(framePeriod, executor);
		
		Mockito.verify(executor).scheduleAtFixedRate(runnableCaptor.capture(),
				Mockito.eq(0l), Mockito.eq(framePeriod),
				Mockito.eq(TimeUnit.MILLISECONDS));

		Mockito.reset(executor);
		
		gameTick.registerObserver(tickable);
		Mockito.verifyZeroInteractions(tickable);
		
		runnable = runnableCaptor.getValue();
	}

	@Test
	public void testGameTick() {
		runnable.run();
		Mockito.verify(tickable).gameTick();
	}
	
	@Test
	public void testRemoveTick() {
		gameTick.removeObserver(tickable);
		runnable.run();
		Mockito.verifyZeroInteractions(tickable);
	}
	
	@Test
	public void testShutdown() {
		gameTick.shutdown();
		Mockito.verify(feature).cancel(true);
		Mockito.verifyZeroInteractions(executor);
	}

}
