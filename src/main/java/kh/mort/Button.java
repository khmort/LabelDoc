package kh.mort;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class Button extends JPanel {

    public AbstractButton btn;
    public JTextArea about;
    private JPanel btnPanel;
    public int btnWidth;
    
    public Button(String about, Icon icon, int btnWidth, Dimension containerDim, boolean isToggleButton) {
        // Parent container layout
        this.setPreferredSize(containerDim);
        this.setLayout(new BorderLayout());

        // Button
        this.btnWidth = btnWidth;
        if (isToggleButton) {
            btn = new JToggleButton(icon);
        } else {
            btn = new JButton(icon);
        }
        btn.setPreferredSize(new Dimension(btnWidth, btnWidth));
        this.btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.btnPanel.add(btn);

        // Caption
        this.about = new JTextArea(about);
        this.about.setLineWrap(true);
        this.about.setWrapStyleWord(true);
        this.about.setEditable(false);
        this.about.setOpaque(false);

        this.add(btnPanel, BorderLayout.NORTH);
        this.add(this.about, BorderLayout.CENTER);
        this.setBorder(BorderFactory.createDashedBorder(Color.BLUE));
    }
}
