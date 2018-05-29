package io.github.iatjsc.ia3vp.api.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Acordao {

	private String processo;
	private String classe;
	private Timestamp movimentacao;
	private String ementa;
	private String  classificacao;

	public String getProcesso() {
		return this.processo;
	}
	public void setProcesso(String processo) {
		this.processo = processo;
	}
	public String getClasse() {
		return this.classe;
	}
	public void setClasse(String classe) {
		this.classe = classe;
	}
	public Timestamp getMovimentacao() {
		return this.movimentacao;
	}
	public void setMovimentacao(Timestamp movimentacao) {
		this.movimentacao = movimentacao;
	}

	public String getEmenta() {
		return this.ementa;
	}
	public void setEmenta(String ementa) {
		this.ementa = ementa;
	}
	public String getClassificacao() {
		return this.classificacao;
	}
	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}

}
