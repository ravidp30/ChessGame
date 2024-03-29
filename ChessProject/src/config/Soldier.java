package config;

import java.util.ArrayList;

public class Soldier extends Piece{

	public Soldier(int x, int y, String name, Boolean isWhite) {
		super(x, y, name, isWhite);
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Piece> Move(Board board) {
		Piece piece;
		int currX=this.getX();
		int currY=this.getY();
		ArrayList<Piece> pieces = new ArrayList<>();
		
		piece = board.getPiece(currX-1, currY-1); // left up
		if(piece != null && !piece.isWhite()) { // if there is opponent on left up (to eat)
			pieces.add(new Piece(currX-1, currY-1, piece.getname(), false));
		}
		
		piece = board.getPiece(currX+1, currY-1); // right up
		if(piece != null && !piece.isWhite()) { // if there is opponent on right up (to eat)
			pieces.add(new Piece(currX+1, currY-1, piece.getname(), false));
		}
		
		piece = board.getPiece(currX, currY-1); // next place to move (up)
		if(currY > 0 && piece == null) {
			pieces.add(new Piece(currX, currY-1, "empty", true));
		}
		
		piece = board.getPiece(currX, currY-2); 
		if(currY == 6 && piece == null && board.getPiece(currX, currY-1) == null) { // moving 2 times up only if there are no opponents 1 and 2 moves up
				pieces.add(new Piece(currX, currY-2, "empty", true));
		}

		return pieces;
	}


}
