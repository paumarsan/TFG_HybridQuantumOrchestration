package upc.hybrid_quantum_orchestration.deployment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Catalog{
	List<Flavor> flavorList;
	List<Image> imageList;
	
	public Catalog() {}
	
	public void initialize(String catFilePath) {
		flavorList = new ArrayList<Flavor>(0);
		imageList = new ArrayList<Image>(0);
		
		 File file = new File(catFilePath);
	     ObjectMapper objectMapper = new ObjectMapper();
	     
	     try {
			JsonNode rootNode = objectMapper.readTree(file);
			
			JsonNode flavors = rootNode.get("flavorList");
			
			Iterator<JsonNode> flavorsIt = flavors.elements();
			while(flavorsIt.hasNext()) {
				
				JsonNode flavorListNode = flavorsIt.next();
				JsonNode flavor = flavorListNode.get("Flavor");
				
				int id = flavor.get("id").asInt();
				String name = flavor.get("name").asText();
				int vcpu = flavor.get("vcpu").asInt();
				int ram = flavor.get("ram").asInt();
				int disk = flavor.get("disk").asInt();
				
				Flavor auxFlavor = new Flavor(id, name, vcpu, ram, disk);
				flavorList.add(auxFlavor);
			}
			/*for(Flavor flavorInList : flavorList) {
				System.out.println(flavorInList.toString());
			}*/
			
			JsonNode images = rootNode.get("imageList");
			
			Iterator<JsonNode> imagesIt = images.elements();
			while(imagesIt.hasNext()) {
				JsonNode imageListNode = imagesIt.next();
				JsonNode image = imageListNode.get("Image");
				
				String id = image.get("id").asText();
				String name = image.get("name").asText();
				
				Image auxImage = new Image(id, name);
				imageList.add(auxImage);
			}
			/*for(Image image : imageList) {
			System.out.println(image.toString());
			}*/
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Flavor> getFlavorList() {
		return flavorList;
	}
	
	public List<Image> getImageList(){
		return imageList;
	}
	
	public void addFlavor(Flavor flavor) {
		flavorList.add(flavor);
	}
	
	public void addImage(Image image) {
		imageList.add(image);
	}
}