package halfback.cmpm.model.tce;

import ca.pfv.spmf.algorithms.sequentialpatterns.clasp_AGP.dataStructures.patterns.Pattern;

public class PatternTracePair {

    private final Pattern _pattern;
    private final TimeConstraintSet _trace;

    public PatternTracePair(Pattern pattern, TimeConstraintSet trace) {
        _pattern = pattern;
        _trace = trace;
    }

    public Pattern getPattern() {
        return _pattern;
    }

    public TimeConstraintSet getTrace() {
        return _trace;
    }
}