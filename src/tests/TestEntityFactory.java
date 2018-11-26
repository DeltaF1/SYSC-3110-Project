package tests;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;

import mainPackage.Board;
import mainPackage.Entity;
import mainPackage.EntityFactory;
import mainPackage.plants.Plant;
import mainPackage.plants.ProjectilePlant;
import mainPackage.plants.Sunflower;
import mainPackage.zombies.Zombie;

public class TestEntityFactory {
	EntityFactory entityFactory;
	@Before
	public void setUp() {
		entityFactory =  new EntityFactory();
	}
	
	/**
	 * Tests that plants can be created by name as expected
	 */
	@Test
	public void testMakePlant() {
		Plant plant = entityFactory.makePlant("proj");
		assertTrue(plant instanceof ProjectilePlant);
		plant = entityFactory.makePlant("sunflower");
		assertTrue(plant instanceof Sunflower);
		plant = entityFactory.makePlant("incorrectInput");
		assertTrue(plant == null);
	}
}
