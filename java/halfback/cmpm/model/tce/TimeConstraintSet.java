package halfback.cmpm.model.tce;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Collection;

import halfback.cmpm.model.tce.TimeConstraint;

/**
 * Implementation of a set of time constraints. <br>
 * The set corresponds to a multiset of time constraints between each couple of events.
 * 
 * @author <a href="mailto:carlos.miranda_lopez@insa-rouen.fr">Carlos Miranda</a>
 */
public class TimeConstraintSet {

    /**
     * A map corresponding to the constraints. <br>
     * As one may mine multiple time constraints for each couple of event types, the event type
     * from the key is just used to identify the couple of event type, while the value contains
     * the real time constraint.
     */
    private Map<TimeConstraint, TimeConstraint> _constraints;

    /**
     * Constructor.
     * @param size The initial capacity of the trace.
     */
    public TimeConstraintSet(int size) {
        _constraints = new LinkedHashMap<TimeConstraint, TimeConstraint>(size);
    }

    /**
     * Empty constructor.
     */
    public TimeConstraintSet() {
        _constraints = new LinkedHashMap<TimeConstraint, TimeConstraint>();
    }

    /**
     * Returns the set of keys of the map.
     * @return The set of keys of the map.
     */
    public Set<TimeConstraint> keySet() {
        return _constraints.keySet();
    }

    /**
     * Returns the set of values of the map.
     * @return The set of values of the  map.
     */
    public Collection<TimeConstraint> values() {
        return _constraints.values();
    }

    /**
     * Adds a time constraint. If the start and end event types already exist in the map key set,
     * then the corresponding constraint and the new constraints are merged. Else, the new time constraint
     * is just inserted.
     * @param tc A time constraint to be added
     */
    public synchronized void add(TimeConstraint tc) {
        TimeConstraint _tc = _constraints.get(tc);
        if (_tc != null) {
            _constraints.put(_tc, _tc.merge(tc));
        } else {
            _constraints.put(tc, tc);
        }
    }

    /**
     * Joins this trace set with another trace. <br>
     * This method is synchronized so as to not having concurrency issues.
     * @param trace Another trace
     */
    public synchronized void union(TimeConstraintSet trace) {
        for (TimeConstraint tc : trace._constraints.values()) {
            add(tc);
        }
    }

    /**
     * Returns the size of the trace, i.e. the number of (unique) time constraints it contains
     * @return The size of this trace
     */
    public int size() {
        return _constraints.size();
    }

    /**
     * Returns a string representation of this trace.
     * @return A string representation of this trace.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Trace to string :\n(");
        for (TimeConstraint tc : _constraints.values()) {
            sb.append(tc.toString()).append(", ");
        }
        sb.append(")");
        return sb.toString();
    }

}