package upc.hybrid_quantum_orchestration.infrastructure;

import java.util.HashMap;

import upc.hybrid_quantum_orchestration.provisioning.Application;
import upc.hybrid_quantum_orchestration.south_bound_interface.OpenStackClient;

public class Server implements Comparable<Server>{
    int cpu;
    int ram;
    int storage;
    HashMap<Integer,Application> appList;

    int availableCPU;
    int availableRAM;
    int availableStorage;
    
    String id;

    public Server(String id, int cpu, int ram, int storage){
        this.cpu = cpu;
        this.ram = ram;
        this.storage = storage;
        appList = new HashMap<Integer,Application>();
    
        availableCPU = cpu;
        availableRAM = ram;
        availableStorage = storage;
        
        this.id = id;
        
    }
    
    public int getCPU() {
    	return cpu;
    }
    
    public int getRAM() {
    	return ram;
    }
    
    public int getStorage() {
    	return storage;
    }
    
    public int getAvailableCPU() {
    	return availableCPU;
    }
    
    public int getAvailableRAM() {
    	return availableRAM;
    }
    
    public int getAvailableStorage() {
    	return availableStorage;
    }
    
    public String getID() {
    	return id;
    }
    
    public void setCPU(int cpu) {
    	this.cpu = cpu;
    }
    
    public void setRAM(int ram) {
    	this.ram = ram;
    }
    
    public void setStorage(int storage) {
    	this.storage = storage;
    }
    
    public void setAvailableCPU(int cpu) {
    	this.availableCPU = cpu;
    }
    
    public void setAvailableRAM(int cpu) {
    	this.availableRAM = ram;
    }
    
    public void setAvailableStorage(int cpu) {
    	this.availableStorage = storage;
    }
    
    public void setID(String id) {
    	this.id = id;
    }
    
    public HashMap<Integer,Application> getAppHashMap() {
    	return this.appList;
    }
    
    public void addApp(Application app, OpenStackClient openstack) {
    	appList.put(app.getID(), app);
    	availableCPU = availableCPU - app.getFlavor().getVcpu();
       	availableRAM = availableRAM - app.getFlavor().getRAM();
       	availableStorage = availableStorage - app.getFlavor().getDisk();
       	
       	if(openstack.isEnabled() == true) {
       		String vmID = openstack.deployVM("instance" + app.getID(), app.getFlavor().getID(), app.getImage().getID(), id);
       		app.setVMID(vmID);
       	}
    }
    
    public Application removeApp(int id, OpenStackClient openstack) {
    	Application app = appList.remove(id);
    	if(app != null) {
			availableCPU = availableCPU + app.getFlavor().getVcpu();
			availableRAM = availableRAM + app.getFlavor().getRAM();
	       	availableStorage = availableStorage + app.getFlavor().getDisk();
	       	
	       	if(openstack.isEnabled() == true) {
	       		openstack.removeVM(app.getVMID());	
	       	}
    	       	return app;
		}
    	return null;
    }
    
  //Geometric mean of the server load by cpu, ram and storage
  	public double geometricMean() {
  		float cpu = (float) availableCPU/this.cpu;
  		float ram = (float) availableRAM/this.ram;
  		float storage = (float) availableStorage/this.storage;
  		
  		if(Math.signum(cpu) == 0 || Math.signum(ram) == 0 || Math.signum(storage) == 0) {
  			return 0;
  		}
  		
  		return Math.exp( (Math.log(cpu) + Math.log(ram) + Math.log(storage))/3 );
  	}

	@Override
	public int compareTo(Server o) {
		if(geometricMean() > o.geometricMean())
			return -1;
		else if(geometricMean() < o.geometricMean())
			return 1;
		else
			return 0;
	}

    public String toString() {
    	StringBuilder sb = new StringBuilder();
		sb.append("\t\t\t");
		sb.append("------ Legacy Server -----\n");
    	sb.append("\t\t\t");
    	sb.append("ID: " + id + "\n");
    	sb.append("\t\t\t");
    	sb.append("CPU's: " + cpu + " cores\n");
    	sb.append("\t\t\t");
    	sb.append("RAM: " + ram + " MB\n");
    	sb.append("\t\t\t");
    	sb.append("Storage: " + storage + " GB\n");
    	
    	return sb.toString();
    }

}


