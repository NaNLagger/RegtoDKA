package com.nanlagger.bax.rgz;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ThemeDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTextArea вариант13ПоЗаданнойTextArea;

    public ThemeDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
    }

    private void onOK() {
// add your code here
        dispose();
    }

    public static void main(String[] args) {
        ThemeDialog dialog = new ThemeDialog();
        dialog.setSize(500,400);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
