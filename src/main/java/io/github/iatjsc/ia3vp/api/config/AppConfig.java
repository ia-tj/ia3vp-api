package io.github.iatjsc.ia3vp.api.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(scanBasePackages="io.github.iatjsc.ia3vp.api")
public class AppConfig implements WebMvcConfigurer{

	private TransportClient client;

	@PostConstruct
	public void initClient() throws UnknownHostException {
		this.client = new PreBuiltTransportClient(Settings.EMPTY)
				.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

	}

	@Bean
	public TransportClient getClient() {
		return this.client;
	}
}