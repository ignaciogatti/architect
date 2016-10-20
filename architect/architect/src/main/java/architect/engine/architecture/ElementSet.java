package architect.engine.architecture;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class ElementSet<T> {
	
    public static <T> Set<T> union(Set<T> a, Set<T> b) {
        Set<T> res = new TreeSet<T>(a);
        res.addAll(b);
        return res;
    }
    
    public static <T> Set<T> intersection(Set<T> a, Set<T> b) {
        Set<T> res = new TreeSet<T>(a);
        res.retainAll(b);
        return res;
    }
    
    public static <T> Set<T> intersection(Set<T> a, Set<T> b, Comparator<T> c) {
        Set<T> res = new TreeSet<T>(c);
        res.addAll(a);
        res.retainAll(b);
        return res;
    }
    
    public static <T> Set<T> difference(Set<T> a, Set<T> b) {
        Set<T> res = new TreeSet<T>(a);
        res.removeAll(b);
        return res;
    }
    
    public static <T> Set<T> symmetricDifference(Set<T> a, Set<T> b) {
        Set<T> res = difference(a,b);
        Set<T> bmenosa = difference(b,a);
        res.addAll(bmenosa);
        return res;
    }
    
    /** List order not maintained **/

	public static <T> void removeDuplicate(ArrayList<T> arlList) {
		LinkedHashSet<T> h = new LinkedHashSet<T>(arlList);
		arlList.clear();
		arlList.addAll(h);
	}
}
