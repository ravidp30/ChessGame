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
		
		for (Piece p:pieces) {
			
			if(p.getX()==piece.getX() && p.getY()==piece.getY()) {//found piece there already
				if(p.isWhite()) {System.out.println("cant move there- somone is there");return 0;}
				else {Kill();System.out.println("kill");return 1;}//KILL
			}
			if(p.getX()==firstPieceSelected.getX() && p.getY()==firstPieceSelected.getY()){//found the current piece
				p.setX(piece.getX());//change the location
				p.setY(piece.getY());
				return 1; //Available to move
			}	
		}
		System.out.println("?");
		return 0;
	}
    	
	public void Kill() {// Kill function
		pieces.remove(this);//remove the killed piece from list
	}

}