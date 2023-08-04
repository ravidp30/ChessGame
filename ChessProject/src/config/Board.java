package config;

import java.util.ArrayList;

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
				System.out.println("piece "+ piece.getname()+" in position: " + piece.getX() + "," +piece.getY());
				return piece;
			}
		}
		return null;
	}
	
	public void Move(int oldX,int oldY, int newX, int newY) {//move function
		for (Piece p:pieces) {
			System.out.println("location of:" + p.getname() +" in: " + p.getX()+ ", " +p.getY());
			System.out.println("old:" + oldX+","+ oldY );
			System.out.println("new:" + newX+","+ newY );

			if(p.getX()==newX && p.getY()==newY) {//found piece there already
				if(p.isWhite()) {System.out.println("cant move there- somone is there");return;}
				else {Kill();System.out.println("kill");return;}//KILL
			}
			if(p.getX()==oldX && p.getY()==oldY){//found the current piece
					System.out.println("1");
				p.setX(newX);//change the location
				p.setY(newY);
				System.out.println("new location of " + p.getname() +" in: " + p.getX()+","+p.getY());
			}	
		}
	}
    	
	public void Kill() {// Kill function
		pieces.remove(this);//remove the killed piece from list
	}

}