package config;

import java.util.ArrayList;

import player.Piece;

public class Board {
	
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
	
	public void Move(int oldX,int oldY, int newX, int newY) {//move function
		
		for (Piece p:pieces)
			if(p.getX()==oldX && p.getY()==oldY) Kill();//kill
		this.x=newX; //move the piece to new location
		this.y=oldX;
	}
	
	
	public void Kill() {// Kill function
		pieces.remove(this);//remove the killed piece from the list 
	}

}