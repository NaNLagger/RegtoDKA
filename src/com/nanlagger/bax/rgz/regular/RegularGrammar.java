package com.nanlagger.bax.rgz.regular;

import com.nanlagger.bax.rgz.nka.PointNka;
import com.nanlagger.bax.rgz.nka.TableNka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by NaNLagger on 02.12.14.
 *
 * @author Stepan Lyashenko
 */
public class RegularGrammar {

    public String notTString = "QWERTYUIOPASDFGHJKLZXCVBNM";
    String notTerminal = "";
    String terminal = "01";
    ArrayList<Rule> rules = new ArrayList<Rule>();
    ArrayList<String> generateStrings = new ArrayList<String>();

    public RegularGrammar() {
        /*addRule(addNotTerminal("S"), "1S,0S,1Q");
        addRule(addNotTerminal("Q"), "1R");
        addRule(addNotTerminal("R"), "#");
        addRule(addNotTerminal("F"), "#");*/
        addRule(addNotTerminal("S"), "aA,bA");
        addRule(addNotTerminal("A"), "d");
        addRule(addNotTerminal("F"), "#");
        //rules = changeGrammar();
    }

    public RegularGrammar(HashMap<String, String> nT, boolean right) {
        for (Map.Entry<String, String> entry : nT.entrySet()) {
            addRule(entry.getKey(), entry.getValue());
        }
        addRule(addNotTerminal("F"), "#");
        if (right)
            rules = changeGrammar();
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public ArrayList<Rule> changeGrammar() {
        ArrayList<Rule> nRules = new ArrayList<Rule>();
        for (Rule rule : rules) {
            nRules.add(new Rule(rule.name));
        }
        for (Rule rule : rules) {
            for (String s : rule.rules) {
                if (terminal.indexOf(s.charAt(0)) >= 0) {
                    Rule temp = getRule("F", nRules);
                    temp.addRule(s+rule.name);
                } else {
                    if (!s.equals("#")) {
                        Rule temp = getRule(String.valueOf(s.charAt(0)), nRules);
                        temp.addRule(s.substring(1)+rule.name);
                    } else {
                        Rule temp = getRule(rule.name, nRules);
                        temp.addRule("#");
                    }
                }
            }
        }
        System.out.println(toString(nRules));
        int k=0;
        boolean flagMerge;
        while (k<nRules.size()) {
            flagMerge = false;
            Rule temp = nRules.get(k);
            if (temp.name.equals("F")) {
                k++;
                continue;
            }
            for (String s : temp.rules) {
                if (s.equals("#")) {
                    mergeRule(getRule("F", nRules), temp);
                    flagMerge = true;
                    break;
                }
            }
            if (flagMerge) {
                nRules.remove(k);
            } else {
                k++;
            }
        }
        Rule start = getRule("F", nRules);
        Rule finish = getRule("S", nRules);
        finish.addRule("#");
        start.deteleRule("#");
        replaceRule(start, finish);
        return nRules;
    }

    protected void mergeRule(Rule main, Rule second) {
        for (String s : second.rules) {
            s = s.replace(second.name, main.name);
            if (!s.equals("#")) {
                main.addRule(s);
            }
        }
    }
    protected void replaceRule(Rule one, Rule two) {
        String sOne = one.name;
        String sTwo = two.name;
        for (int i=0; i<one.rules.size(); i++) {
            String s = one.getRule(i);
            if (s.contains(one.name)) {
                s = s.replace(one.name, two.name);
            } else {
                s = s.replace(two.name, one.name);
            }
            one.deleteRule(i);
            one.addRule(i,s);
        }
        for (int i=0; i<two.rules.size(); i++) {
            String s = two.getRule(i);
            if (s.contains(two.name)) {
                s = s.replace(two.name, one.name);
            } else {
                s = s.replace(one.name, two.name);
            }
            two.deleteRule(i);
            two.addRule(i,s);
        }
        one.name = sTwo;
        two.name = sOne;
    }

    protected Rule getRule(String name, ArrayList<Rule> nRules) {
        for (Rule rule : nRules) {
            if (rule.name.equals(name)) {
                return rule;
            }
        }
        return null;
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
            notTerminal+=s;
        }
        return s;
    }

    public void addRule(String name, String string) {
        String[] ruleStrings = string.split(",");
        Rule r = new Rule(name);
        for (String rule : ruleStrings) {
            r.addRule(rule);
        }
        rules.add(r);
    }

    public void generate() {
        int k=0;
        while (k < rules.size()) {
            Rule mainRule = rules.get(k);
            for (int i=0; i<mainRule.rules.size(); i++) {
                //System.out.println(i);
                String p = mainRule.getRule(i);
                if (p.length() > 1) {
                    if(terminal.indexOf(p.charAt(1)) >= 0) {
                        String nNT = addNewTerminal();
                        mainRule.deleteRule(i);
                        mainRule.addRule(i, p.substring(0,1)+nNT);
                        addRule(nNT, p.substring(1));
                    }
                } else {
                    if(terminal.indexOf(p.charAt(0)) >= 0) {
                        //String nNT = addNewTerminal();
                        mainRule.deleteRule(i);
                        mainRule.addRule(i, p.substring(0,1)+"F");
                        //addRule(nNT, p.substring(1));
                    }
                }
            }
            k++;
        }
    }



    public TableNka getTableNka() {
        TableNka tableNka = new TableNka();
        tableNka.setAbc(terminal);
        for(Rule rule : rules) {
            PointNka p = new PointNka(rule.name);
            for (int i=0; i<rule.rules.size(); i++) {
                String rString = rule.getRule(i);
                if (rString.equals("#")) {
                    p.flagFinish = true;
                } else {
                    p.addWay(String.valueOf(rString.charAt(0)), String.valueOf(rString.charAt(1)));
                }
            }
            tableNka.addPoint(p);
        }
        return tableNka;
    }

    @Override
    public String toString() {
        String q = "";
        for (Rule r : rules) {
            q += r.toString() + "\n";
        }
        return q;
    }

    protected String toString(ArrayList<Rule> nRules) {
        String q = "";
        for (Rule r : nRules) {
            q += r.toString() + "\n";
        }
        return q;
    }

    public String generateString(int start, int finish) {
        //System.out.println(start + " | " + finish);
        if (start > finish) {
            return "";
        }
        generateStrings = new ArrayList<String>();
        generateString("S", start, finish);
        String result = "";
        for(String s : generateStrings) {
            result += s + "\n";
        }
        return result;
    }

    protected void generateString(String str, int start, int finish) throws StackOverflowError{
        if (str.length() >= finish+2) {
            if (str.contains("#")) {
                if(!generateStrings.contains(str.substring(0, str.length()-1))) {
                    generateStrings.add(str.substring(0, str.length() - 1));
                }
                //System.out.println(str);
            }
            return;
        }
        String s = str.substring(str.length()-1);
        Rule tempRule = getRule(s, this.rules);
        if(tempRule != null) {
            for (String replacement : tempRule.rules) {
                generateString(str.replaceAll(s,replacement), start, finish);
            }
        } else {
            if (str.length() >= start+1 && str.length() <= finish+2) {
                if(!generateStrings.contains(str.substring(0, str.length()-1))) {
                    generateStrings.add(str.substring(0, str.length() - 1));
                }
                //System.out.println(str);
            }
        }
    }

    public static boolean checkWithRegExp(String userNameString){
        Pattern p = Pattern.compile("[A-Z]->(#|([^A-Z]*[A-Z]?)\\|)*(#|([^A-Z]*[A-Z]?))");
        Matcher m = p.matcher(userNameString);
        return m.matches();
    }

    public static boolean checkRightWithRegExp(String userNameString){
        Pattern p = Pattern.compile("[A-Z]->(#|([A-Z]?[^A-Z]*)\\|)*(#|([A-Z]?[^A-Z]*))");
        Matcher m = p.matcher(userNameString);
        return m.matches();
    }

    public static boolean checkAlfavit(String userNameString) {
        Pattern p = Pattern.compile("[^A-Z#\\|,]*");
        Matcher m = p.matcher(userNameString);
        return m.matches();
    }
}
