package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZero;

public class CalculadoraTest {
	private Calculadora calc;
	
	@Before
	public void setup() {
		calc = new Calculadora();
	}
	
	@Test
	public void deveSomarDoisNumeros() {
		// cenário
		int a = 5;
		int b = 3;
		
		// ação
		int result = calc.somar(a,b);
		
		// verificação
		Assert.assertEquals(8,result);
	}
	
	@Test 
	public void deveSubtrairDoisNumeros() {
		// cenário
		int a = 5;
		int b = 3;
		
		// ação
		int result = calc.subtrair(a,b);
		
		// verificação
		Assert.assertEquals(2,result);
	}
	
	@Test 
	public void deveMultiplicarDoisNumeros() {
		// cenário
		int a = 5;
		int b = 3;
		
		// ação
		int result = calc.multiplicar(a,b);
		
		// verificação
		Assert.assertEquals(15,result);
	}
	
	@Test 
	public void deveDividirDoisNumeros() throws NaoPodeDividirPorZero {
		// cenário
		int a = 25;
		int b = 5;
		
		// ação
		int result = calc.dividir(a,b);
		
		// verificação
		Assert.assertEquals(5,result);
	}
	
	@Test(expected = NaoPodeDividirPorZero.class)
	public void lancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZero {
		// cenário
		int a = 25;
		int b = 0;
		
		// ação
		int result = calc.dividir(a,b);
		
		// verificação
		Assert.assertEquals(5,result);
	}
}
