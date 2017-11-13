package abowman.util;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.procedure.*;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.math3.util.Combinations;
import scala.collection.immutable.ListSerializeEnd;

public class Util {

    @Context
    public GraphDatabaseService db;

    @UserFunction
    @Description("abowman.util.combinations(coll, minSelect, maxSelect:minSelect) - Returns collection of all combinations of list elements of selection size between minSelect and maxSelect (default:minSelect), inclusive")
    public List<List<Object>> combinations(@Name("coll") List<Object> coll, @Name(value="minSelect") long minSelectIn, @Name(value="maxSelect",defaultValue = "-1") long maxSelectIn) {
        int minSelect = (int) minSelectIn;
        int maxSelect = (int) maxSelectIn;
        maxSelect = maxSelect == -1 ? minSelect : maxSelect;

        if (coll == null || coll.isEmpty() || minSelect < 1 || minSelect > coll.size() || minSelect > maxSelect || maxSelect > coll.size()) {
            return Collections.emptyList();
        }

        List<List<Object>> combinations = new ArrayList<>();

        for (int i = minSelect; i <= maxSelect; i++) {
            Iterator<int[]> itr = new Combinations(coll.size(), i).iterator();

            while (itr.hasNext()) {
                List<Object> entry = new ArrayList<>(i);
                int[] indexes = itr.next();
                if (indexes.length > 0) {
                    for (int index : indexes) {
                        entry.add(coll.get(index));
                    }
                    combinations.add(entry);
                }
            }
        }

        return combinations;
    }



    @UserFunction
    @Description("abowman.util.hash([values]) | computes the hash of all values in the list (order insensitive)")
    public long hash(@Name("values") List<Object> values) {
        long hash = 0;
        for (Object obj : values) {
            hash += 31 * obj.hashCode();
        }

        return hash;
    }
}
