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
		/*if((currX>0 && currY<7) && (currX<7 && currY>0)  ) {//ORANGE in ipad - can move all sides
				for(int j=1 , i=1 ; (currY+j<8 || currX+i<8 ||currY-j>=0 ||currX-i>=0 ) ; j++, i++) {
			pieces.add(new Piece(currX+i, currY+j, "KingW", true)); // down + right
			pieces.add(new Piece(currX, currY+j, "KingW", true));// down
			pieces.add(new Piece(currX+i, currY, "KingW", true)); // right
			pieces.add(new Piece(currX-i, currY-j, "KingW", true));//up + left
			pieces.add(new Piece(currX+i, currY-j, "KingW", true));//up + right
			pieces.add(new Piece(currX, currY-j, "KingW", true));//up 
			pieces.add(new Piece(currX-i, currY, "KingW", true));// left
			pieces.add(new Piece(currX-i, currY+j, "KingW", true));//down + left
				}
		}		
		else if (currX==0 && currY>0 && currY<7){// PERPLE in ipad - on the Left side
			pieces.add(new Piece(currX, currY+1, "KingW", true));// down
			pieces.add(new Piece(currX+1, currY+1, "KingW", true)); // down + right
			pieces.add(new Piece(currX+1, currY, "KingW", true)); // right
			pieces.add(new Piece(currX+1, currY-1, "KingW", true));//up + right
			pieces.add(new Piece(currX, currY-1, "KingW", true));//up 
		}		
		else if(currX==7 && currY>0 && currY<7)// GREEN in ipad - on the right side
		{
			pieces.add(new Piece(currX, currY+1, "KingW", true));// down
			pieces.add(new Piece(currX-1, currY+1, "KingW", true));//down + left
			pieces.add(new Piece(currX-1, currY, "KingW", true));// left
			pieces.add(new Piece(currX, currY-1, "KingW", true));//up 
			pieces.add(new Piece(currX-1, currY-1, "KingW", true));//up + left
		}
		else if(currY==0 && currX>0 && currX<7)// BLUE-SKY in ipad - on the top side
		{
			pieces.add(new Piece(currX, currY+1, "KingW", true));// down
			pieces.add(new Piece(currX+1, currY, "KingW", true)); // right
			pieces.add(new Piece(currX+1, currY+1, "KingW", true)); // down + right
			pieces.add(new Piece(currX-1, currY+1, "KingW", true));//down + left
			pieces.add(new Piece(currX-1, currY, "KingW", true));// left
		}
		else if(currY==7 && currX>0 && currX<7) {// YELLOW in ipad - on the down side
			pieces.add(new Piece(currX, currY-1, "KingW", true));//up
			pieces.add(new Piece(currX+1, currY, "KingW", true)); // right
			pieces.add(new Piece(currX-1, currY, "KingW", true));// left
			pieces.add(new Piece(currX-1, currY-1, "KingW", true));//up + left
			pieces.add(new Piece(currX+1, currY-1, "KingW", true));//up + right
		}
		else if(currY==0 && currX==0){// up left CORNER
			pieces.add(new Piece(currX+1, currY, "KingW", true)); // right
			pieces.add(new Piece(currX, currY+1, "KingW", true));// down
			pieces.add(new Piece(currX+1, currY+1, "KingW", true)); // down + right
		}
		else if(currY==0 && currX==7){// up right CORNER
			pieces.add(new Piece(currX, currY+1, "KingW", true));// down
			pieces.add(new Piece(currX-1, currY+1, "KingW", true));//down + left
			pieces.add(new Piece(currX-1, currY, "KingW", true));// left
		}
		else if(currY==7 && currX==7){// down right CORNER
			pieces.add(new Piece(currX, currY-1, "KingW", true));//up
			pieces.add(new Piece(currX-1, currY, "KingW", true));// left
			pieces.add(new Piece(currX-1, currY-1, "KingW", true));//up + left
		}		
		else if(currY==7 && currX==0){// down left CORNER
			pieces.add(new Piece(currX, currY-1, "KingW", true));//up
			pieces.add(new Piece(currX+1, currY, "KingW", true)); // right
			pieces.add(new Piece(currX+1, currY-1, "KingW", true));//up + right
		}
		
		*/
		if(currX<7) {
			for(int i=1 ; currX+i<8; i++) { //go RIGHT
				pieces.add(new Piece(currX+i, currY, "QueenW", true)); // right
				right=i;
			}
		}
		if(currX>0) {
			for(int i=1 ; currX-i>0; i++) { //go LEFT
				pieces.add(new Piece(currX-i, currY, "QueenW", true));// left
				left=i;
			}
		}
		if(currY<7) {
			for(int i=1 ; currY-i>0; i++) { //go UP
				pieces.add(new Piece(currX, currY-i, "QueenW", true));//up
				up=i;
			}
		}
		if(currY>0) {
			for(int i=1 ; currY-i<8; i++) { //go DOWN
				pieces.add(new Piece(currX, currY+i, "KingW", true));// down
				down=i;
			}
		}
		if(down>=left) {
			for(int i=1 ; left-i>=0 ; i++) { //go DOWN + LEFT
				pieces.add(new Piece(currX-i, currY+i, "QueenW", true));//down + left		
			}
		}
		if(down<left) {
			for(int i=1 ; down-i>=0 ; i++) { //go DOWN + LEFT
				pieces.add(new Piece(currX-i, currY+i, "QueenW", true));//down + left		
			}
			
			
		}//------------------------------ continue from here--------------------------
		if(down<left) {
		for(int i=1 ; down-i>=0 ; i++)  //go DOWN + RIGHT
			pieces.add(new Piece(currX+i, currY+i, "QueenW", true)); // down + right

		
		}
		
		return pieces;
	}
	
	
}
