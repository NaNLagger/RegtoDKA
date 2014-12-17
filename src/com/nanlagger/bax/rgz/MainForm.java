package com.nanlagger.bax.rgz;

import com.nanlagger.bax.rgz.dka.TableDKA;
import com.nanlagger.bax.rgz.nka.TableNka;
import com.nanlagger.bax.rgz.regular.RegularGrammar;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by NaNLagger on 08.12.14.
 *
 * @author Stepan Lyashenko
 */
public class MainForm extends JFrame {
    private JButton themeButton;
    private JButton addButton;
    private JButton generateButton;
    private JTextArea textArea1;
    private JTable table1;
    private JButton generateStringButton;
    private JButton checkButton;
    private JTextField textField1;
    private JPanel mainPanel;
    private JPanel dinamicPanel;
    private JButton deleteButton;
    private JTextField textField2;
    private JCheckBox rightCheckBox;
    private JSpinner spinner1;
    private JSpinner spinner2;
    private JButton writeFileButton;
    private JButton readFileButton;

    private final JFileChooser fc = new JFileChooser();
    private ArrayList<JTextField> dinamicFields = new ArrayList<JTextField>();
    private TableDKA tableDKA;
    RegularGrammar regularGrammar;
    private final TableModel tableModel;
    BufferedWriter out;

    public MainForm() {
        super();
        try {
            out = new BufferedWriter(new FileWriter("output.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setContentPane(mainPanel);
        setSize(900, 400);
        setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                try {
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                System.exit(0);
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                addRuleField();
            }
        });
        writeFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    //out.write(tableDKA.toString());
                    out.write(textArea1.getText());
                    //out.close();
                } catch (IOException e) {

                }
            }
        });
        readFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                int returnVal = fc.showOpenDialog(MainForm.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    dinamicPanel.removeAll();
                    dinamicFields.removeAll(dinamicFields);
                    dinamicPanel.updateUI();
                    String filename = fc.getSelectedFile().getName();
                    try {
                        FileReader myfile = new FileReader(fc.getSelectedFile());
                        BufferedReader in = new BufferedReader(myfile);
                        String s = in.readLine();
                        if (s != null) {
                            textField2.setText(s);
                        }
                        while ((s = in.readLine()) != null) {
                            addRuleField().setText(s);
                        }
                    } catch (IOException e) {

                    }
                } else {
                }
            }
        });
        themeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ThemeDialog dialog = new ThemeDialog();
                dialog.setSize(400,300);
                dialog.setVisible(true);
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dinamicPanel.remove(dinamicFields.get(dinamicFields.size() - 1));
                dinamicFields.remove(dinamicFields.size()-1);
                dinamicPanel.updateUI();
            }
        });
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                textArea1.setText(tableDKA.checkStroke(textField1.getText()));
            }
        });

        generateStringButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                textArea1.setText(regularGrammar.generateString((Integer) spinner2.getValue(), (Integer) spinner1.getValue()));
            }
        });

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(textField2.getText().length() < 1) {
                    textArea1.setText("Задайте алфавит!");
                    return;
                }
                if (!RegularGrammar.checkAlfavit(textField2.getText())) {
                    textArea1.setText("Неверно задан алфавит!");
                    return;
                }
                if (dinamicFields.size() < 1) {
                    textArea1.setText("Задайте хотябы одно правило");
                    return;
                }
                HashMap<String, String> hashMap = new HashMap<String, String>();
                for (JTextField textField : dinamicFields) {
                    String parseString = textField.getText().replaceAll(" ","");
                    if(!rightCheckBox.isSelected()) {
                        if (!RegularGrammar.checkWithRegExp(parseString)) {
                            textArea1.setText("Неверный формат: " + parseString + "\nПример: S->aS|bS|dS");
                            return;
                        }
                    } else {
                        if (!RegularGrammar.checkRightWithRegExp(parseString)) {
                            textArea1.setText("Неверный формат: " + parseString + "\nПример: S->Sa|Sb|Sd");
                            return;
                        }
                    }
                    String checkA = parseString.replaceAll("->|\\||[A-Z]","");
                    for (int i = 0; i<checkA.length(); i++) {
                        if (!textField2.getText().contains(String.valueOf(checkA.charAt(i))) && !String.valueOf(checkA.charAt(i)).equals("#")) {
                            textArea1.setText("Нет символа '" + String.valueOf(checkA.charAt(i)) + "' в алфавите!");
                            return;
                        }
                    }
                    String nT[] = parseString.split("->|\\|");
                    String tempString = "";
                    for(int i=1; i<nT.length-1; i++) {
                        tempString += nT[i]+",";
                        //System.out.println(str);
                    }
                    tempString += nT[nT.length-1];

                    hashMap.put(nT[0],tempString);



                }
                if (hashMap.get("S") == null) {
                    textArea1.setText("Используйте в качестве целевого символа 'S'");
                    return;
                }

                regularGrammar = new RegularGrammar(hashMap, rightCheckBox.isSelected());
                regularGrammar.setTerminal(textField2.getText());
                regularGrammar.generate();
                System.out.println(regularGrammar.toString());
                TableNka tableNka = regularGrammar.getTableNka();
                tableNka.generate();
                tableDKA = tableNka;
                System.out.println(tableNka.toString());
                textArea1.setText(tableNka.toString());
                table1.setModel(tableModel);
                table1.updateUI();
            }
        });
        tableModel = new AbstractTableModel() {

            @Override
            public int getRowCount() {
                return tableDKA.getSize()+1;
            }

            @Override
            public int getColumnCount() {
                return tableDKA.getAbcSize()+1;
            }


            @Override
            public Object getValueAt(int i, int i2) {
                if (i==0 && i2 == 0) {
                    return "#";
                }
                if (i==0) {
                    return tableDKA.getAbc()[i2-1];
                }
                if (i2 == 0) {
                    return tableDKA.getPoint(i-1).name;
                }

                return tableDKA.getPoint(i-1).ways.get(tableDKA.getAbc()[i2-1]);
            }

        };


        dinamicPanel.setLayout(null);
    }

    public JTextField addRuleField() {
        JTextField tempField = new JTextField(30);
        tempField.setBounds(10, dinamicFields.size()*30, 200, 25);
        tempField.setVisible(true);
        dinamicFields.add(tempField);

        dinamicPanel.add(tempField);
        dinamicPanel.updateUI();
        return tempField;
    }
}
