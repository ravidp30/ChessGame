package config;

import java.util.ArrayList;

import player.Piece;

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
				return piece;
			}
		}
		return null;
	}

}
