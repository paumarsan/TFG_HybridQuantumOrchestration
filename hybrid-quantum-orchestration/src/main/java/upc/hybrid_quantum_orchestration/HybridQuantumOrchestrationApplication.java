package upc.hybrid_quantum_orchestration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import upc.hybrid_quantum_orchestration.core.HQOController;
@SpringBootApplication
public class HybridQuantumOrchestrationApplication implements CommandLineRunner {

	public HQOController hqoController = new HQOController();
	
	public static void main(String[] args) {
		
		new SpringApplication(HybridQuantumOrchestrationApplication.class).run(args);
		
	}

	@Override
	public void run(String... args)  {
		
		hqoController.initialize();
		
	}

	@Bean
	public HQOController getHQOController()
	{
		return this.hqoController;
	}
}
