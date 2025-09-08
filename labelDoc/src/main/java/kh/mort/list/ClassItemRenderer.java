package kh.mort.list;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

public class ClassItemRenderer extends JPanel implements ListCellRenderer<ClassItem> {

    JTextField classField;
    JPanel colorViewer;
    Color color;
    Color bg;
    Color fg;

    public ClassItemRenderer()
    {
        classField = new JTextField();
        classField.setBorder(null);
        colorViewer = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.setColor(bg);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(color);
                g.fillRect(3, 3, this.getWidth() - 6, this.getHeight() - 6);
                g.setColor(Color.black);
                g.drawRect(3, 3, this.getWidth() - 6, this.getHeight() - 6);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(30, super.getPreferredSize().height);
            }
        };
        setLayout(new BorderLayout());
        add(classField, BorderLayout.CENTER);
        add(colorViewer, BorderLayout.EAST);
    }

    private void revalidate(String name, int id, Color c)
    {
        classField.setText(name + " (ID: " + id + ")");
        color = c;
    }

    private void setComponentsColor(Color bg, Color fg) {
        this.bg = bg;
        this.fg = fg;
        classField.setBackground(bg);
        classField.setForeground(fg);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends ClassItem> list, ClassItem value, int index,
            boolean isSelected, boolean cellHasFocus)
    {
        revalidate(value.name, value.id, value.color);
        if (isSelected) {
            setComponentsColor(Color.BLUE, Color.WHITE);
        } else {
            setComponentsColor(Color.WHITE, Color.BLACK);
        }
        return this;
    }

    
}
