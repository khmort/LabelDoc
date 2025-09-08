package kh.mort.list;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import kh.mort.canvas.Box;

public class ObjectListItem extends JPanel {
    
    Box object;
    int cls;
    String clsText;

    int itemHeight;
    ObjectList container;
    Color itemColor;
    JButton deleteBtn;
    JTextField classField;
    JCheckBox isVisibleCheckBox;

    public ObjectListItem(ObjectList list, Box box, int cls, String clsText, int itemHeight, Color itemColor)
    {
        this.container = list;
        this.itemHeight = itemHeight;
        this.object = box;
        this.itemColor = itemColor;
        this.cls = cls;
        initComponents(clsText);
        initListeners();
    }

    private void initComponents(String clsText) 
    {
        deleteBtn = new JButton(new ImageIcon("labelDoc/src/main/resources/icons8-delete-20.png"));
        deleteBtn.setPreferredSize(new Dimension(30, 30));
        classField = new JTextField(clsText);
        classField.setEditable(false);
        classField.setBorder(null);
        isVisibleCheckBox = new JCheckBox();
        isVisibleCheckBox.setSelected(true);
        setItemColor(Color.WHITE, Color.BLACK);

        this.setLayout(new BorderLayout());
        this.add(isVisibleCheckBox, BorderLayout.WEST);
        this.add(classField, BorderLayout.CENTER);
        this.add(deleteBtn, BorderLayout.EAST);
    }

    private void initListeners()
    {
        isVisibleCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (isVisibleCheckBox.isSelected()) {
                    container.expose(object, cls);
                } else {
                    container.hide(object, cls);
                }
            }
        });

        deleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                container.del(object, cls);
                container.revalidate();
            }
        });

        classField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent arg0) {
                container.addToSelects(ObjectListItem.this);
                container.updateSelectsUI();
            }
            @Override
            public void focusLost(FocusEvent arg0) {
                container.removeFromSelects(ObjectListItem.this);
            }
        });

        classField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int idx = container.indexOf(ObjectListItem.this);
                switch (e.getKeyCode()) {
                    case 40:
                        idx = Math.min(container.getItemCount() - 1, idx + 1);
                        container.getItemComponent(idx).classField.requestFocus();
                        break;
                    case 38:
                        idx = Math.max(0, idx - 1);
                        container.getItemComponent(idx).classField.requestFocus();
                        break;
                    case 17:
                        container.multipleSelection = true;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 17) {
                    container.multipleSelection = false;
                }
            }
        });
    }

    public void setItemColor(Color bg, Color fg) {
        classField.setBackground(bg);
        classField.setForeground(fg);
        isVisibleCheckBox.setBackground(bg);
    }

    @Override
    public Dimension getPreferredSize()
    {
        Dimension dim = super.getPreferredSize();
        return new Dimension((int) dim.getWidth(), itemHeight);
    }

}
