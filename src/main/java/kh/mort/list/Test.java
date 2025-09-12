package kh.mort.list;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JList;

public class Test {
    public static void main(String[] args) {

        ClassItem[] items = {
            new ClassItem("Paragraph", 1, Color.magenta),
            new ClassItem("Table", 2, Color.blue),
            new ClassItem("List", 3, Color.ORANGE)
        };
        JList<ClassItem> list = new JList<>(items);
        list.setCellRenderer(new ClassItemRenderer());

        JFrame frame = new JFrame();
        frame.getContentPane().add(list);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
