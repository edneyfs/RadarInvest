package br.com.edneysiqueira.radarinvest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RadarInvestApplication {

	public static void main(String[] args) {
		SpringApplication.run(RadarInvestApplication.class, args);
	}

}
