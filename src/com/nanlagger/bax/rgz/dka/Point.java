package com.nanlagger.bax.rgz.dka;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 22.10.14.
 */
public class Point {
    public String name;
    public HashMap<String, String> ways = new HashMap<String, String>();
    public boolean flagStart = false;
    public boolean flagFinish = false;

    public Point(String name) {
        this.name = name;
    }

    public Point(String name, String[] alpha, String[] points) {
        this(name);
        for (int i=0; i<alpha.length; i++) {
            ways.put(alpha[i],points[i]);
        }
    }

    public void addWay(String term, String point) {
        ways.put(term,point);
    }

    public boolean isWay(String name) {
        return ways.containsValue(name);
    }

    @Override
    public String toString() {
        String q = name + (flagFinish ? " (f)" : "") + " : ";
        for(Map.Entry<String, String> entry : ways.entrySet()) {
            q+= entry.toString() + " | ";
        }
        return q;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            return ((Point) obj).name.equals(this.name);
        } else {
            return false;
        }
    }
}
