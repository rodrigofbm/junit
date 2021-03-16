package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;

public class AssertTest {
	
	@Test public void teste() {
		// boolean
		Assert.assertTrue(true);
		Assert.assertFalse(false);
		
		// int short long boolean
		Assert.assertEquals(1, 1);
		
		// terceiro parâmetro é a margem de erro
		Assert.assertEquals(0.51, 0.51, 0.001);
		
		// boxing e auto-boxing
		int i = 5;
		Integer i2 = 5;
		Assert.assertEquals(i, i2.intValue());
		Assert.assertEquals(Integer.valueOf(i), i2);

		// strings
		Assert.assertEquals("bola", "bola");
		Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
		Assert.assertTrue("bola".startsWith("bo"));
		
		// Objetos
		Usuario user1 = new Usuario("Rodrigo");
		Usuario user2 = new Usuario("Rodrigo");
		Usuario user3 = null;
		
		Assert.assertEquals(user1, user2);
		Assert.assertNull(user3);
		Assert.assertNotNull(user1);
		
		// Objetos - verificar se mesma instância
		Assert.assertSame(user2, user2);
		Assert.assertNotSame(user1, user2);
	}
}
