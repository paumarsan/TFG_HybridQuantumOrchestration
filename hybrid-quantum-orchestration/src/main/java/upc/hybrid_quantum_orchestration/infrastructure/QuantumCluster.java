package upc.hybrid_quantum_orchestration.infrastructure;

import java.util.ArrayList;
import java.util.List;

public class QuantumCluster {
    List<QuantumRack> quantumRackList;
    
    public QuantumCluster() {
    	quantumRackList = new ArrayList<>(0);
    }
    
    public List<QuantumRack> getQuantumRackList() {
    	return this.quantumRackList;
    }
    
    public void addQuantumRack(QuantumRack quantumRack) {
    	this.quantumRackList.add(quantumRack);
    }
    
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append("\t");
    	sb.append("Quantum Cluster:\n");
    	
		for(QuantumRack qRack : quantumRackList) {
			sb.append(qRack.toString());
		}
       	return sb.toString();
    }
}
