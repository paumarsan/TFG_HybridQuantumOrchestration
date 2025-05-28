package upc.hybrid_quantum_orchestration.provisioning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;

import org.openstack4j.api.Builders;
import org.openstack4j.model.compute.ServerCreate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import upc.hybrid_quantum_orchestration.deployment.*;
import upc.hybrid_quantum_orchestration.infrastructure.*;
import upc.hybrid_quantum_orchestration.north_bound_interface.NBIController;
import upc.hybrid_quantum_orchestration.south_bound_interface.OpenStackClient;

@Service
public class ProvisioningService {

	private static final Logger log = LoggerFactory.getLogger(ProvisioningService.class);
	
	Datacenter datacenter;
	Catalog catalog;
	String ip, url, user, password, project, domain;
	OpenStackClient openstack;
	boolean openstackEnabled;
	int algorithm;
	String[] quantumAppTypes = new String[4];
	
	int appCounter;
	HashMap<Integer,Server> serverHashMap;
	HashMap<Integer,QuantumServer> qServerHashMap;
	List<Server> serverList;
	List<Rack> rackList;
	List<QuantumServer> qServerList;
	List<QuantumRack> qRackList;
	
	
	
	public ProvisioningService() {
		appCounter = 0;
		openstack = new OpenStackClient();
		quantumAppTypes = new String[]{"machine learning","optimization","simulation","cryptography"};
	}
	
	public void initService (Datacenter datacenter, Catalog catalog, int algorithm, boolean openstackEnabled, 
			String ip, String url, String user, String password, String project, String domain)
	{
		this.datacenter = datacenter;
		this.catalog = catalog;
		
		this.algorithm = algorithm;
		this.openstackEnabled = openstackEnabled;
		this.ip = ip; 
		this.url = url;
		this.user = user;
		this.password = password;
		this.project = project;
		this.domain = domain;
		
		if(openstackEnabled == true)
			this.openstack.initialize(this.openstackEnabled, this.ip, this.url, this.user, this.password, this.project, this.domain);
		
		serverHashMap = new HashMap<Integer, Server>();
		qServerHashMap = new HashMap<Integer, QuantumServer>();
		
		//Create lists of servers and racks, unsorted
		rackList = new ArrayList<Rack>();		
		serverList = new ArrayList<Server>();
		for(Cluster cluster: datacenter.getClusterList()) {
			for(Rack rack: cluster.getRackList()) {
				rackList.add(rack);
				for(Server server: rack.getServerList()) {
					serverList.add(server);
				}
			}
		}
		
		
		qRackList = new ArrayList<QuantumRack>();
		qServerList = new ArrayList<QuantumServer>();
		for(QuantumCluster qCluster: datacenter.getQuantumClusterList()) {
			for(QuantumRack qRack: qCluster.getQuantumRackList()) {
				qRackList.add(qRack);
				for(QuantumServer server: qRack.getQuantumServerList()) {
					qServerList.add(server);
				}
			}
		}
			
	}
	
	public String processRequest(Application app) throws Exception{
		String serverID = new String();
		boolean isQuantum = false;
		
		for(int i = 0; i<quantumAppTypes.length; i++) {
			if(app.getApplicationType().equals(quantumAppTypes[i])) {
				isQuantum = true;
			}
			else if(app.getQbits() > 10) {
				isQuantum = true;
			}
		}
		
		
		if(isQuantum == true) {
			if(algorithm == 1)
				serverID = quantumServerFirstFitAlgorithm(app, qServerList);
			else if(algorithm == 2)
				serverID = quantumServerLoadBalancingAlgorithm(app, qServerList);
			
			else if(algorithm == 3 || algorithm == 4)
				serverID = quantumRackBalancingAlgorithm(app, qRackList);	
			
			if(serverID == null) {
				isQuantum = false;
				log.info("Attempted to service application as QUANTUM but there's no space for it, trying to service as LEGACY");
			}
			
		}
		if(isQuantum == false) {
			vmForApp(app);
			if(algorithm == 1)
				serverID = serverFirstFitAlgorithm(app, serverList);
			else if(algorithm == 2)
				serverID = serverLoadBalancingAlgorithm(app, serverList);
			else if(algorithm == 3 || algorithm == 4)
				serverID = rackBalancingAlgorithm(app, rackList);
		}

        return serverID;
    }
	
	//Provided resources decision for incoming application requests
	public void vmForApp(Application app) {
		for(Flavor flavor: catalog.getFlavorList()) {
			if(flavor.getVcpu() >= app.getCPU() & 
					flavor.getRAM() >= app.getRAM() & 
					flavor.getDisk() >= app.getStorage()) {
				app.setFlavor(flavor);
				app.setImage(catalog.getImageList().getFirst());
				break;
			}
		}	
	}
	
	//Provisioning algorithm that aims to fill server after server
	public String serverFirstFitAlgorithm(Application app, List<Server> servers) {
		for(Server server: servers) {
			if(server.getAvailableCPU() >= app.getFlavor().getVcpu() & 
					server.getAvailableRAM() >= app.getFlavor().getRAM() & 
					server.getAvailableStorage() >= app.getFlavor().getDisk()) {
				
				int id = generateID();
				app.setID(id);
				app.setServerID(server.getID());
				server.addApp(app, openstack);
				serverHashMap.put(id, server);
				
				
				return app.getServerID();
			}
		}
		return null;
	}
	
	//Provisioning algorithm that spreads out the load as much as possible
	public String serverLoadBalancingAlgorithm(Application app, List<Server> servers) {
		
		Collections.sort(servers);
		
		for(Server server: servers) {
			if(server.getAvailableCPU() >= app.getFlavor().getVcpu() & 
					server.getAvailableRAM() >= app.getFlavor().getRAM() & 
					server.getAvailableStorage() >= app.getFlavor().getDisk()) {
				
				int id = generateID();
				app.setID(id);
				app.setServerID(server.getID());
				server.addApp(app, openstack);
				serverHashMap.put(id, server);
				

				
				return app.getServerID();
			}
		}
		return null;
	}
	
	public String rackBalancingAlgorithm(Application app, List<Rack> racks) {
		Collections.sort(racks);
		
		String id = new String();
		for(Rack rack: racks) {
			if(algorithm == 3) 
				id = serverFirstFitAlgorithm(app, rack.getServerList());
			else if(algorithm == 4)
				id = serverLoadBalancingAlgorithm(app, rack.getServerList());
			
			if(app.getID() != 0)
				return id;
		}
		return id;
	}
	
	public int removeApp(int id) {
		
		Server server = serverHashMap.get(id);
		QuantumServer qServer = qServerHashMap.get(id);
		if(server != null) {
			Application removedApp = server.removeApp(id, openstack);
			serverHashMap.remove(id);
			return removedApp.getID();
		}
		else if(qServer != null) {
			qServer.removeApp(id);
			qServerHashMap.remove(id);
			return id;
		}
		else
			return 0;
	}

	//Provisioning algorithm that aims to fill quantum type servers one after another
	public String quantumServerFirstFitAlgorithm(Application app, List<QuantumServer> qServers) {
		for(QuantumServer qServer: qServers) {
			if(qServer.getAvailableQbits() >= app.getQbits()) {
				int id = generateID();
				app.setID(id);
				app.setQuantumServerID(qServer.getIP() + ":" + qServer.getPort());
				qServer.addApp(id, app.getQbits());
				qServerHashMap.put(id, qServer);
				return app.getQuantumServerID();
			}
		}
		return null;
	}
	
	//Provisioning algorithm for quantum servers that spreads out the load as much as possible
	public String quantumServerLoadBalancingAlgorithm(Application app, List<QuantumServer> qServers) {
		
		Collections.sort(qServers);
		
		for(QuantumServer qServer: qServers) {
			if(qServer.getAvailableQbits() >= app.getQbits()) {
				int id = generateID();
				app.setID(id);
				app.setQuantumServerID(qServer.getIP() + ":" + qServer.getPort());
				qServer.addApp(id, app.getQbits());
				qServerHashMap.put(id, qServer);
				return qServer.getIP() + ":" + qServer.getPort();
			}
		}
		return null;
	}
	
	public String quantumRackBalancingAlgorithm(Application app, List<QuantumRack> qRacks) {
		Collections.sort(qRacks);
		
		String id = new String();
		for(QuantumRack qRack: qRacks) {
			if(algorithm == 3)
				id = quantumServerFirstFitAlgorithm(app, qRack.getQuantumServerList());
			else if(algorithm == 4)
				id = quantumServerLoadBalancingAlgorithm(app, qRack.getQuantumServerList());

			if(id != null) 
				return id;
		}
		
		return id;
	}
	

	
	public int generateID() {
		appCounter++;
        return appCounter;
	}
	
	//Sort servers from lower to higher load DEPRECATED
	public void sortServers() {
		//Collections.sort(serverSortList);
		
		StringBuilder sb = new StringBuilder();
		for(Server server: serverList) {
			sb.append(server.getID() + "\n");
			sb.append(server.geometricMean() + "\n");
		}
		System.out.println(sb);
		
	}
		
}
