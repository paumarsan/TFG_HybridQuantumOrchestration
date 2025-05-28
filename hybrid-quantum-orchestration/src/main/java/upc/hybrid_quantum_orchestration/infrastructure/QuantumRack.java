package upc.hybrid_quantum_orchestration.infrastructure;

import java.util.ArrayList;
import java.util.List;

public class QuantumRack implements Comparable<QuantumRack>{
    List<QuantumServer> quantumServerList;
    
    public QuantumRack() {
    	quantumServerList = new ArrayList<>(0);
    }
    
    public List<QuantumServer> getQuantumServerList() {
    	return this.quantumServerList;
    }
    
    public void addQuantumServer(QuantumServer quantumServer) {
    	this.quantumServerList.add(quantumServer);
    }
    
    public double arithmeticMean() {
    	double mean = 0;
		for(QuantumServer qServer: quantumServerList) {
			mean = qServer.ratio() + mean;	
		}
		mean = mean/quantumServerList.size();
		
		return mean;
    }
    
	@Override
	public int compareTo(QuantumRack o) {
		if(arithmeticMean() > o.arithmeticMean()) {
			return -1;
		}
		if(arithmeticMean() < o.arithmeticMean()) {
			return 1;
		}
		else
			return 0;
	}
    
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append("\t\t");
    	sb.append("Quantum Rack:\n");
    	
		for(QuantumServer qServer : quantumServerList) {
			sb.append(qServer.toString());
		}
       	return sb.toString();
    }

	
}