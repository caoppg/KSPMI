package halfback.cmpm.model.ssl;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.Itemset;
import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.Sequence;

import halfback.cmpm.model.ssl.LinkedListItemset;
import halfback.cmpm.model.ssl.ItemsetNode;

/**
 * Implementation of a subsequence lattice, a data structure allowing to find all occurrences of
 * a subsequence within a sequence. <br>
 * It consists of a two dimensional linked list. Each layer represents an element of the subsequence,
 * and each element of each layer, an appearance of that element within the sequence.
 * This implementation supposes the subsequence actually occurs within the sequence, at least once.
 * 
 * @author <a href="mailto:carlos.miranda_lopez@insa-rouen.fr">Carlos Miranda</a>
 */
public class SubsequenceLattice implements Iterable<LinkedListItemset> {

    /**
     * The lattice, a two dimensional linked list of ItemsetNode.
     */
    private List<LinkedListItemset> _lattice;

    /**
     * The sequence to be matched.
     */
    private Sequence _sequence;

    /**
     * The subsequence to be searched.
     */
    private Sequence _subsequence;

    /**
     * Constructor.
     */
    public SubsequenceLattice() {
        _lattice = new ArrayList<LinkedListItemset>();
    }

    /**
     * Constructor.
     * 
     * @param sequence The sequence to be matched.
     * @param subsequence The subsequence to be searched.
     */
    public SubsequenceLattice(Sequence sequence, Sequence subsequence) {
        _lattice = new ArrayList<LinkedListItemset>(subsequence.size());
        for (int i = 0; i < subsequence.size(); ++i) {
            _lattice.add(new LinkedListItemset());
        }
        _sequence = sequence;
        _subsequence = subsequence;
    }

    /**
     * Builds the subsequence lattice from the sequence and subsequence given, without pruning
     * the appearances of elements that do not belong to an occurrence of the subsequence.
     */
    public void match() {
        Itemset currentItemset;
        int minQ = 0, maxQ = 1;
        for (int i = 0; i < _sequence.size(); ++i) {
            currentItemset = _sequence.get(i);
            for (int j = minQ; j < maxQ; ++j) {
                if (currentItemset.getItems().containsAll(_subsequence.get(j).getItems())) {
                    ItemsetNode insertedNode = addNode(j, currentItemset, i);
                    if (j > 0) {
                        linkLayer(j - 1, insertedNode);
                    }
                    if (maxQ < _subsequence.size()) {
                        maxQ++;
                    }
                }
            }
            if (i + _subsequence.size() + 1 > _sequence.size()) {
                minQ++;
            }
        }
    }

    /**
     * Adds a node to the given layer.
     * 
     * @param index The id of the layer.
     * @param itemset The element found.
     * @param position The position of the element found in the sequence.
     * 
     * @return The added ItemsetNode instance.
     */
    private ItemsetNode addNode(int index, Itemset itemset, int position) {
        ItemsetNode node = new ItemsetNode(itemset, position);
        _lattice.get(index).add(node);
        return node;
    }

    /**
     * Links all the nodes of the layer referenced by index, pointing to null, to the new 
     * inserted node of the next layer (index + 1).
     * 
     * @param index The id of the layer to be linked.
     * @param insertedNode The new inserted node from the next layer.
     */
    private void linkLayer(int index, ItemsetNode insertedNode) {
        ItemsetNode node = _lattice.get(index).getFirst();
        while (null != node) {
            if (null == node.getNext() && !node.equals(insertedNode)) {
                node.setNext(insertedNode);
            }
            node = node.getNextInLayer();
        }
    }

    /**
     * Marks nodes as pruned if they belong to an incomplete occurrence of the subsequence, i.e.
     * elements pruned do not form an occurrence of the searched subsequence. <br>
     * It starts on the penultimate layer (none of the last layer's elements are pruned) and goes up.
     */
    public void prune() {
        for (int i = _subsequence.size() - 2; i >= 0; --i) {
            ItemsetNode node = _lattice.get(i).getFirst();
            while (null != node) {
                // Mark as pruned if there is no next or next is marked as pruned.
                if (null == node.getNext() || node.getNext().isPruned()) {
                    node.prune();
                }
                node = node.getNextInLayer();
            }
        }
    }

    /**
     * Returns the ith linked list of the lattice (corresponding to the ith element of the subsequence).
     * 
     * @param i The id of the linked list to retrieve.
     * 
     * @return The ith linked list of the lattice.
     */
    public LinkedListItemset get(int i) {
        return _lattice.get(i);
    }

    /**
     * Allows for iteration over the horizontal linked lists.
     * 
     * @return A horizontal iterator.
     */
    public Iterator<LinkedListItemset> iterator() {
        return _lattice.iterator();
    }

    /**
     * Returns a string representation of this subsequence lattice.
     * 
     * @return A string representation of this subsequence lattice.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_sequence.toString()).append(" - ");
        sb.append(_subsequence.toString()).append(": \n");
        for (LinkedListItemset lli : _lattice) {
            sb.append(lli.toString()).append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }
}