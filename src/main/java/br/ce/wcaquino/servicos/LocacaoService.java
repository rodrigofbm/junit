package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Date;
import java.util.List;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueExceptions;
import br.ce.wcaquino.exceptions.LocadoraException;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueExceptions, LocadoraException {
		if (filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Filmes inválidos");
		}
		
		for (Filme filme : filmes) {
			if(filme.getEstoque() == 0) 
				throw new FilmeSemEstoqueExceptions("Filme fora de estoque");
		}
		
		if (usuario == null) {
			throw new LocadoraException("Usuário inválido");
		}
		
		Locacao locacao = new Locacao();
		
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(locacao.getValorTotal());

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}
}














