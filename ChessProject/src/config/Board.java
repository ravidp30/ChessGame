package config;

import java.io.Serializable;
import java.util.ArrayList;

public class Board implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x, y;
	private ArrayList<Piece> pieces;
	
	public Board(int x, int y, ArrayList<Piece> pieces) {
		this.x = x;
		this.y = y;
		this.pieces = pieces;
	}
	
	public Piece getPiece(int x, int y) {
		for(Piece piece : pieces) {
			if(piece.getX() == x && piece.getY() == y) {
				return piece;
			}
		}
		return null;
	}
	
	public void setPieceXY(Piece piece, int x, int y) {
		for(Piece p : pieces) {
			if(p.getX() == piece.getX() && p.getY() == piece.getY()) {
				p.setX(x);
				p.setY(y);
			}
		}
	}
	
	public int MoveCheck(Piece firstPieceSelected ,Piece piece) {//move check function
		
		if(piece.getname().equals("empty place")) { // move to empty place
			return 1; //Available to move to empty place
		}
		
		else { // move to a place with black / white piece
		
			for (Piece p:pieces) {
				
				if(p.getX()==piece.getX() && p.getY()==piece.getY()) {//found piece there already
					if(p.isWhite()) { // move to place with white piece
						System.out.println("cant move there- somone is there");
						return 0;
					}
		
					else { // move to place with black piece
						pieces.remove(p); // remove the piece from the list
						System.out.println("kill");
						return 2;
					}//KILL
				}
		
			}
		}
		System.out.println("?");
		return 0;
	}

}