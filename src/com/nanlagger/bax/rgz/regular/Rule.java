package com.nanlagger.bax.rgz.regular;

import java.util.ArrayList;

/**
 * Created by NaNLagger on 02.12.14.
 *
 * @author Stepan Lyashenko
 */
public class Rule {
    String name;
    ArrayList<String> rules = new ArrayList<String>();

    public Rule(String name) {
        this.name = name;
    }

    public void addRule(String rule) {
        rules.add(rule);
    }

    public void addRule(int index, String rule) {
        rules.add(index, rule);
    }

    public String getRule(int index) {
        return rules.get(index);
    }

    public void deleteRule(int index) {
        rules.remove(index);
    }

    public void deteleRule(String s) {
        rules.remove(rules.indexOf(s));
    }

    @Override
    public String toString() {
        String q = name + " -> ";
        for (String s : rules) {
            q += s + " | ";
        }
        return q;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Rule) {
            return ((Rule) obj).name.equals(this.name);
        } else {
            return false;
        }
    }
}
