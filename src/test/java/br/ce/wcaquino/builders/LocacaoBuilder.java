package br.ce.wcaquino.builders;

import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

import java.util.Arrays;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;

import java.lang.Double;
import java.util.Date;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;


public class LocacaoBuilder {
	private Locacao locacao;
	
	private LocacaoBuilder(){}

	public static LocacaoBuilder umLocacao() {
		LocacaoBuilder builder = new LocacaoBuilder();
		inicializarDadosPadroes(builder);
		return builder;
	}

	public static void inicializarDadosPadroes(LocacaoBuilder builder) {
		builder.locacao = new Locacao();
		Locacao locacao = builder.locacao;

		
		locacao.setUsuario(umUsuario().agora());
		locacao.setFilmes(Arrays.asList(umFilme().agora()));
		locacao.setDataLocacao(new Date());
		locacao.setDataRetorno(DataUtils.adicionarDias(new Date(), 1));
		locacao.setValorTotal(4.0);
	}

	public LocacaoBuilder comUsuario(Usuario param) {
		locacao.setUsuario(param);
		return this;
	}

	public LocacaoBuilder comListaFilmes(Filme... params) {
		locacao.setFilmes(Arrays.asList(params));
		return this;
	}

	public LocacaoBuilder comDataLocacao(Date param) {
		locacao.setDataLocacao(param);
		return this;
	}

	public LocacaoBuilder comDataRetorno(Date param) {
		locacao.setDataRetorno(param);
		return this;
	}

	public LocacaoBuilder comValorTotal(Double param) {
		locacao.setValorTotal(param);
		return this;
	}

	public Locacao agora() {
		return locacao;
	}
}
