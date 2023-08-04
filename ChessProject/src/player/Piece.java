package player;

import java.util.LinkedList;

public class Piece {
	int x;
	int y;
	String name;
	boolean isWhite;
	LinkedList<Piece> piecesList;
	
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

	public void Move(int x,int y) {//move function
		
		for (Piece p:piecesList)
			if(p.x==x && p.y==y) Kill();//kill
		this.x=x; //move the piece to new location
		this.y=y;
	}
	
	
	public void Kill() {// Kill function
		piecesList.remove(this);//remove the killed piece from the list 
	}
}
