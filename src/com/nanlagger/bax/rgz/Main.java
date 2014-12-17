package com.nanlagger.bax.rgz;

import com.nanlagger.bax.rgz.dka.Point;
import com.nanlagger.bax.rgz.dka.TableDKA;

public class Main {
    TableDKA tableDKA = new TableDKA();

    public static void main(String[] args) {
        MainForm mainForm = new MainForm();
        /*mainForm.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });*/
        /*RegularGrammar regularGrammar = new RegularGrammar();
        System.out.println(regularGrammar.toString());
        regularGrammar.generate();
        System.out.println(regularGrammar.toString());
        TableNka tableNka = regularGrammar.getTableNka();
        System.out.println(tableNka.toString());
        tableNka.generate();
        System.out.println(tableNka.toString());*/

        //(new PointNka("d")).deleteReplay("saadfsadfdasdsd");
        //System.out.println(checkWithRegExp("S->aS|bS|dS"));
    }



    public String checkStroke(String str) {
        String result = "";
        Point point = tableDKA.getPoint(0);
        //result += "(" + point.name + "," + str + ")\n";
        for (int i = 0; i < str.length(); i++) {
            char s = str.charAt(i);
            String temp = str.substring(i);
            result += "(" + point.name + "," + temp + ")\n";
            String next = point.ways.get(String.valueOf(s));
            point = tableDKA.getPoint(next);
            if (point == null) {
                result += "Нет перехода по данному символу!\n";
                break;
            }
        }
        try {
            result += "(" + point.name + ", #)\n";
        } catch (Exception e) {

        }
        try {
            if(!point.flagFinish)
                result += "Выхода нет!\n";
        } catch (Exception e) {

        }
        return result;
    }

    public void changeParameters(String abcString, String statesString, String finishString) {
        //tableDKA.removeAll();
        tableDKA = new TableDKA();
        String[] abc = abcString.split(",");
        tableDKA.setAbc(abc);
        String[] states = statesString.split(",");
        for(String name : states) {
            Point p = new Point(name);
            tableDKA.addPoint(p);
        }
    }
}
