package player;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.acl.Acl;
import java.util.LinkedList;
import javafx.scene.image.Image;



public class ChessPanel {
	public static void main(String[] args) {
		LinkedList<Piece> pieceL= new LinkedList<>();
		
		
		JFrame frame= new JFrame(); 
		frame.setBounds(10, 10, 600, 600);
		int squareSize = 64;
		JPanel panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                boolean White = true;
    
                for (int y = 0; y < 8; y++) {
                    for (int x = 0; x < 8; x++) {
                        if (White)
                            g.setColor(Color.WHITE.darker());
                       
                        else
                            g.setColor(Color.GREEN.darker());
    
                        g.fillRect(x * squareSize, y * squareSize, squareSize, squareSize);
                        White = !White; // Toggle the WHITE boolean for the next square in the same row
                    }
                    White = !White; // Toggle the WHITE boolean for the next row
                }
             /*   for (Piece piece : pieceL) {
                    try {
                        BufferedImage img = ImageIO.read(new File(piece.getImagePath()));
                        g.drawImage(img, piece.getX() * squareSize, piece.getY() * squareSize, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*/
               
            }
        };
        
		frame.add(panel);
		
		
	        

		frame.setDefaultCloseOperation(3);
		frame.setVisible(true);
	}


}
