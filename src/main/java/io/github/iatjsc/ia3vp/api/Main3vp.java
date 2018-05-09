package io.github.iatjsc.ia3vp.api;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.Normalizer;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Main3vp {

	@RequestMapping("/")
	long home() {
		SearchResponse result = searchRelevancesJudgmentByEmenta("crime");
		return result.getHits().getTotalHits();
	}


	public static void main(String[] args) throws UnknownHostException, InterruptedException, ExecutionException {
		SpringApplication.run(Main3vp.class, args);
		Main3vp main = new Main3vp();

		main.close();

	}

	private TransportClient client;

	public SearchResponse searchRelevancesJudgmentByEmenta(String ementa) {

		String query = toQuery(ementa);

		QueryStringQueryBuilder queryString = QueryBuilders.queryStringQuery(query).field("ementa");

		System.out.println(queryString);

		SearchRequestBuilder request = getSearchRequest().setQuery(queryString);
		SearchResponse response = request.get();

		System.out.println("Total de " + response.getHits().getTotalHits() + " processos");

		return response;
	}

	public void close() {
		if (this.client != null) {
			this.client.close();
		}
	}

	private SearchRequestBuilder getSearchRequest() {
		SearchRequestBuilder setTypes = getClient().prepareSearch("ia3vp").setTypes("acordao");
		return setTypes;
	}

	private TransportClient getClient() {
		if (this.client == null) {
			try {
				this.client = new PreBuiltTransportClient(Settings.EMPTY)
						.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
			} catch (UnknownHostException e) {
				throw new RuntimeException("Erro ao conectar ao elastic", e);
			}
		}
		return this.client;
	}

	private String toQuery(String termoBusca) {
		return limparTermo(termoBusca);
	}

	private String limparTermo(String termo) {
		String preProcessado = removerCaracteresEspeciais(substituirAcentosPorAscii(termo.toLowerCase()));
		String escape = escape(preProcessado).trim();
		System.out.println("Termo limpo: " + escape);
		return escape;
	}

	private String escape(String termo) {
		return termo.replaceAll("/", "\\\\/");
	}

	private String removerCaracteresEspeciais(String termo) {
		return termo.replaceAll("[^\\p{Alnum}\\s\\/\\-\\,\\.]", "");
	}

	private String substituirAcentosPorAscii(String str) {
		return Normalizer.normalize(str, Normalizer.Form.NFD)
				.replaceAll("[^\\p{ASCII}]", "");
	}

}
