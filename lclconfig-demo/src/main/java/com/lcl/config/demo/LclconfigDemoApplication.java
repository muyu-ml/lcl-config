package com.lcl.config.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import com.lcl.config.client.annotation.EnableLclConfig;
import java.util.Arrays;

@SpringBootApplication
@EnableConfigurationProperties(LclDemoConfig.class)
@Slf4j
@EnableLclConfig
public class LclconfigDemoApplication {

	@Value("${lcl.a}")
	private String a;

	@Autowired
	private LclDemoConfig lclDemoConfig;

	@Autowired
	Environment environment;


	public static void main(String[] args) {
		SpringApplication.run(LclconfigDemoApplication.class, args);
	}

	@Bean
	ApplicationRunner applicationRunner() {
		log.info(Arrays.toString(environment.getActiveProfiles()));
		return args -> {
			log.info(a);
			log.info(lclDemoConfig.getA());
		};
	}

}
