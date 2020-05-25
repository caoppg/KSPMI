package halfback.cmpm.model.tce;

import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.Itemset;

public class NumberedItemset extends Itemset {
	
	private final int _id;
	
	public NumberedItemset(int id) {
		super();
		_id = id;
	}
	
	public NumberedItemset(int id, Itemset itemset) {
		super(itemset);
		_id = id;
	}
	
	public int getId() {
		return _id;
	}
	
	public String toString() {
		return super.toString() + "_" + _id;
	}
	
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;
		if (o instanceof NumberedItemset) {
			return _id == ((NumberedItemset) o)._id && super.equals(o);
		} else {
			return super.equals(o);
		}
	}
}
