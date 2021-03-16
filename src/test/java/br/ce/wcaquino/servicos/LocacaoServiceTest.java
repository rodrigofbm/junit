package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueExceptions;
import br.ce.wcaquino.exceptions.LocadoraException;

public class LocacaoServiceTest {
	private LocacaoService locacaoService;
	// o Junit não reinicializa variável estática a cada teste
	// private static int count = 0;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception =  ExpectedException.none();
	
	@Before
	public void setup() {
		locacaoService = new LocacaoService();
		// count++;
		// System.out.println(count);
	}
	
	@Test
	public void testeLocacao() throws Exception {
		//cenário
		Usuario usuario = new Usuario("Rodrigo");
		List<Filme> filmes = Arrays.asList(new Filme("The Matrix", 5, 12.99), new Filme("The Dark Knight", 10, 19.99));
		
		//ação
		Locacao locacao = locacaoService.alugarFilme(usuario, filmes);
		
		//verificação
		error.checkThat(isMesmaData(locacao.getDataRetorno(), adicionarDias(new Date(), 1)), is(true));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(locacao.getValor(), is(32.98));
	}
	
	@Test(expected = FilmeSemEstoqueExceptions.class)
	public void testeLocacaoErroForaDeEstoque() throws FilmeSemEstoqueExceptions, Exception {
		//cenário
		Usuario usuario = new Usuario("Rodrigo");
		List<Filme> filmes = Arrays.asList(new Filme("The Matrix", 0, 12.99), new Filme("The Dark Knight", 0, 19.99));
		
		//ação
		locacaoService.alugarFilme(usuario, filmes);
	}
	
	@Test()
	public void testeLocacaoErroForaDeEstoque2() {
		//cenário
		Usuario usuario = new Usuario("Rodrigo");
		List<Filme> filme = Arrays.asList(new Filme("The Matrix", 0, 12.99), new Filme("The Dark Knight", 0, 19.99));
		
		//ação
		try {
			locacaoService.alugarFilme(usuario, filme);
			fail("Deveria ter lançado uma exception");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("Filme fora de estoque"));
		}
	}
	
	@Test()
	public void testeLocacaoErroForaDeEstoque3() throws Exception {
		//cenário
		Usuario usuario = new Usuario("Rodrigo");
		List<Filme> filmes = Arrays.asList(new Filme("The Matrix", 0, 12.99), new Filme("The Dark Knight", 0, 19.99));
		
		exception.expect(Exception.class);
		exception.expectMessage("Filme fora de estoque");
		//ação
		locacaoService.alugarFilme(usuario, filmes);
	}
	
	@Test()
	public void testeLocacaoUsuarioVazio() throws FilmeSemEstoqueExceptions {
		//cenário
		List<Filme> filmes = Arrays.asList(new Filme("The Matrix", 5, 12.99), new Filme("The Dark Knight", 10, 19.99));
		
		//ação
		try {
			locacaoService.alugarFilme(null, filmes);
			fail("Deveria lançar exceção");
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuário inválido"));
		}
		
		// System.out.println("Forma robusta");
	}
	
	@Test()
	public void testeLocacaoFilmeVazio() throws FilmeSemEstoqueExceptions, LocadoraException {
		//cenário
		Usuario usuario = new Usuario("Rodrigo");
		
		exception.expect(Exception.class);
		exception.expectMessage("Filmes inválidos");
		//ação
		locacaoService.alugarFilme(usuario, null);
		
		System.out.println("Forma nova");
	}
}
