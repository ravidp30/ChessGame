package config;

import java.util.ArrayList;

public class Knight extends Piece{

	public Knight(int x, int y, String name, Boolean isWhite) {
		super(x, y, name, isWhite);
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Piece>  Move(Board board) {
		int currX=this.getX();
		int currY=this.getY();
		Piece piece;
		ArrayList<Piece> pieces = new ArrayList<>();

		
		for(int x = currX-2; x <= currX+2; x++) {
			for(int y = currY-2; y <= currY+2; y++) {
				if(x-currX == 2 || currX-x == 2 || currY-y == 2 || y-currY == 2) {
					if(x >= 0 && x <= 7 && y >= 0 && y <= 7) {
						if(x != currX && y != currY) {
							if(!((x-currX == 2 || currX-x == 2) && (currY-y == 2 || y-currY == 2))) {
								piece = board.getPiece(x, y);
								if(piece == null) {
									pieces.add(new Piece(x, y, "empty", true));
								}else if(!piece.isWhite()) {
									pieces.add(new Piece(x, y, piece.getname(), false));
								}
							}
						}
					}
				}
				
			}
		}
		
		
		return pieces;
	}



}
