package kh.mort;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class Button extends JPanel {

    public JButton btn;
    public JTextArea about;
    private JPanel btnPanel;
    public int btnWidth;
    
    public Button(String about, Icon icon, int btnWidth, Dimension containerDim) {
        // Parent container layout
        this.setPreferredSize(containerDim);
        this.setLayout(new BorderLayout());

        // Button
        this.btnWidth = btnWidth;
        btn = new JButton(icon) {
            @Override
            public int getWidth() {
                return Button.this.btnWidth;
            }
        };
        this.btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.btnPanel.add(btn);

        // Caption
        this.about = new JTextArea(about);
        this.about.setLineWrap(true);
        this.about.setWrapStyleWord(true);
        this.about.setEditable(false);
        this.about.setOpaque(false);

        this.add(btnPanel, BorderLayout.CENTER);
        this.add(this.about, BorderLayout.SOUTH);
    }
}
