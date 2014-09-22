package nl.tudelft.ti2206;

import java.io.IOException;

import nl.tudelft.ti2206.bubbles.BubbleMesh;
import nl.tudelft.ti2206.game.GUI;

public class tempBubbleMeshParseTest {
	public static void main(String[] s){
		BubbleMesh mesh = null;
		try {
			mesh = BubbleMesh.parse(GUI.class
					.getResourceAsStream("/board.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String str = mesh.toParseableString();
		System.out.println(str);
		mesh = BubbleMesh.parse(str);
		String str2 = mesh.toParseableString();
		System.out.println(str2);
		System.out.println(str.equals(str2));
	}
}
