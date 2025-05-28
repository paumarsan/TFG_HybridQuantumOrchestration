package upc.hybrid_quantum_orchestration.south_bound_interface;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class SBIQuantumAgent {
	
	private Logger log = LoggerFactory.getLogger(SBIQuantumAgent.class);
	private RestTemplate restTemplate;
	
	public SBIQuantumAgent() {
		restTemplate = new RestTemplate();
	}
	
	public void postRequest (String ip, int port, int qbits)
	{
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json");
		HttpEntity<?> postEntity = new HttpEntity<>(qbits,header);

		String url = "http://" + ip + ":" + port + "/quantum-agent/add-application";
		
		ResponseEntity<String> httpResponse = restTemplate.exchange(url, HttpMethod.POST, postEntity, String.class);
		HttpStatusCode code = httpResponse.getStatusCode();
		//log.info("POST Request Code: " + String.valueOf(code) + "\t Added " + qbits+ " qubits");
	}
	
	public int getRequest (String ip, int port)
	{
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json");
		HttpEntity<?> postEntity = new HttpEntity<>(header);

		String url = "http://" + ip + ":" + port + "/quantum-agent/get-status";
		
		ResponseEntity<Integer> httpResponse = restTemplate.exchange(url, HttpMethod.GET, postEntity, Integer.class);
		HttpStatusCode code = httpResponse.getStatusCode();
		//log.info("GET Request Code: " + String.valueOf(code) + "\t " + httpResponse.getBody()+ " qubits available");

		
		return httpResponse.getBody();
	}
	
	public void deleteRequest (String ip, int port, int qbits)
	{
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "application/json");
		HttpEntity<?> postEntity = new HttpEntity<>(header);

		String url = "http://" + ip + ":" + port + "/quantum-agent/remove-application/" + qbits;
		
		ResponseEntity<String> httpResponse = restTemplate.exchange(url, HttpMethod.DELETE, postEntity, String.class);
		HttpStatusCode code = httpResponse.getStatusCode();
		//log.info("DELETE Request Code: " + String.valueOf(code) + "\t Removed " + qbits+ " qubits");

	}
	
}





