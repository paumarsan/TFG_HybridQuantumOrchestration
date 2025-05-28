package upc.hybrid_quantum_orchestration.infrastructure;

import java.util.List;
import java.util.ArrayList;

public class Cluster {
    List<Rack> rackList;
    
    public Cluster() {
    	 rackList = new ArrayList<>(0);
    }
    
    public List<Rack> getRackList() {
    	return this.rackList;
    }
    
    public void addRack(Rack rack) {
    	this.rackList.add(rack);
    }
    
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append("\t");
    	sb.append("Legacy Cluster:\n");
    	
		for(Rack rack : rackList) {
			sb.append(rack.toString());
		}
       	return sb.toString();
    }
}
