package config;

import java.util.LinkedList;

public class Piece {
	private int x;
	private int y;
	private String name;
	private boolean isWhite;
	private LinkedList<Piece> piecesList;
	
	public  Piece(int x,int y, String name,Boolean isWhite/*, LinkedList<Piece> piecesList*/) {
		this.x=x;
		this.y=y;
		this.name=name;
		this.isWhite=isWhite;
		//this.piecesList=piecesList;
		//piecesList.add(this);//adding new piece
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public void setX(int newX) {
		x=newX;
	}
	public void setY(int newY) {
		y=newY;
	}
	
	
}
