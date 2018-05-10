package io.github.iatjsc.ia3vp.api;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

import org.springframework.boot.SpringApplication;

import io.github.iatjsc.ia3vp.api.config.AppConfig;

public class Main3vp {

	public static void main(String[] args) throws UnknownHostException, InterruptedException, ExecutionException {
		SpringApplication.run(AppConfig.class, args);
	}

}
