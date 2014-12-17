package com.nanlagger.bax.rgz.dka;

import java.util.ArrayList;

/**
 * Created by root on 22.10.14.
 */
public class TableDKA {
    protected ArrayList<Point> table = new ArrayList<Point>();
    protected String[] abc;

    public void setAbc(String[] abc) {
        this.abc = abc;
    }

    public void setAbc(String abcString) {
        char[] abcChar = abcString.toCharArray();
        this.abc = new String[abcChar.length];
        for (int i=0; i<abc.length; i++) {
            abc[i] = String.valueOf(abcChar[i]);
        }
    }

    public void addPoint(Point p) {
        table.add(p);
    }

    public Point getPoint(String name) {
        for (Point p : table) {
            if(p.name.equalsIgnoreCase(name))
                return p;
        }
        return null;
    }

    public Point getPoint(int i) {
        return table.get(i);
    }

    public int getSize() {
        return table.size();
    }

    public int getAbcSize() {
        return abc.length;
    }

    public String[] getAbc() {
        return abc;
    }

    public void removeAll() {
        table.removeAll(table);
    }

    public boolean searchTerminal(String s) {
        for (int i=0; i<abc.length; i++) {
            if (abc[i].equals(s)) {
                return true;
            }
        }
        return false;
    }

    public String checkStroke(String str) {
        String result = "";
        Point point = getPoint("S");
        //result += "(" + point.name + "," + str + ")\n";
        for (int i = 0; i < str.length(); i++) {
            char s = str.charAt(i);
            String temp = str.substring(i);
            result += "(" + point.name + "," + temp + ")\n";
            String next = point.ways.get(String.valueOf(s));
            point = getPoint(next);
            if (point == null) {
                result += "Нет перехода по данному символу!\n";
                return result;
            }
        }
        try {
            result += "(" + point.name + ", #)\n";
        } catch (Exception e) {

        }
        try {
            if(!point.flagFinish) {
                result += "Выхода нет!\n";
                return result;
            }
        } catch (Exception e) {

        }
        result += "Цепочка принята!";
        return result;
    }

    @Override
    public String toString() {
        String q = "";
        q += "({";
        for (Point point : table) {
            q+=point.name+",";
        }
        q = q.substring(0,q.length()-1);
        q += "},{";
        for (String s : abc) {
            q+=s+",";
        }
        q = q.substring(0,q.length()-1);
        q += "},d,S,{";
        for (Point point : table) {
            if (point.flagFinish) {
                q += point.name + ",";
            }
        }
        q = q.substring(0,q.length()-1);
        q += "});\n";
        q += "#";
        for (int i=0; i<abc.length; i++) {
            q+=" "+ abc[i];
        }
        q+="\n";
        for (Point point : table) {
            q+=point.name;
            for (int i=0; i<abc.length; i++) {
                String s;
                s = point.ways.get(abc[i]);
                if (s == null) {
                    q += " -";
                } else {
                    q += " "+s;
                }
            }
            q+="\n";
        }
        return q;
    }
}
