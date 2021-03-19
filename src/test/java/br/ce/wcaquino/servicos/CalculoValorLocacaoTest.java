package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.builders.FilmeBuilder.umFilme;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueExceptions;
import br.ce.wcaquino.exceptions.LocadoraException;

@RunWith(Parameterized.class)
public class CalculoValorLocacaoTest {
	private LocacaoService locacaoService;
	private SPCService spcService;
	private LocacaoDAO dao;
	private EmailService emailService;
	
	@Parameter
	public List<Filme> filmes;
	@Parameter(value = 1)
	public double valorLocacao;
	@Parameter(value = 2)
	public String cenario;
	
	@Before
	public void setup() {
		locacaoService = new LocacaoService();
		dao = Mockito.mock(LocacaoDAO.class);
		spcService = Mockito.mock(SPCService.class);
		emailService = Mockito.mock(EmailService.class);
		locacaoService.daoSetup(dao);
		locacaoService.spcServiceSetup(spcService);
		locacaoService.emailServiceSetup(emailService);
	}
	
	// Essa coleção será usada por cada método de teste dessa classe
	@Parameters(name = "{2}")
	public static Collection<Object[]> getParametros() {
		return Arrays.asList(new Object[][] {
			{ 
				Arrays.asList(umFilme().agora(),umFilme().agora()), 
				8.0,
				"2 Filmes: Sem desconto"
			},
			{ 
				Arrays.asList(
						umFilme().agora(),umFilme().agora(),
						umFilme().agora()), 
				11.0,
				"3 Filmes: 25%"
			},
			{ 	Arrays.asList(
					umFilme().agora(),umFilme().agora(),
					umFilme().agora(),umFilme().agora()), 
				13.0,
				"4 Filmes: 50%"
			},
			{ 
				Arrays.asList(
						umFilme().agora(),umFilme().agora(),
						umFilme().agora(),umFilme().agora(),
						umFilme().agora()), 
				14.0,
				"5 Filmes: 75%"
			},
			{ 
				Arrays.asList(
						umFilme().agora(),umFilme().agora(),
						umFilme().agora(),umFilme().agora(),
						umFilme().agora(),umFilme().agora()), 
				14.0,
				"6 Filmes: 100%"
			},
			{ 
				Arrays.asList(
						umFilme().agora(),umFilme().agora(),
						umFilme().agora(),umFilme().agora(),
						umFilme().agora(),umFilme().agora(),
						umFilme().agora()), 
				18.0,
				"7 Filmes: 100%"
			},
		});
	}
	
	@Test
	public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueExceptions, LocadoraException {
		// cenário
		Usuario usuario = new Usuario("Rodrigo");
		
		// ação
		Locacao locacao= locacaoService.alugarFilme(usuario, filmes);
		
		// verificação
		assertThat(locacao.getValorTotal(), is(valorLocacao));
	}
}
