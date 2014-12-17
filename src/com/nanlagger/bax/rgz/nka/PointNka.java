package com.nanlagger.bax.rgz.nka;

import com.nanlagger.bax.rgz.dka.Point;

import java.util.Map;

/**
 * Created by NaNLagger on 04.12.14.
 *
 * @author Stepan Lyashenko
 */
public class PointNka extends Point {

    public PointNka(String name) {
        super(name);
    }

    @Override
    public void addWay(String term, String point) {
        if (ways.containsKey(term)) {
            String nValue = ways.get(term);
            nValue+=point;
            nValue = deleteReplay(nValue);
            ways.put(term,nValue);
        } else {
            ways.put(term,point);
        }
    }

    public String deleteReplay(String string) {
        String q = "";
        for (char s : string.toCharArray()) {
            if (!q.contains(String.valueOf(s))) {
                q+=String.valueOf(s);
            }
        }
        return q;
    }

    public void addWay(Point point) {
        if (point.flagFinish)
            flagFinish = true;
        for(Map.Entry<String, String> entry : point.ways.entrySet()) {
            addWay(entry.getKey(), entry.getValue());
        }
    }
}
