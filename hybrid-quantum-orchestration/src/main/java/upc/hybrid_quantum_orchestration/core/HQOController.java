package upc.hybrid_quantum_orchestration.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import upc.hybrid_quantum_orchestration.provisioning.ProvisioningService;
import upc.hybrid_quantum_orchestration.infrastructure.Datacenter;
import upc.hybrid_quantum_orchestration.deployment.Catalog;

//server.NBIProvisioningService;

public class HQOController {

	private Logger log = LoggerFactory.getLogger(HQOController.class);
	
	// Services
	@Autowired
	private ProvisioningService nbiProvisioningService;
	
	@Value("${simulated.infrastructure.file}")
	private String infFilePath;
	
	@Value("${openstack.infrastructure.file}")
	private String infFilePathOpen;
	
	@Value("${catalog.file}")
	private String catFilePath;
	
	@Value("${algorithm}")
	private int algorithm;
	
	@Value("${openstack.enabled}")
	private boolean openstackEnabled;
	
	@Value("${openstack.keystone.ip}")
	private String ip;
	
	@Value("${openstack.keystone.url}")
	private String url;
	
	@Value("${openstack.keystone.user}")
	private String user;
	
	@Value("${openstack.keystone.password}")
	private String password;
	
	@Value("${openstack.keystone.project}")
	private String project;
	
	@Value("${openstack.keystone.domain}")
	private String domain;
	 
	
	public HQOController ()
	{
		
	}
	
	public void initialize ()
	{
		log.info("Initializing Hybrid Quantum Orchestration Controller Services");
		Datacenter datacenter = new Datacenter();
		if(openstackEnabled == true)
			datacenter.initialize(infFilePathOpen);
		else
			datacenter.initialize(infFilePath);
		
		
		Catalog catalog = new Catalog();
		catalog.initialize(catFilePath);

		nbiProvisioningService.initService(datacenter, catalog, algorithm, openstackEnabled, ip, url, user, password, project, domain);
		
		
	}
}