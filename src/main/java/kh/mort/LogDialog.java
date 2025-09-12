package kh.mort;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LogDialog extends JDialog {

    JTextArea logArea;
    JButton closeBtn;

    public LogDialog(JFrame parent) {
        initComponents(parent);
        initListeners();
    }

    private void initComponents(JFrame parent) {
        logArea = new JTextArea();
        logArea.setWrapStyleWord(true);
        logArea.setEditable(false);
        closeBtn = new JButton("Close");
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.add(closeBtn);
        setLayout(new BorderLayout());
        add(new JScrollPane(logArea), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
        closeBtn.setEnabled(false);
        this.setSize(300, 300);
        this.setLocationRelativeTo(parent);
    }

    private void initListeners() {
        closeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                LogDialog.this.dispose();
            }
        });
    }

    public void addLine(String text) {
        logArea.append(text + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    public void finish() {
        closeBtn.setEnabled(true);
    }
}
