package config;

import java.util.ArrayList;

public class Soldier extends Piece{

	public Soldier(int x, int y, String name, Boolean isWhite) {
		super(x, y, name, isWhite);
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Piece> Move(Board board) {
		int currX=this.getX();
		int currY=this.getY();
		int left = 0,right=0,up=0,down = 0;
		ArrayList<Piece> pieces = new ArrayList<>();
		
		if(currY > 0) {
			pieces.add(new Piece(currX, currY-1, "soldierW", true));
		}
		if(currY == 6) {
			if(board.getPiece(currX, 5) == null)
				pieces.add(new Piece(currX, currY-2, "soldierW", true));
		}

		return pieces;
	}


}
