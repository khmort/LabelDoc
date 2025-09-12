package kh.mort;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ClassCreatorDialog extends JDialog {

    public String name;
    public int id;
    public Color color;

    private JLabel nameLabel;
    private JTextField nameField;
    private JLabel idLabel;
    private JTextField idField;
    private JLabel colorLabel;
    private JColorChooser colorChooser;
    private JButton addBtn;
    private JButton cancelBtn;
    private JLabel errorLabel;

    public ClassCreatorDialog(JFrame f, String title, boolean arg2) {
        super(f, title, arg2);
        initComponents(f);
        initListeners();
    }

    private void initComponents(JFrame parent)
    {
        setTitle("Class creator");
        setLocationRelativeTo(parent);
        nameLabel = new JLabel("Name:");
        idLabel = new JLabel("ID:");
        colorLabel = new JLabel("Color:");
        nameField = new JTextField();
        idField = new JTextField();
        colorChooser = new JColorChooser();
        addBtn = new JButton("Add");
        cancelBtn = new JButton("Cancel");
        errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gb = new GridBagConstraints();
        gb.insets = new Insets(5, 5, 5, 5);

        // --- Row 0: name ---
        gb.gridx = 0;
        gb.gridy = 0;
        gb.anchor = GridBagConstraints.WEST;
        gb.gridwidth = 1;
        gb.fill = GridBagConstraints.NONE;
        formPanel.add(nameLabel, gb);

        gb.gridx = 1;
        gb.gridy = 0;
        gb.gridwidth = 2;
        gb.fill = GridBagConstraints.HORIZONTAL;
        gb.weightx = 1.0;
        formPanel.add(nameField, gb);

        // --- Row 1: id ---
        gb.gridx = 0;
        gb.gridy = 1;
        gb.gridwidth = 1;
        gb.fill = GridBagConstraints.NONE;
        gb.weightx = 0;
        formPanel.add(idLabel, gb);

        gb.gridx = 1;
        gb.gridy = 1;
        gb.gridwidth = 2;
        gb.fill = GridBagConstraints.HORIZONTAL;
        gb.weightx = 1.0;
        formPanel.add(idField, gb);

        // --- Row 2: color ---
        gb.gridx = 0;
        gb.gridy = 2;
        gb.gridwidth = 1;
        gb.fill = GridBagConstraints.NONE;
        gb.weightx = 0;
        formPanel.add(colorLabel, gb);

        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(addBtn);
        btnPanel.add(cancelBtn);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(formPanel);
        mainPanel.add(colorChooser);
        mainPanel.add(errorLabel);
        mainPanel.add(btnPanel);

        this.getContentPane().add(mainPanel);
    }

    private void initListeners() {
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (isFieldsValid()) {
                    name = nameField.getText();
                    id = Integer.parseInt(idField.getText());
                    color = colorChooser.getColor();
                    ClassCreatorDialog.this.dispose();
                }
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                ClassCreatorDialog.this.dispose();
            }
        });
    }

    public boolean isFieldsValid() {
        String name = nameField.getText();
        String id = idField.getText();
        if (name.isBlank()) {
            errorLabel.setText("-Name- is not valid!");
            return false;
        }
        try {
            Integer.parseInt(id);
        } catch (Exception exc) {
            errorLabel.setText("-ID- is not valid!");
            return false;
        }
        return true;
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            name = null;
            id = 0;
            color = null;
        }
        super.setVisible(b);
    }
}
