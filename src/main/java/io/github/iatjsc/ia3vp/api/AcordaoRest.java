package io.github.iatjsc.ia3vp.api;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.iatjsc.ia3vp.api.dto.Acordao;

@RestController
@Controller
public class AcordaoRest {

	@Autowired
	private TransportClient client;

	@CrossOrigin
	@RequestMapping(path="/", method = RequestMethod.GET)
	public List<Acordao> findAll() {
		return findAll(null);
	}


	public List<Acordao> findAll(String termos) {
		QueryBuilder query;
		if (StringUtils.isEmpty(termos)) {
			query = QueryBuilders.matchAllQuery();
		} else {

			query = QueryBuilders.queryStringQuery(termos).field("ementa");
		}

		System.out.println(query);

		SearchRequestBuilder request = getSearchRequest().setQuery(query);
		SearchResponse response = request.get();

		System.out.println("Total de " + response.getHits().getTotalHits() + " processos");


		List<Acordao> acordaos = new ArrayList<>();

		ObjectMapper objectMapper = new ObjectMapper();
		SearchHit[] hits = response.getHits().getHits();

		try {
			for (SearchHit hit : hits) {
				String source = hit.getSourceAsString();
				acordaos.add(objectMapper.readValue(source, Acordao.class));
			}
			return acordaos;
		} catch (IOException e) {
			throw new RuntimeException("Erro ao converter acordão para json", e);
		}
	}


	private SearchRequestBuilder getSearchRequest() {
		SearchRequestBuilder setTypes = this.client.prepareSearch("ia3vp").setTypes("acordao");
		return setTypes;
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
