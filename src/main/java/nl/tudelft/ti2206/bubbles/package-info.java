/**
 * The bubbles package contains the various types of {@link nl.tudelft.ti2206.bubbles.Bubble} implementations.
 * Bubbles are stored in a {@link nl.tudelft.ti2206.bubbles.mesh.BubbleMesh}, and know their neighbouring bubbles,
 * so that during popping these neighbours can be found by relatively easy traversal.
 * The {@code BubbleMesh} contains the logic to store and  maintain the structure of the Bubbles,
 * for example to load a bubble map from a file, to replace a bubble on the mesh, to pop bubbles or to insert a new row after a few misses.
 * Places that are not occupied by "hittable" bubbles like the {@link nl.tudelft.ti2206.bubbles.ColouredBubble}, are occupied by {@link nl.tudelft.ti2206.bubbles.BubblePlaceholder BubblePlaceholders}
 * which can easily be replaced by a {@link nl.tudelft.ti2206.bubbles.decorators.MovingBubble} when it hits a hittable bubble or collides with the top wall.
 * 
 * @author Jan-Willem Gmelig Meyling
 */
package nl.tudelft.ti2206.bubbles;

