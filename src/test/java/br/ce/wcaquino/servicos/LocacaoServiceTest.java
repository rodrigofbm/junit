package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static br.ce.wcaquino.builders.FilmeBuilder.umFilmeSemEstoque;
import static br.ce.wcaquino.builders.LocacaoBuilder.umLocacao;
import static br.ce.wcaquino.builders.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.CustomMatchers.caiEm;
import static br.ce.wcaquino.utils.DataUtils.adicionarDias;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueExceptions;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import buildermaster.BuilderMaster;

public class LocacaoServiceTest {
	@InjectMocks
	private LocacaoService locacaoService;
	@Mock
	private SPCService spcService;
	@Mock
	private LocacaoDAO dao;
	@Mock
	private EmailService emailService;
	// o Junit n√£o reinicializa vari√°vel est√°tica a cada teste
	// private static int count = 0;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception =  ExpectedException.none();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		/*
		 * locacaoService = new LocacaoService(); dao = Mockito.mock(LocacaoDAO.class);
		 * spcService = Mockito.mock(SPCService.class); emailService =
		 * Mockito.mock(EmailService.class); locacaoService.daoSetup(dao);
		 * locacaoService.spcServiceSetup(spcService);
		 * locacaoService.emailServiceSetup(emailService);
		 */
		// count++;
		// System.out.println(count);
	}
	
	@Test
	public void testeLocacao() throws Exception {
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		//cen√°rio
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().comValor(15.0).agora());
		
		//a√ß√£o
		Locacao locacao = locacaoService.alugarFilme(usuario, filmes);
		
		//verifica√ß√£o
		error.checkThat(isMesmaData(locacao.getDataRetorno(), adicionarDias(new Date(), 1)), is(true));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(locacao.getValorTotal(), is(15.0));
	}
	
	@Test(expected = FilmeSemEstoqueExceptions.class)
	public void testeLocacaoErroForaDeEstoque() throws FilmeSemEstoqueExceptions, Exception {
		//cen√°rio
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().semEstoque().agora());
		
		//a√ß√£o
		locacaoService.alugarFilme(usuario, filmes);
	}
	
	@Test()
	public void testeLocacaoErroForaDeEstoque2() {
		//cen√°rio
		Usuario usuario = umUsuario().agora();
		List<Filme> filme = Arrays.asList(umFilme().semEstoque().agora());
		
		//a√ß√£o
		try {
			locacaoService.alugarFilme(usuario, filme);
			fail("Deveria ter lan√ßado uma exception");
		} catch (Exception e) {
			assertThat(e.getMessage(), is("Filme fora de estoque"));
		}
	}
	
	@Test()
	public void testeLocacaoErroForaDeEstoque3() throws Exception {
		//cen√°rio
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilmeSemEstoque().agora());
		
		exception.expect(Exception.class);
		exception.expectMessage("Filme fora de estoque");
		//a√ß√£o
		locacaoService.alugarFilme(usuario, filmes);
	}
	
	@Test()
	public void testeLocacaoUsuarioVazio() throws FilmeSemEstoqueExceptions {
		//cen√°rio
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		//a√ß√£o
		try {
			locacaoService.alugarFilme(null, filmes);
			fail("Deveria lan√ßar exce√ß√£o");
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usu√°rio inv√°lido"));
		}
		
		// System.out.println("Forma robusta");
	}
	
	@Test()
	public void testeLocacaoFilmeVazio() throws FilmeSemEstoqueExceptions, LocadoraException {
		//cen√°rio
		Usuario usuario = umUsuario().agora();
		
		exception.expect(Exception.class);
		exception.expectMessage("Filmes inv√°lidos");
		//a√ß√£o
		locacaoService.alugarFilme(usuario, null);
		
		System.out.println("Forma nova");
	}
	
	@Test
	//@Ignore
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueExceptions, LocadoraException {
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		// cen√°rio
		List<Filme> filmes  = Arrays.asList(umFilme().agora());
		Usuario usuario = umUsuario().agora();
		
		// a√ß√£o
		Locacao locacao = locacaoService.alugarFilme(usuario, filmes);
		
		// verifica√ß√£o
		// boolean ehSegunda = DataUtils.verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY);
		// assertTrue(ehSegunda);
		assertThat(locacao.getDataRetorno(), caiEm(Calendar.MONDAY));
		//assertThat(locacao.getDataRetorno(), CustomMatchers.caiNumaSegunda());
	}
	
	@Test
	public void naoDeveAlugarFilmeParaNegativado() throws Exception {
		// cen·rio
		Usuario usuario = umUsuario().agora();
		//Usuario usuario2 = umUsuario().comNome("Marquito").agora();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 6, 12.99));
		
		Mockito.when(spcService.isNevativado(Mockito.any(Usuario.class))).thenReturn(true);
		
		// aÁ„o
		try {
			locacaoService.alugarFilme(usuario, filmes);
			// locacaoService.alugarFilme(usuario2, filmes); gera falha
		
		// verificaÁ„o
			fail("Deveria lanÁar uma exceÁ„o");
		}  catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usu·rio negativado no SPC"));
		}
		
		verify(spcService).isNevativado(usuario);
	}
	
	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		// cen·rio
		Usuario usuario = umUsuario().agora();
		Usuario usuario2 = umUsuario().comNome("Usu·rio em dia").agora();
		Usuario usuario3 = umUsuario().comNome("Outro atrasado").agora();
		//Usuario usuario2 = umUsuario().comNome("Usuario 2").agora();
		List<Locacao> pendentes = Arrays.asList(
				umLocacao().comUsuario(usuario).comAtraso().agora(),
				umLocacao().comUsuario(usuario2).agora(),
				umLocacao().comUsuario(usuario3).comAtraso().agora(),
				umLocacao().comUsuario(usuario3).comAtraso().agora());
		when(dao.obterLocacoesPendentes()).thenReturn(pendentes);
		
		// aÁ„o
		this.locacaoService.notificarAtrasos();
		
		// verificaÁ„o
		verify(emailService).notificarAtraso(usuario);
		verify(emailService, Mockito.times(2)).notificarAtraso(usuario3); // comentando essa linha ir· falhar pois esse cen·rio ocorreu por conta
		// do array List<Locacao> pendentes ter o usuario3 no nosso teste
		verify(emailService, Mockito.never()).notificarAtraso(usuario2);
		verifyNoMoreInteractions(emailService); // passa se nenhum outro e-mail alÈm do usuario e usuario3 foi enviado
		//verifyZeroInteractions(spcService);
	}
	
	@Test
	public void deveTratarErroDoSPC() throws Exception  {
		// cen·rio
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(umFilme().agora());
		
		when(spcService.isNevativado(usuario)).thenThrow(new Exception("Falha catastrÛfica."));
		
		// verificaÁ„o
		exception.expect(LocadoraException.class);
		exception.expectMessage("Problemas com o SPC, tente novamente mais tarde.");
		
		// aÁ„o
		locacaoService.alugarFilme(usuario, filmes);
				
	}
	
	@Test
	public void deveProrrogarUmaLocacao() {
		// cen·rio
		Locacao locacao = umLocacao().agora();
		int dias = 3;
		// aÁ„o
		locacaoService.prorrogarLocacao(locacao, dias);
		
		// verificaÁ„o
		ArgumentCaptor<Locacao> argCapt = ArgumentCaptor.forClass(Locacao.class);
		verify(dao).salvar(argCapt.capture());
		Locacao locacaoProrrogada = argCapt.getValue();
		
		error.checkThat(locacaoProrrogada.getValorTotal(), is(4.0*dias));
		error.checkThat(isMesmaData(locacaoProrrogada.getDataRetorno(), adicionarDias(new Date(), dias)), is(true));
		error.checkThat(isMesmaData(locacaoProrrogada.getDataLocacao(), new Date()), is(true));
	}
	
	public static void main(String[] args) {
		new BuilderMaster().gerarCodigoClasse(Locacao.class);
	}
}
