package nl.tudelft.ti2206.throwaway;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class BubbleMesh implements Iterable<Bubble> {
	
	private static final Logger log = LoggerFactory.getLogger(BubbleMesh.class);

	private final List<Bubble> startBubbles;
	
	private final List<Bubble> allBubbles;
	
	public final static String TEMPLATE_STRING = "xxxxxxx\n"
			+ "xxx x\n"
			+ "xx xx\n"
			+ "xxx x\n";
	
	public static BubbleMesh parse(File file) throws FileNotFoundException, IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)))) {
			List<String> lines = Lists.newArrayList();
			while (reader.ready()) {
				lines.add(reader.readLine());
			}
			return parse(lines);
		}
	}
	
	public static BubbleMesh parse(String string) {
		return parse(Arrays.asList(string.split("[\r\n]+")));
	}
	
	public static BubbleMesh parse(List<String> rows) {
		int rowAmount = rows.size();
		if (rowAmount < 1)
			throw new IllegalArgumentException("Wrong file format");
		int rowSize = rows.get(0).length();
		
		Bubble[][] bubbles = new Bubble[rowAmount][rowSize];
		BubbleMesh result = new BubbleMesh();
		
		for(int i = 0; i < rowAmount; i++) {
			String rowStr = rows.get(i);
			for(int j = 0; j < rowSize; j++) {
				if(rowStr.charAt(j) != 'x') continue;
				Bubble bubble = bubbles[i][j] = new Bubble();
				
				if(i > 0) {
					if(i % 2 == 0) { // 3rd, 5fth rows , ... 
						bubble.setTopRight(bubbles[i-1][j]);
						if(j > 1) {
							bubble.setTopLeft(bubbles[i-1][j-1]);
						}
					}
					else {
						bubble.setTopLeft(bubbles[i-1][j]);
						if(j < (rowSize - 1)) {
							bubble.setTopRight(bubbles[i-1][j+1]);
						}
					}
				}
				
				if(j > 0) {
					bubble.setLeft(bubbles[i][j-1]);
				}
				
				result.allBubbles.add(bubble);
			}
		}
		
		return result;
	}
	
	public BubbleMesh() {
		
//		Bubble b1 = new Bubble();
//		Bubble b2 = new Bubble();
//		Bubble b3 = new Bubble();
//		Bubble b4 = new Bubble();
//		Bubble b5 = new Bubble();
//		Bubble b6 = new Bubble();
//
//		b1.setRight(b2);
//		b2.setRight(b3);
//		b1.setBottomRight(b4);
//		b2.setBottomLeft(b4);
//		b2.setBottomRight(b5);
//		b3.setBottomLeft(b5);
//		b4.setBottomRight(b6);
//		b5.setBottomLeft(b6);

		this.allBubbles = Lists.newArrayList();
		this.startBubbles = Lists.newArrayList();
		
//		this.startBubbles = Lists.newArrayList(b1);
//		this.allBubbles = Lists.newArrayList(b1, b2, b3, b4, b5, b6);
	}

	@Override
	public Iterator<Bubble> iterator() {
		return allBubbles.iterator();
	}

	public List<Bubble> getStartBubbles() {
		return startBubbles;
	}

	public void calculatePositions() {
		for(Bubble bubble : allBubbles.subList(1, allBubbles.size())) {
			Point newPosition = bubble.calculatePosition();
			bubble.setPosition(newPosition);
			log.info("Changed bubble position to {}", newPosition);
		}
	}
	
}
