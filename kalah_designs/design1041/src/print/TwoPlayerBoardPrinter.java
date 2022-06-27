package print;

import com.qualitascorpus.testsupport.IO;

public class TwoPlayerBoardPrinter extends BoardPrinter {
	public void printState(IO io, int[][] allHouses, int[] allStores) {
		final String boardCorner = "+----+"; // Far left/right of the top/bottom of the board
		final String boardMiddleEdge = "|    |"; // Far left/right of the middle of the board
		final String boardRepeating = "-------+"; // Middle part of top/middle/bottom of the board		
		final String houseFormat = "%s[%s] |";
		
		int[] p1Houses = allHouses[0];
		int[] p2Houses = allHouses[1];
		int p1Store = allStores[0];
		int p2Store = allStores[1];
		
		int numHouses = p1Houses.length;
		
		StringBuilder 
			edge = new StringBuilder(),
			topRow = new StringBuilder(),
			middle = new StringBuilder(),
			bottomRow = new StringBuilder();
		
		//Set up the left side of the board
		edge.append(boardCorner);
		topRow.append("| P2 |");
		middle.append(boardMiddleEdge);
		bottomRow.append("| " + leftPad(p2Store) + " |");
		
		//Repeating part
		for (int i = 0; i < numHouses; i++) {
			edge.append(boardRepeating);
			int reversedI = numHouses - 1 - i; //print p2's houses backwards
			topRow.append(formatHouseString(houseFormat, reversedI, p2Houses[reversedI]));
			middle.append(boardRepeating);
			bottomRow.append(formatHouseString(houseFormat, i, p1Houses[i]));
		}
		
		//Right side
		//We need to remove one character from 'edge' and 'middle'
		trim(edge); 
		edge.append(boardCorner);
		topRow.append(" " + leftPad(p1Store) + " |");
		trim(middle);
		middle.append(boardMiddleEdge);
		bottomRow.append(" P1 |");
		
		io.println(edge.toString());
		io.println(topRow.toString());
		io.println(middle.toString());
		io.println(bottomRow.toString());
		io.println(edge.toString());
	}
	
	//Private convenience methods
	
	/**
	 * Format a house + stones element for the printing of the board.
	 * Both values must be left-padded.
	 * @param format
	 * @param arg1
	 * @param arg2
	 * @return
	 */
	private String formatHouseString(String format, int arg1, int arg2) {
		return String.format(format, leftPad(convertToPrintValue(arg1)), leftPad(arg2));
	}
	
	/**
	 * Remove the last character. When appending BOARD_MIDDLE, 
	 * we need to remove the last '+' when we reach the end.
	 * @param sb
	 */
	private void trim(StringBuilder sb) {
		sb.setLength(sb.length() - 1);
	}
	
	/**
	 * Pad the number of seeds/house number with a space if there are less than 10.
	 * Used when printing the board.
	 * @param n
	 * @return
	 */
	private String leftPad(int n) {
		if (n < 10) {
			return " " + n;
		} else {
			return "" + n;
		}
	}
	
}
