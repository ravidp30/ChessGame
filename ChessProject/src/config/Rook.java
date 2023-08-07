package config;

import java.util.ArrayList;

public class Rook extends Piece{

	public Rook(int x, int y, String name, Boolean isWhite) {
		super(x, y, name, isWhite);
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Piece>  Move(Board board) {
		int currX=this.getX();
		int currY=this.getY();
		ArrayList<Piece> pieces = new ArrayList<>();
		
		//------------------------------ Right--------------------------

		if(currX<=7) 
			for(int i=1 ; currX+i<8; i++) { 
				if(board.getPiece(currX+i, currY) == null)
					pieces.add(new Piece(currX+i, currY, "RookW", true)); // right
				else {
					break;
				}
			}
		//------------------------------ Left--------------------------

		if(currX>=0) 
			for(int i=1 ; currX-i>=0; i++) { 
				if(board.getPiece(currX-i, currY) == null)
					pieces.add(new Piece(currX-i, currY, "RookW", true));// left
				else {
					break;
				}
			}
		//------------------------------ Up--------------------------

		if(currY<=7) 
			for(int i=1 ; currY-i>=0; i++) {
				if(board.getPiece(currX, currY-i) == null)
					pieces.add(new Piece(currX, currY-i, "RookW", true));//up
				else {
					break;
				}
			}
		//------------------------------ Down--------------------------

		if(currY>=0) 
			for(int i=1 ; currY+i<8; i++) { 
				if(board.getPiece(currX, currY+i) == null)
					pieces.add(new Piece(currX, currY+i, "RookW", true));// down
				else {
					break;
				}			
			}
		
		
		return pieces;
	}
	
	
}
