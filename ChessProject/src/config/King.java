package config;

import java.util.ArrayList;

public class King extends Piece{

	public King(int x, int y, String name, Boolean isWhite) {
		super(x, y, name, isWhite);
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Piece>  Move() {
		int currX=this.getX();
		int currY=this.getY();
		ArrayList<Piece> pieces = new ArrayList<>();
		
		// going all the squares around the king
		for(int x = currX - 1; x <= currX + 1; x++) {
			for(int y = currY - 1; y <= currY + 1; y++) {
				if(x >= 0 && x <= 7 && y >= 0 && y <= 7) { // inside the board
					pieces.add(new Piece(x, y, "KingW", true));
				}
			}
		}
		
		/*
		if((currX>0 && currY<7) && (currX<7 && currY>0)  ) {//ORANGE in ipad - can move all sides
			pieces.add(new Piece(currX+1, currY+1, "KingW", true)); // down + right
			pieces.add(new Piece(currX, currY+1, "KingW", true));// down
			pieces.add(new Piece(currX+1, currY, "KingW", true)); // right
			pieces.add(new Piece(currX-1, currY-1, "KingW", true));//up + left
			pieces.add(new Piece(currX+1, currY-1, "KingW", true));//up + right
			pieces.add(new Piece(currX, currY-1, "KingW", true));//up 
			pieces.add(new Piece(currX-1, currY, "KingW", true));// left
			pieces.add(new Piece(currX-1, currY+1, "KingW", true));//down + left
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
		}*/
		/*for (Piece p : pieces) {
			System.out.println(p.getname() + "move option: " + p.getX() + ","+p.getY());
		}*/
		return pieces;
	}
	
	
}
