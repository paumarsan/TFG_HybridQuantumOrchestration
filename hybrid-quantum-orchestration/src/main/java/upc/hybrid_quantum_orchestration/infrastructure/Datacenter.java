package upc.hybrid_quantum_orchestration.infrastructure;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Datacenter {
    List<Cluster> clusterList;
    List<QuantumCluster> quantumClusterList;
    
    public Datacenter() {}
    
    public void initialize(String infFilePath) {
    	clusterList = new ArrayList<>(0);
        quantumClusterList = new ArrayList<>(0);

        File file = new File(infFilePath);
        
        ObjectMapper objectMapper = new ObjectMapper();
        
        try {
			JsonNode rootNode = objectMapper.readTree(file);
			
			JsonNode datacenter = rootNode.get("Datacenter");
			JsonNode clusters = datacenter.get("clusterList");
			
			if(clusters != null) {
				Iterator<JsonNode> clustersIt = clusters.elements();
				while (clustersIt.hasNext()) {
					
					JsonNode clusterListNode = clustersIt.next();
					JsonNode cluster = clusterListNode.get("Cluster");
					
					
					JsonNode racks = cluster.get("rackList");
					//List<Rack> rackList = new ArrayList<Rack>(0);
					
					Cluster auxCluster = new Cluster();
					
					
					Iterator<JsonNode> racksIt = racks.elements();
					while (racksIt.hasNext()) {
						
						JsonNode rackListNode = racksIt.next();
						JsonNode rack = rackListNode.get("Rack");
						
						
						JsonNode servers = rack.get("serverList");
						List<Rack> rackList = new ArrayList<Rack>(0);
						
						Rack auxRack = new Rack();
						
						Iterator<JsonNode> serversIt = servers.elements();
						while(serversIt.hasNext()) {
							
							JsonNode serverListNode = serversIt.next();
							JsonNode server = serverListNode.get("Server");
							
							String id = server.get("id").asText();
							int cpu = server.get("cpu").asInt();
							int ram = server.get("ram").asInt();
							int storage = server.get("storage").asInt();
							
							Server auxServer = new Server(id, cpu, ram, storage);
							auxRack.addServer(auxServer);
						}
						//rackList.add(auxRack);
						auxCluster.addRack(auxRack);
					}
					addCluster(auxCluster);
				
				}
			}
			JsonNode qClusters = datacenter.get("qClusterList");
			
			if(qClusters != null) {
				Iterator<JsonNode> qClustersIt = qClusters.elements();
				while (qClustersIt.hasNext()) {
					
					JsonNode qClusterListNode = qClustersIt.next();
					JsonNode qCluster = qClusterListNode.get("QuantumCluster");
					
					
					JsonNode qRacks = qCluster.get("qRackList");
					List<QuantumRack> qRackList = new ArrayList<QuantumRack>(0);
					
					QuantumCluster auxQuantumCluster = new QuantumCluster();
					
					
					Iterator<JsonNode> qRacksIt = qRacks.elements();
					while (qRacksIt.hasNext()) {
						
						JsonNode qRackListNode = qRacksIt.next();
						JsonNode qRack = qRackListNode.get("QuantumRack");
						
						
						JsonNode qServers = qRack.get("qServerList");
						List<QuantumServer> qServerList = new ArrayList<QuantumServer>(0);
						
						QuantumRack auxQuantumRack = new QuantumRack();
						
						Iterator<JsonNode> qServersIt = qServers.elements();
						while(qServersIt.hasNext()) {
							
							JsonNode qServerListNode = qServersIt.next();
							JsonNode qServer = qServerListNode.get("QuantumServer");
							
							int qbits = qServer.get("qbits").asInt();
							String ip = qServer.get("ip").asText();
							int port = qServer.get("port").asInt();
							
							QuantumServer auxQuantumServer = new QuantumServer(qbits, ip, port);
							auxQuantumRack.addQuantumServer(auxQuantumServer);
							
						}
						auxQuantumCluster.addQuantumRack(auxQuantumRack);
						
					}
					addQuantumCluster(auxQuantumCluster);
				}
			}
			
		//System.out.println(toString());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public List<Cluster> getClusterList(){
    	return clusterList;
    }
    
    public void addCluster(Cluster cluster) {
    	this.clusterList.add(cluster);
    }
    
    public List<QuantumCluster> getQuantumClusterList(){
    	return quantumClusterList;
    }
    
    public void addQuantumCluster(QuantumCluster quantumCluster) {
    	this.quantumClusterList.add(quantumCluster);
    }
    
    
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	

    	sb.append("Datacenter:\n");
    	
		for(Cluster cluster : clusterList) {
			sb.append(cluster.toString());
		}
		for(QuantumCluster qCluster : quantumClusterList) {
			sb.append(qCluster.toString());
		}
       	return sb.toString();
    }
}
