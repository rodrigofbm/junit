package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.CustomMatchers.caiEm;
import static br.ce.wcaquino.utils.DataUtils.adicionarDias;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assume;
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
import br.ce.wcaquino.utils.DataUtils;
import buildermaster.BuilderMaster;

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
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		//cenário
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().comValor(15.0).agora());
		
		//ação
		Locacao locacao = locacaoService.alugarFilme(usuario, filmes);
		
		//verificação
		error.checkThat(isMesmaData(locacao.getDataRetorno(), adicionarDias(new Date(), 1)), is(true));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(locacao.getValorTotal(), is(15.0));
	}
	
	@Test(expected = FilmeSemEstoqueExceptions.class)
	public void testeLocacaoErroForaDeEstoque() throws FilmeSemEstoqueExceptions, Exception {
		//cenário
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().semEstoque().agora());
		
		//ação
		locacaoService.alugarFilme(usuario, filmes);
	}
	
	@Test()
	public void testeLocacaoErroForaDeEstoque2() {
		//cenário
		Usuario usuario = umUsuario().agora();
		List<Filme> filme = Arrays.asList(umFilme().semEstoque().agora());
		
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
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());
		
		exception.expect(Exception.class);
		exception.expectMessage("Filme fora de estoque");
		//ação
		locacaoService.alugarFilme(usuario, filmes);
	}
	
	@Test()
	public void testeLocacaoUsuarioVazio() throws FilmeSemEstoqueExceptions {
		//cenário
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
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
		Usuario usuario = umUsuario().agora();
		
		exception.expect(Exception.class);
		exception.expectMessage("Filmes inválidos");
		//ação
		locacaoService.alugarFilme(usuario, null);
		
		System.out.println("Forma nova");
	}
	
	@Test
	//@Ignore
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueExceptions, LocadoraException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		// cenário
		List<Filme> filmes  = Arrays.asList(umFilme().agora());
		Usuario usuario = umUsuario().agora();
		
		// ação
		Locacao locacao = locacaoService.alugarFilme(usuario, filmes);
		
		// verificação
		// boolean ehSegunda = DataUtils.verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY);
		// assertTrue(ehSegunda);
		assertThat(locacao.getDataRetorno(), caiEm(Calendar.MONDAY));
		//assertThat(locacao.getDataRetorno(), CustomMatchers.caiNumaSegunda());
	}
	
	public static void main(String[] args) {
		new BuilderMaster().gerarCodigoClasse(Locacao.class);
	}
}
