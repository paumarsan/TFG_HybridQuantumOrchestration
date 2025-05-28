package upc.hybrid_quantum_orchestration.infrastructure;

import java.util.HashMap;
import upc.hybrid_quantum_orchestration.south_bound_interface.SBIQuantumAgent;

public class QuantumServer implements Comparable<QuantumServer>{
	String ip;
	int port;
	int qbits;

    int availableQbits;
    HashMap<Integer, Integer> appList;
    
    SBIQuantumAgent sbi = new SBIQuantumAgent();


    public QuantumServer(int qbits, String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.qbits = qbits;
        //vmList =  new ArrayList<>(0);
        
        availableQbits = qbits;
        appList = new HashMap<Integer,Integer>();

    }
    public String getIP() {
    	return this.ip;
    }
    
    public void setIP(String ip) {
    	this.ip = ip;
    }
    
    public int getQbits() {
    	return this.qbits;
    }
    
    public void setQbits(int qbits) {
    	this.qbits = qbits;
    }
    
    public int getPort() {
    	return this.port;
    }
    
    public void setPort(int port) {
    	this.port = port;
    }
    
    public int getAvailableQbits() {
    	return availableQbits;
    }
    
    public void setAvailableQbits(int availableQbits) {
    	this.availableQbits = availableQbits;
    }
    
    public HashMap<Integer, Integer> getAppList(){
    	return appList;
    }
    
    public void addApp(int appID, int qbits) {
    	appList.put(appID, qbits);
    	availableQbits = availableQbits - qbits;
    	sbi.postRequest(this.ip, this.port, qbits);
    	//sbi.getRequest(this.ip, this.port);
    }
    
    public void removeApp(int appID) {
    	int removedQbits = appList.remove(appID);
    	availableQbits = availableQbits + removedQbits;
    	sbi.deleteRequest(this.ip, this.port, removedQbits);
    }
    
    public double ratio() {
    	return availableQbits/qbits;
    }
    
	@Override
	public int compareTo(QuantumServer o) {
		if(ratio() > o.ratio())
			return  -1;
		else if(ratio() < o.ratio())
			return 1;
		else
			return 0;
	}
    

    
    public String toString() {
    	StringBuilder sb = new StringBuilder();
		sb.append("\t\t\t");
		sb.append("------ Quantum Server -----\n");
    	sb.append("\t\t\t");
    	sb.append("IP: " + ip + "\n");
    	sb.append("\t\t\t");
    	sb.append("Port: " + port + "\n");
    	sb.append("\t\t\t");
    	sb.append("Qubits: " + qbits + " MB\n");
    	
    	return sb.toString();
    }
    
}
