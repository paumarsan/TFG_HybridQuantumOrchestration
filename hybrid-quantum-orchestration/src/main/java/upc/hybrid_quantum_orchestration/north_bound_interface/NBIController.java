package upc.hybrid_quantum_orchestration.north_bound_interface;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import upc.hybrid_quantum_orchestration.provisioning.ProvisioningService;
import upc.hybrid_quantum_orchestration.provisioning.Application;


@RestController
@RequestMapping("/hqo-controller/nbi")
public class NBIController {

	private static final Logger log = LoggerFactory.getLogger(NBIController.class);

	@Autowired
	private ProvisioningService provisioningService;
	
	public NBIController () {}
	
	@PostMapping(value = "/provisioning")
	public ResponseEntity<?> addApplication (@RequestBody Application app)
	{		
		String serverID = new String();
		try {
			serverID = provisioningService.processRequest(app);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(app.getServerID() != null)
			log.info("Application serviced at LEGACY server "+ app.getServerID() + " as "+ app.getFlavor().getName() +" with code "+ app.getID());
		else if(app.getQuantumServerID() != null) 
			log.info("Application serviced at QUANTUM server "+ app.getQuantumServerID() +" with "+ app.getQbits() +" qbits with code "+ app.getID());
		else 
			log.info("There are no available resources for the application");
		
		return new ResponseEntity<Integer>(app.getID(), HttpStatus.OK);
	}

	@DeleteMapping(value = "/provisioning/{id}")
	public ResponseEntity<?> removeApplication (@PathVariable("id") int id){
		int returnedID = provisioningService.removeApp(id);
		if (returnedID == id) {
			log.info("Application with code "+ id +" removed from the datacenter");
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else if (returnedID == 0) {
			log.info("Application was not found in the system");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		else {
			log.info("Critical issue: ID of app to remove doesn't match ID of app removed");
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}


	
}
