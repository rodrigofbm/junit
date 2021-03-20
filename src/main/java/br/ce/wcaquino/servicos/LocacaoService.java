package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueExceptions;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoService {
	private LocacaoDAO dao;
	private SPCService spc;
	private EmailService emailService;
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueExceptions, LocadoraException {
		if (filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Filmes inv√°lidos");
		}
		
		for (Filme filme : filmes) {
			if(filme.getEstoque() == 0) 
				throw new FilmeSemEstoqueExceptions("Filme fora de estoque");
		}
		
		if (usuario == null) {
			throw new LocadoraException("Usu√°rio inv√°lido");
		}
		
		boolean isNegativado;
		
		try {
			isNegativado = spc.isNevativado(usuario);
			
			/*
			 * if(spc.isNevativado(usuario)) { throw new
			 * LocadoraException("Usu·rio negativado no SPC"); }
			 * 
			 * dessa forma, ao se lanÁar o LocadoraException, o catch logo abaixo ser· chamado
			 * enviando na verdade a outra mensagem
			 */
		} catch (Exception e) {
			throw new LocadoraException("Problemas com o SPC, tente novamente mais tarde.");
		}
		
		if(isNegativado) {
			throw new LocadoraException("Usu·rio negativado no SPC");
		}
		
		
		Locacao locacao = new Locacao();
		
		for (int i = 0; i < filmes.size(); i++) {
			Filme filme = filmes.get(i);
			
			switch (i) {
				case 2:
					filme.setPrecoLocacao(filme.getPrecoLocacao() * 0.75);
					break;
				case 3:
					filme.setPrecoLocacao(filme.getPrecoLocacao() * 0.50);
					break;
				case 4:
					filme.setPrecoLocacao(filme.getPrecoLocacao() * 0.25);;
					break;
				case 5:
					filme.setPrecoLocacao(filme.getPrecoLocacao() * 0.0);
					break;
			}
		}
		
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValorTotal(locacao.getSubTotal());
		

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
			dataEntrega = adicionarDias(dataEntrega, 1);
		}
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		dao.salvar(locacao);
		
		return locacao;
	}
	
	public void notificarAtrasos() {
		List<Locacao> pendentes = this.dao.obterLocacoesPendentes();
		
		for(Locacao pendente: pendentes) {
			if(pendente.getDataRetorno().before(new Date())) {
				emailService.notificarAtraso(pendente.getUsuario());
			}
		}
	}
	
	public void prorrogarLocacao(Locacao locacao, int dias) {
		Locacao novaLocacao = new Locacao();
		novaLocacao.setDataLocacao(new Date());
		novaLocacao.setDataRetorno(obterDataComDiferencaDias(dias));
		novaLocacao.setFilmes(locacao.getFilmes());
		novaLocacao.setUsuario(locacao.getUsuario());
		novaLocacao.setValorTotal(locacao.getValorTotal() * dias);
		
		dao.salvar(novaLocacao);
	}
	
	/*
	 * public void daoSetup(LocacaoDAO dao) { this.dao = dao; }
	 * 
	 * public void spcServiceSetup(SPCService service) { this.spc = service; }
	 * 
	 * public void emailServiceSetup(EmailService emailService) { this.emailService
	 * =emailService; }
	 */
}














