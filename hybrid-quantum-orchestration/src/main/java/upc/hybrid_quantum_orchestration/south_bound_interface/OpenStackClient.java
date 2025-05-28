package upc.hybrid_quantum_orchestration.south_bound_interface;

import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.core.transport.Config;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.openstack.OSFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import upc.hybrid_quantum_orchestration.north_bound_interface.NBIController;

public class OpenStackClient{
	
	boolean isEnabled;
	String ip,url, user, password, project, domain;
	
	OSClientV3 os;
	
	private static final Logger log = LoggerFactory.getLogger(OpenStackClient.class);
	
	public OpenStackClient() {
	}
	
	public void initialize(boolean isEnabled, String ip, String url, String user, String password, String project, String domain) {
		this.isEnabled = isEnabled;
		this.ip = ip;
		this.url = url;
		this.user = user;
		this.password = password;
		this.project = project;
		this.domain = domain;
		/*Config config = Config.newConfig();
		config.withEndpointNATResolution("172.26.37.219");
		
		os = OSFactory.builderV3()
                .endpoint(this.url)
                .withConfig(config)
                .credentials(this.user, this.password, Identifier.byName(this.domain))
                .scopeToProject(Identifier.byName(this.project),Identifier.byName(this.domain))
                .authenticate();*/
		if(isEnabled == true) {
			log.info("Logged into OpenStack");
		}
		/*os.compute().servers().boot(Builders.server().name("test")
                .flavor("1")
                .image("0793e716-6171-428c-9f5f-56edd82ba423")
                .build());*/
	}
	
	public void setEnabledTo(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	public boolean isEnabled() {
		return this.isEnabled;
	}
	
	public String deployVM(String vmName, int flavorID, String imageID, String serverName) {
		ServerCreate sc = Builders.server()
				.name(vmName)
				.flavor(String.valueOf(flavorID))
				.image(imageID)
				.availabilityZone(serverName)
				.build();
		
		
		Config config = Config.newConfig();
		config.withEndpointNATResolution(ip);
		
		os = OSFactory.builderV3()
                .endpoint(this.url)
                .withConfig(config)
                .credentials(this.user, this.password, Identifier.byName(this.domain))
                .scopeToProject(Identifier.byName(this.project),Identifier.byName(this.domain))
                .authenticate();
		
		log.info("deploying in openstack");
		
		String id = os.compute().servers().boot(sc).getId();
		
		
		
		return id;
	}
	
	public void removeVM(String vmName) {
		Config config = Config.newConfig();
		config.withEndpointNATResolution(ip);
		
		os = OSFactory.builderV3()
                .endpoint(this.url)
                .withConfig(config)
                .credentials(this.user, this.password, Identifier.byName(this.domain))
                .scopeToProject(Identifier.byName(this.project),Identifier.byName(this.domain))
                .authenticate();
		
		os.compute().servers().delete(vmName);
	}
	
}