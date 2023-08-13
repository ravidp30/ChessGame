package config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

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
		if(pieces == null) { // initialize the board at first with null ArrayList and on SetUpPiece its adding to the pieces arraylist here in the board
			this.pieces = new ArrayList<>();
		}
		else {
			this.pieces = pieces; // this for checking when creating new board for checking check
		}
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
	
	/*public ArrayList<Piece> getPieces(){
		ArrayList<Piece> tempPieces = new ArrayList<>();
		tempPieces = pieces;
		return tempPieces;
	}*/
	
	public ArrayList<Piece> getPieces(){
		return pieces;
	}
	
	public void setPieces(ArrayList<Piece> pieces){
		this.pieces = pieces;
	}
	
	
	public int MoveCheck(int oldX, int oldY, int newX, int newY) {//move check function
		
		Piece newPiece = getPiece(newX, newY);
		//Piece oldPiece  = getPiece(oldX, oldY);
		
		if(newPiece == null || newPiece.getname().equals("empty")) { // move to empty place
			//setPieceXY(oldPiece, newX, newY);
			return 1; //Available to move to empty place
		}
		
		//else { // move to a place with black / white piece
		
			if(newPiece.isWhite()) {
				System.out.println("cant move there- someone is there");
				return 0;
			}
			else if(!newPiece.isWhite()){
				System.out.println("eat");
				return 2;
			}
			else {
				return 1;
		}
	}
	
    public void addPiece(Piece piece) {
        pieces.add(piece);
    }
	
	public void removePiece(int x, int y) {
	    Iterator<Piece> iterator = pieces.iterator();
	    while (iterator.hasNext()) {
	        Piece p = iterator.next();
	        if (p.getX() == x && p.getY() == y) {
	            iterator.remove(); // Safe way to remove the current element
	        }
	    }
	}
	
	
	
	
	/*
	public int MoveCheck(Piece firstPieceSelected ,Piece piece) {//move check function
		
		if(piece.getname().equals("empty place")) { // move to empty place
			
			for (Piece p:pieces) {
				if(p.getX()==firstPieceSelected.getX() && p.getY()==firstPieceSelected.getY()){//found the current piece
					p.setX(piece.getX());//change the location
					p.setY(piece.getY());
					return 1; //Available to move to empty place
				}	
			}
		}
		
		else { // move to a place with black / white piece
		
			for (Piece p:pieces) {
				
				if(p.getX()==piece.getX() && p.getY()==piece.getY()) {//found piece there already
					if(p.isWhite()) { // move to place with white piece
						System.out.println("cant move there- someone is there");
						return 0;
					}
		
					else { // move to place with black piece
						int x = p.getX();
						int y = p.getY();
						pieces.remove(p); // remove the piece from the list
						System.out.println("kill");
						
						
						setPieceXY(firstPieceSelected, x, y);
						
						return 2;
					}//KILL
				}
		
			}
		}
		System.out.println("?");
		return 0;
	}
*/
}