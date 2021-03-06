package br.ce.wcaquino.entidades;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Locacao {

	private Usuario usuario;
	private List<Filme> filmes = Arrays.asList();
	private Date dataLocacao;
	private Date dataRetorno;
	private Double valorTotal;
	
	public Usuario getUsuario() {
		return usuario;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Date getDataLocacao() {
		return dataLocacao;
	}
	
	public void setDataLocacao(Date dataLocacao) {
		this.dataLocacao = dataLocacao;
	}
	
	public Date getDataRetorno() {
		return dataRetorno;
	}
	
	public void setDataRetorno(Date dataRetorno) {
		this.dataRetorno = dataRetorno;
	}
	
	public Double getValorTotal() {
		return valorTotal;
	}
	
	public void setValorTotal(Double valor) {
		this.valorTotal = valor;
	}
	
	public List<Filme> getFilmes() {
		return filmes;
	}

	public void setFilmes(List<Filme> filmes) {
		this.filmes = filmes;
	}

	public Double getSubTotal() {
		double total = 0;
		
		for (Filme filme :this.filmes) {
		   total += filme.getPrecoLocacao();
		}
		
		return total;
	}
}