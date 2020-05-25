package halfback.prototype.cmpm.controller;

import java.io.IOException;

import halfback.prototype.cmpm.model.Chronicle;

public class ChronicleTableEntry implements Comparable<ChronicleTableEntry> {

    private final Chronicle _chronicle;

    public ChronicleTableEntry(Chronicle chronicle) {
        _chronicle = chronicle;
    }

    public String toString() {
        return _chronicle.getPattern();
    }

    public int getSupport() throws IOException {
        return _chronicle.getSupport();	
    }

    public int getSize() {
        return _chronicle.size();
    }

    public double getCoverage() throws IOException {
    	return _chronicle.getCoverage();
    }
    
    public double getAccuracy() throws IOException {
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