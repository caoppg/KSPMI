package halfback.cmpm.model.ssl;

import halfback.cmpm.model.ssl.ItemsetNode;

/**
 * Implementation of a linked list of ItemsetNodes. It stores a link to the beginning of the list
 * to allow iteration and a link to the end of the list to allow appending.
 * 
 * @author <a href="mailto:carlos.miranda_lopez@insa-rouen.fr">Carlos Miranda</a>
 */
public class LinkedListItemset {

    /**
     * Links to the first and last nodes in the list.
     */
    private ItemsetNode _first, _last;

    /**
     * Constructor. <br>
     * Both links are started to null, denoting an empty list.
     */
    public LinkedListItemset() {
        _first = _last = null;
    }

    /**
     * Constructor from a collection of ItemsetNode.
     * 
     * @param itemsetNodes A collection of ItemsetNode
     */
    public LinkedListItemset(ItemsetNode ...itemsetNodes) {
        _first = itemsetNodes[0];
        if (itemsetNodes.length > 1) {
            ItemsetNode previous = _first;
            for (int i = 1; i < itemsetNodes.length; ++i) {
                previous.setNextInLayer(itemsetNodes[i]);
                previous = previous.getNextInLayer();
            }
            _last = itemsetNodes[itemsetNodes.length - 1];
        } else {
            _last = _first;
        }
    }

    /**
     * Adds a node to the list.
     * 
     * @param node The node to be added.
     */
    public void add(ItemsetNode node) {
        if (null == _first) {
            _first = node;
            _last = node;
        } else {
            ItemsetNode previous = _last;
            previous.setNextInLayer(node);
            _last = node;
        }
    }

    /**
     * Returns the first node of this list.
     * 
     * @return The first node of this list.
     */
    public ItemsetNode getFirst() {
        return _first;
    }

    /**
     * Returns the last node of this list.
     * 
     * @return The last node of this list.
     */
    public ItemsetNode getLast() {
        return _last;
    }

    /**
     * Returns a string representation of this linked list.
     * 
     * @return A string representation of this linked list.
     */
    @Override
    public String toString() {
        ItemsetNode node = _first;
        StringBuilder sb = new StringBuilder();
        while (null != node) {
            sb.append(node.getItemset().toString());
            if (node.isPruned()) {
                sb.append("p");
            }
            if (node.getNext() == null) {
                sb.append("n");
            }
            sb.append(" -> ");
            node = node.getNextInLayer();
        }
        sb.append("null");
        return sb.toString();
    }
}