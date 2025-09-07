package kh.mort.canvas;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Test {
    public static void main(String[] args) throws IOException {
        
        Canvas c = new Canvas();
        c.img = ImageIO.read(new File("/images/high/img-16.png"));

        JFrame frame = new JFrame();
        frame.getContentPane().add(c);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
