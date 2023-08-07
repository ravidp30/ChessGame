package config;

import java.util.ArrayList;

public class Rook extends Piece{

	public Rook(int x, int y, String name, Boolean isWhite) {
		super(x, y, name, isWhite);
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Piece>  Move(Board board) {
		Piece piece;
		int currX=this.getX();
		int currY=this.getY();
		ArrayList<Piece> pieces = new ArrayList<>();
		
		//------------------------------ Right--------------------------

		if(currX<=7) 
			for(int i=1 ; currX+i<8; i++) { 
				piece = board.getPiece(currX+i, currY);
				if(piece == null)
					pieces.add(new Piece(currX+i, currY, "RookW", true)); // right
				else if(!piece.isWhite()) {
					pieces.add(new Piece(currX+i, currY, "RookW", true)); // right
					break;
				}
				else {
					break;
				}
			}
		//------------------------------ Left--------------------------

		if(currX>=0) 
			for(int i=1 ; currX-i>=0; i++) { 
				piece = board.getPiece(currX-i, currY);
				if(piece == null)
					pieces.add(new Piece(currX-i, currY, "RookW", true));// left
				else if(!piece.isWhite()) {
					pieces.add(new Piece(currX-i, currY, "RookW", true)); // right
					break;
				}
				else {
					break;
				}
			}
		//------------------------------ Up--------------------------

		if(currY<=7) 
			for(int i=1 ; currY-i>=0; i++) {
				piece = board.getPiece(currX, currY-i);
				if(piece == null)
					pieces.add(new Piece(currX, currY-i, "RookW", true));//up
				else if(!piece.isWhite()) {
					pieces.add(new Piece(currX, currY-i, "RookW", true)); // right
					break;
				}
				else {
					break;
				}
			}
		//------------------------------ Down--------------------------

		if(currY>=0) 
			for(int i=1 ; currY+i<8; i++) { 
				piece = board.getPiece(currX, currY+i);
				if(piece == null)
					pieces.add(new Piece(currX, currY+i, "RookW", true));// down
				else if(!piece.isWhite()) {
					pieces.add(new Piece(currX, currY+i, "RookW", true)); // right
					break;
				}
				else {
					break;
				}			
			}
		
		
		return pieces;
	}
	
	
}
