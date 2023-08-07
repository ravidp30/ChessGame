package config;

import java.util.ArrayList;

public class Queen extends Piece{

	public Queen(int x, int y, String name, Boolean isWhite) {
		super(x, y, name, isWhite);
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Piece>  Move(Board board) {
		boolean stop = false;
		int currX=this.getX();
		int currY=this.getY();
		int left = 0,right=0,up=0,down = 0;
		ArrayList<Piece> pieces = new ArrayList<>();
		System.out.println("1");
		//------------------------------ Right--------------------------

		if(currX<=7) 
			for(int i=1 ; currX+i<8; i++) { 
				if(board.getPiece(currX+i, currY) == null && !stop) {
					pieces.add(new Piece(currX+i, currY, "QueenW", true)); // right
				}
				else {
					stop = true;
				}
				right=i;
				
			}
		//------------------------------ Left--------------------------
		
		stop = false;
		if(currX>=0) 
			for(int i=1 ; currX-i>=0; i++) { 
				if(board.getPiece(currX-i, currY) == null && !stop) {
					pieces.add(new Piece(currX-i, currY, "QueenW", true));// left
				}
				else {
					stop = true;
				}
				left=i;
			}
		//------------------------------ Up--------------------------

		stop = false;
		if(currY<=7) 
			for(int i=1 ; currY-i>=0; i++) { 
				if(board.getPiece(currX, currY-i) == null && !stop) {
					pieces.add(new Piece(currX, currY-i, "QueenW", true));//up
				}
				else {
					stop = true;
				}
				up=i;
			}
		//------------------------------ Down--------------------------

		stop = false;
		if(currY>=0) 
			for(int i=1 ; currY+i<8; i++) { 
				if(board.getPiece(currX, currY+i) == null && !stop) {
					pieces.add(new Piece(currX, currY+i, "QueenW", true));// down
				}
				else {
					stop = true;
				}
				down=i;
			}
		
		//------------------------------ Down + Left--------------------------
		
		if(down>=left) 
			for(int i=1 ; left-i>=0 ; i++)
				if(board.getPiece(currX-i, currY+i) == null)
					pieces.add(new Piece(currX-i, currY+i, "QueenW", true));//down + left	
				else {
					break;
				}
			
		if(down<left) 
			for(int i=1 ; down-i>=0 ; i++) 
				if(board.getPiece(currX-i, currY+i) == null)
				pieces.add(new Piece(currX-i, currY+i, "QueenW", true));//down + left	
				else {
					break;
				}
									
		//------------------------------ Down + Right--------------------------
		
		if(down>=right) 
		for(int i=1 ; right-i>=0 ; i++) 
			if(board.getPiece(currX+i, currY+i) == null)
				pieces.add(new Piece(currX+i, currY+i, "QueenW", true)); // down + right
			else {
				break;
			}
		
		if(down<right) 
			for(int i=1 ; down-i>=0 ; i++)  
				if(board.getPiece(currX+i, currY+i) == null)
					pieces.add(new Piece(currX+i, currY+i, "QueenW", true)); // down + right
				else {
					break;
				}
			
		//------------------------------ Up + Left--------------------------
		
		if(up>=left) 
			for(int i=1 ; left-i>=0 ; i++)  
				if(board.getPiece(currX-i, currY-i) == null)
					pieces.add(new Piece(currX-i, currY-i, "QueenW", true));//up + left
				else {
					break;
				}
		
					
		if(up<left) 
			for(int i=1 ; up-i>=0 ; i++) 
				if(board.getPiece(currX-i, currY-i) == null)
					pieces.add(new Piece(currX-i, currY-i, "QueenW", true));//up + left
				else {
					break;
				}
			
		
		//------------------------------ Up + Right--------------------------
		if(up>=right) 
			for(int i=1 ; right-i>=0 ; i++)
				if(board.getPiece(currX+i, currY-i) == null)
					pieces.add(new Piece(currX+i, currY-i, "QueenW", true));//up + right
				else {
					break;
				}
						
		if(up<right) 
			for(int i=1 ; up-i>=0 ; i++) 
				if(board.getPiece(currX+i, currY-i) == null)
					pieces.add(new Piece(currX+i, currY-i, "QueenW", true));//up + right			
				else {
					break;
				}	
		
		for (Piece p : pieces) {
		System.out.println(p.getname() + "move option: " + p.getX() + ","+p.getY());
	}
		return pieces;
	}
	
	
}
