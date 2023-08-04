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
		this.setWhite(isWhite);
		//this.piecesList=piecesList;
		//piecesList.add(this);//adding new piece
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}

	public void setX(int newX) {
		x=newX;
	}
	public void setY(int newY) {
		y=newY;
	}

	public boolean isWhite() {
		return isWhite;
	}

	public void setWhite(boolean isWhite) {
		this.isWhite = isWhite;
	}
	public String getname() {
		return this.name;
	}
	
}
