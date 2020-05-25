package halfback.cmpm.controller;

import halfback.cmpm.model.Chronicle;

public class ChronicleTableEntry implements Comparable<ChronicleTableEntry> {

    private final Chronicle _chronicle;

    public ChronicleTableEntry(Chronicle chronicle) {
        _chronicle = chronicle;
    }

    public String toString() {
        return _chronicle.getPattern();
    }

    public int getSupport() {
        return _chronicle.getSupport();	
    }

    public int getSize() {
        return _chronicle.size();
    }

    public double getCoverage() {
    	return _chronicle.getCoverage();
    }
    
    public double getAccuracy() {
    	return _chronicle.getAccuracy();
    }

    public Chronicle getChronicle() {
        return _chronicle;
    }

	@Override
	public int compareTo(ChronicleTableEntry o) {
		return _chronicle.size() - o._chronicle.size();
	}

}