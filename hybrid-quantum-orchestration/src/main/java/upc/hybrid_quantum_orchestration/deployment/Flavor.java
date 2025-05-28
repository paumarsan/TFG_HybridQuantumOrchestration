package upc.hybrid_quantum_orchestration.deployment;

public class Flavor{
	int id;
	String name;
	int vcpu;
	int ram;
	int disk;
	
	public Flavor(int id, String name, int vcpu, int ram, int disk) {
		this.id = id;
		this.name = name;
		this.vcpu = vcpu;
		this.ram = ram;
		this.disk = disk;
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getVcpu() {
		return this.vcpu;
	}
	
	public int getRAM() {
		return this.ram;
	}
	
	public int getDisk() {
		return this.disk;
	}
	
	/*public void setID(int id){
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setVcpu(int vcpu){
		this.vcpu = vcpu;
	}
	
	public void setRAM(int ram){
		this.ram = ram;
	}
	
	public void setDisk(int disk){
		this.disk = disk;
	}*/
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\nFlavor name: "+ name + "\n");
		sb.append("ID: "+ id + "\n");
		sb.append("VCPU's: "+ vcpu + "\n");
		sb.append("RAM: "+ ram + " MB \n");
		sb.append("Disk: "+ disk + " GB \n");
		
		return sb.toString();
	}


}