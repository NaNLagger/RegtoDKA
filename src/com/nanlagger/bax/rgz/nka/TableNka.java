package com.nanlagger.bax.rgz.nka;

import com.nanlagger.bax.rgz.dka.Point;
import com.nanlagger.bax.rgz.dka.TableDKA;

import java.util.Map;

/**
 * Created by NaNLagger on 04.12.14.
 *
 * @author Stepan Lyashenko
 */
public class TableNka extends TableDKA {
    public String notTString = "QWERTYUIOPASDFGHJKLZXCVBNM";

    public void generate() {
        int k=0;
        while (k < table.size()) {
            PointNka pointNka = (PointNka) table.get(k);
            for (int i=0; i<abc.length; i++) {
                String namePoint;
                namePoint = pointNka.ways.get(abc[i]);
                if(namePoint != null && getPoint(namePoint) == null) {
                    PointNka nPoint = new PointNka(namePoint);
                    for (int j=0; j<namePoint.length(); j++) {
                        String s = String.valueOf(namePoint.charAt(j));
                        nPoint.addWay(getPoint(s));
                    }
                    addPoint(nPoint);
                }
            }
            k++;
        }

        while (!removeNotUsedState()) {

        }
        renamePoint();
    }

    public boolean removeNotUsedState() {
        int i=0;
        boolean flag = false;
        for (Point point : table) {
            if (point.name.equals("S")) {
                flag = true;
                continue;
            }
            flag = false;
            for (Point point1 : table) {
                if (point.equals(point1)) {
                    continue;
                }
                if (point1.isWay(point.name)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                table.remove(point);
                break;
            }
        }
        return flag;
    }

    public void renamePoint() {
        for (Point point : table) {
            if(point.name.length() == 1) {
                addNotTerminal(point.name);
            }
        }

        for (Point point : table) {
            if (point.name.length() > 1) {
                String oldName = point.name;
                String newName = addNewTerminal();
                point.name = newName;
                for (Point replace : table) {
                    for (Map.Entry<String, String> reValue : replace.ways.entrySet()) {
                        if(reValue.getValue().equals(oldName)) {
                            replace.ways.put(reValue.getKey(), newName);
                        }
                    }
                }
            }
        }
    }

    public String addNewTerminal() {
        //System.out.println(notTString.length());
        return addNotTerminal(String.valueOf(notTString.charAt(0)));
        //return String.valueOf(notTString.charAt(0));
    }

    public String addNotTerminal(String s) {
        if (s.length() == 1) {
            int index = notTString.indexOf(s);
            notTString = notTString.substring(0,index) + notTString.substring(index+1);
        }
        return s;
    }
}
