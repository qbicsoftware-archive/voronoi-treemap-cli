package life.qbic.voronoi.util;

import life.qbic.voronoi.model.RowData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RowDataComparator implements Comparator<RowData> {

    public int compare(RowData c1, RowData c2) {
        List<String> l1 = new ArrayList<>(c1.getLevels());
        List<String> l2 = new ArrayList<>(c2.getLevels());

        return compare(l1, l2);
    }

    public int compare(List<String> l1, List<String> l2) {
        int i;
        if (l1.get(0).compareTo(l2.get(0)) == 0) {
            if (l1.size() > 1)
                i = compare(l1.subList(1, l1.size()), l2.subList(1, l2.size()));
            else
                i = l1.get(0).compareTo(l2.get(0));
        }
        else
            i = l1.get(0).compareTo(l2.get(0));
        return i;
    }

}
