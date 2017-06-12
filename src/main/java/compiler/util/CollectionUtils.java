package compiler.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * CollectionUtils class
 *
 * @author Maglethong Spirr
 */
public class CollectionUtils {

    @SafeVarargs
    public static <T> Collection<T> concat(Collection<T> col, T... ts) {
        Collection<T> ret = new ArrayList<>(col);
        ret.addAll(Arrays.asList(ts));
        return Collections.unmodifiableCollection(ret);
    }

    @SafeVarargs
    public static <T> Collection<T> concat(Collection<T> col, Collection<T>... cols) {
        Collection<T> ret = new ArrayList<>(col);
        for (Collection<T> ts : cols) {
            ret.addAll(ts);
        }
        return Collections.unmodifiableCollection(ret);
    }
}
