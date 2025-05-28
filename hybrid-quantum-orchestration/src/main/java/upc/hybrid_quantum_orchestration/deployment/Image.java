package upc.hybrid_quantum_orchestration.deployment;

public class Image{
	String id;
	String name;
	
	public Image(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getID() {
		return this.id;
	}
	
	public void setID(String id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	

	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\nImage name: "+ name + "\n");
		sb.append("ID: "+ id + "\n");
		
		return sb.toString();
	}
}