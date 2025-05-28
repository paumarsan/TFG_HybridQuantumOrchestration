package upc.hybrid_quantum_orchestration.provisioning;

import com.fasterxml.jackson.annotation.JsonProperty;

import upc.hybrid_quantum_orchestration.deployment.Flavor;
import upc.hybrid_quantum_orchestration.deployment.Image;

public class Application {
    
    @JsonProperty("applicationType")
    String applicationType;
    @JsonProperty("cpu")
    int cpu;
    @JsonProperty("ram")
    int ram;
    @JsonProperty("storage")
    int storage;
    @JsonProperty("qbits")
    int qbits;
    
    int id;
    
    Flavor flavor;
    Image image;
    String quantumServerID, serverID, deployedVMID;
    

    public Application(String applicationType, int cpu, int ram, int storage, int qbits){
        this.applicationType = applicationType;
        this.cpu = cpu;
        this.ram = ram;
        this.storage = storage;
        this.qbits = qbits;
    }
    
    public int getCPU(){
        return this.cpu;
    }
    
    public void setCPU(int cpu) {
    	this.cpu = cpu;
    }
    
    public int getRAM(){
        return this.ram;
    }
    
    public void setRAM(int ram) {
    	this.ram = ram;
    }
    
    public int getStorage(){
        return this.storage;
    }
    
    public void  setStorage(int storage) {
    	this.storage = storage;
    }

    public String getApplicationType(){
        return this.applicationType;
    }
    
    public void setApplicationType(String applicationType) {
    	this.applicationType = applicationType;
    }
    
    public int getQbits() {
    	return this.qbits;
    }
    
    public void setQbits(int qbits) {
    	this.qbits = qbits;
    }
    
    public int getID() {
    	return id;
    }
    
    public void setID(int id) {
    	this.id = id;
    }
    
    public Flavor getFlavor() {
    	return flavor;
    }
    
    public void setFlavor(Flavor flavor) {
    	this.flavor = flavor;
    }
    
    public Image getImage() {
    	return image;
    }
    
    public void setImage(Image image) {
    	this.image = image;
    }
    
    public String getVMID() {
    	return deployedVMID;
    }
    
    public void setVMID(String vmID) {
    	this.deployedVMID = vmID;
    }
    
    public String getQuantumServerID() {
    	return this.quantumServerID;
    }
    
    public void setQuantumServerID(String id) {
    	this.quantumServerID = id;
    }
    
    public String getServerID() {
    	return this.serverID;
    }
    
    public void setServerID(String id) {
    	this.serverID = id;
    }
    
    
    
    
    
    
    
}
