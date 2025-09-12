package kh.mort.canvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ListSelectionDialog extends JDialog {

    public JList<String> itemList;
    public String selectedValue;
    public boolean confirmed = false;

    public ListSelectionDialog(Frame parent, String title, String[] options) {
        super(parent, title, true); // Modal dialog
        setLayout(new BorderLayout());

        itemList = new JList<>(options);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemList.setSelectedIndex(0);
        add(new JScrollPane(itemList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("Ok");
        JButton cancelButton = new JButton("Cancel");

        ActionListener confirmAction = e -> {
            selectedValue = itemList.getSelectedValue();
            confirmed = true;
            dispose();
        };

        okButton.addActionListener(confirmAction);
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        itemList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    confirmAction.actionPerformed(null);
                }
            }
        });

        setSize(300, 200);
        setLocationRelativeTo(parent);
    }

    public String getSelectedValue() {
        return confirmed ? selectedValue : null;
    }
}
