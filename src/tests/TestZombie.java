package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import mainPackage.plants.Plant;
import mainPackage.zombies.Zombie;

public class TestZombie {
	Zombie zombie;
	
	@Before
	public void setUp() {
		zombie = new Zombie("somePlant", 60,20,2);
	}
	
	@Test
	public void testGetMovSpd() {
		assertEquals("Attack speed should be 2",
				zombie.getMovSpd(),2);
	}
}
