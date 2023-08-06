package config;

import java.util.ArrayList;

public class Queen extends Piece{

	public Queen(int x, int y, String name, Boolean isWhite) {
		super(x, y, name, isWhite);
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Piece>  Move() {
		int currX=this.getX();
		int currY=this.getY();
		int left = 0,right=0,up=0,down = 0;
		ArrayList<Piece> pieces = new ArrayList<>();
		System.out.println("1");
		//------------------------------ Right--------------------------

		if(currX<=7) 
			for(int i=1 ; currX+i<8; i++) { 
				pieces.add(new Piece(currX+i, currY, "QueenW", true)); // right
				right=i;
			}
		//------------------------------ Left--------------------------

		if(currX>=0) 
			for(int i=1 ; currX-i>=0; i++) { 
				pieces.add(new Piece(currX-i, currY, "QueenW", true));// left
				left=i;
			}
		//------------------------------ Up--------------------------

		if(currY<=7) 
			for(int i=1 ; currY-i>=0; i++) { 
				pieces.add(new Piece(currX, currY-i, "QueenW", true));//up
				up=i;
			}
		//------------------------------ Down--------------------------

		if(currY>=0) 
			for(int i=1 ; currY+i<8; i++) { 
				pieces.add(new Piece(currX, currY+i, "QueenW", true));// down
				down=i;
			}
		
		//------------------------------ Down + Left--------------------------
		
		if(down>=left) 
			for(int i=1 ; left-i>=0 ; i++) 
				pieces.add(new Piece(currX-i, currY+i, "QueenW", true));//down + left		
			
		if(down<left) 
			for(int i=1 ; down-i>=0 ; i++) 
				pieces.add(new Piece(currX-i, currY+i, "QueenW", true));//down + left		
									
		//------------------------------ Down + Right--------------------------
		
		if(down>=right) 
		for(int i=1 ; right-i>=0 ; i++) 
			pieces.add(new Piece(currX+i, currY+i, "QueenW", true)); // down + right
		
		if(down<right) 
			for(int i=1 ; down-i>=0 ; i++)  
				pieces.add(new Piece(currX+i, currY+i, "QueenW", true)); // down + right
			
		//------------------------------ Up + Left--------------------------
		
		if(up>=left) 
			for(int i=1 ; left-i>=0 ; i++)  
				pieces.add(new Piece(currX-i, currY-i, "QueenW", true));//up + left
					
		if(up<left) 
			for(int i=1 ; up-i>=0 ; i++) 
				pieces.add(new Piece(currX-i, currY-i, "QueenW", true));//up + left
			
		
		//------------------------------ Up + Right--------------------------
		if(up>=right) 
			for(int i=1 ; right-i>=0 ; i++)  
				pieces.add(new Piece(currX+i, currY-i, "QueenW", true));//up + right
						
		if(up<right) 
			for(int i=1 ; up-i>=0 ; i++)  
				pieces.add(new Piece(currX+i, currY-i, "QueenW", true));//up + right			
		
		for (Piece p : pieces) {
		System.out.println(p.getname() + "move option: " + p.getX() + ","+p.getY());
	}
		return pieces;
	}
	
	
}
