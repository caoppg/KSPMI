package halfback.cmpm.model.ssl;

import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.Itemset;

/**
 * Implementation of a two dimensional linked list's node having an Itemset.
 * 
 * @author <a href="mailto:carlos.miranda_lopez@insa-rouen.fr">Carlos Miranda</a>
 */
public class ItemsetNode {

    /**
     * The itemset encapsulated within this node.
     */
    private final Itemset _ITEMSET;

    /**
     * The index of the itemset in the sequence (not subsequence !).
     */
    private final Integer _INDEX;

    /**
     * _next is a link to the next node in the next layer, while _nextInLayer is a link to following
     * node within the same layer.
     */
    private ItemsetNode _next, _nextInLayer;

    /**
     * Whether this node is pruned or not.
     */
    private boolean _pruned;

    /**
     * Constructor. <br>
     * Links are initialized to null and _pruned to false.
     * 
     * @param itemset The itemset contained within this node.
     * @param index The position of the itemset in the sequence.
     */
    public ItemsetNode(Itemset itemset, Integer index) {
        _ITEMSET = itemset;
        _INDEX = index;
        _next = _nextInLayer = null;
        _pruned = false;
    }

    /**
     * Sets the next node within the layer for this node.
     * There is no check to see whether the itemsets correspond to the same layer. This check must
     * be done before calling this method.
     * 
     * @param nextInLayer The next node in the layer.
     */
    public void setNextInLayer(ItemsetNode nextInLayer) {
        _nextInLayer = nextInLayer;
    }

    /**
     * Sets the next node in the next layer.
     * There is no check to see whether the itemset corresponds to the next layer. This check must
     * be done before calling this method.
     * 
     * @param next The next node in the next layer.
     */
    public void setNext(ItemsetNode next) {
        _next = next;
    }

    /**
     * Returns the next node in the next layer.
     * 
     * @return The next node in the next layer.
     */
    public ItemsetNode getNext() {
        return _next;
    }

    /**
     * Return the next node in the same layer.
     * 
     * @return The next node in the same layer.
     */
    public ItemsetNode getNextInLayer() {
        return _nextInLayer;
    }

    /**
     * Sets this node as pruned.
     */
    public void prune() {
        _pruned = true;
    }

    /**
     * Returns whether this node is pruned.
     * 
     * @return Whether this node is pruned.
     */
    public boolean isPruned() {
        return _pruned;
    }

    /**
     * Returns the itemset contained within this node.
     * 
     * @return The itemset contained withing this node.
     */
    public Itemset getItemset() {
        return _ITEMSET;
    }

    /**
     * Returns whether this itemsetNode instance is equal to the other base on itemset and position.
     * 
     * @param o An object to check for equality.
     * 
     * @return Whether this itemsetNode instance is equal to the other.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (null == o || !getClass().equals(o.getClass())) return false;
        ItemsetNode other = (ItemsetNode) o;
        return other._ITEMSET.equals(_ITEMSET) && other._INDEX.equals(_INDEX);
    }
}