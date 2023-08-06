package config;

import java.util.ArrayList;

public class Bishop extends Piece{

	public Bishop(int x, int y, String name, Boolean isWhite) {
		super(x, y, name, isWhite);
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Piece>  Move() {
		int currX=this.getX();
		int currY=this.getY();
		int left = 0,right=0,up=0,down = 0;
		ArrayList<Piece> pieces = new ArrayList<>();
		
		//------------------------------count the Right--------------------------

		if(currX<7) 
			for(int i=1 ; currX+i<8; i++) { 
				right=i;
			}
		//------------------------------ count the Left--------------------------

		if(currX>0) 
			for(int i=1 ; currX-i>0; i++) { 
				left=i;
			}
		//------------------------------ count the Up--------------------------

		if(currY<7) 
			for(int i=1 ; currY-i>0; i++) { 
				up=i;
			}
		//------------------------------ count the Down--------------------------

		if(currY>0) 
			for(int i=1 ; currY-i<8; i++) { 
				down=i;
			}
		
		//------------------------------ Down + Left--------------------------
		
		if(down>=left) 
			for(int i=1 ; left-i>=0 ; i++) 
				pieces.add(new Piece(currX-i, currY+i, "BishopW", true));//down + left		
			
		if(down<left) 
			for(int i=1 ; down-i>=0 ; i++) 
				pieces.add(new Piece(currX-i, currY+i, "BishopW", true));//down + left		
									
		//------------------------------ Down + Right--------------------------
		
		if(down>=right) 
		for(int i=1 ; right-i>=0 ; i++) 
			pieces.add(new Piece(currX+i, currY+i, "BishopW", true)); // down + right
		
		if(down<right) 
			for(int i=1 ; down-i>=0 ; i++)  
				pieces.add(new Piece(currX+i, currY+i, "BishopW", true)); // down + right
			
		//------------------------------ Up + Left--------------------------
		
		if(up>=left) 
			for(int i=1 ; left-i>=0 ; i++)  
				pieces.add(new Piece(currX-i, currY-i, "BishopW", true));//up + left
					
		if(up<left) 
			for(int i=1 ; up-i>=0 ; i++) 
				pieces.add(new Piece(currX-i, currY-i, "BishopW", true));//up + left
			
		
		//------------------------------ Up + Right--------------------------
		if(up>=right) 
			for(int i=1 ; right-i>=0 ; i++)  
				pieces.add(new Piece(currX+i, currY-i, "BishopW", true));//up + right
						
		if(up<right) 
			for(int i=1 ; up-i>=0 ; i++)  
				pieces.add(new Piece(currX+i, currY-i, "BishopW", true));//up + right			
		
		
		return pieces;
	}
	
	
}
