package upc.hybrid_quantum_orchestration.infrastructure;

import java.util.List;
import java.util.ArrayList;

public class Rack implements Comparable<Rack> {
    List<Server> serverList;
    
    public Rack() {
    	 serverList = new ArrayList<>(0);
    }
    
    public List<Server> getServerList() {
    	return this.serverList;
    }
    
    public void addServer(Server server) {
    	this.serverList.add(server);
    }
    

    public double arithmeticMean() {
    	double mean = 0;
		for(Server server: serverList) {
			mean = server.geometricMean() + mean;	
		}
		mean = mean/serverList.size();
		
		return mean;
    }
    
	@Override
	public int compareTo(Rack o) {
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
    	sb.append("Legacy Rack:\n");
    	
		for(Server server : serverList) {
			sb.append(server.toString());
		}
       	return sb.toString();
    }

}

