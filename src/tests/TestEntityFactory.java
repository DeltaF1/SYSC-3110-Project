package tests;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;

import mainPackage.Board;
import mainPackage.Entity;
import mainPackage.EntityFactory;
import mainPackage.Plant;
import mainPackage.Tile;
import mainPackage.Zombie;
import mainPackage.ProjectilePlant;
import mainPackage.Sunflower;

public class TestEntityFactory {
	EntityFactory entityFactory;
	@Before
	public void setUp() {
		entityFactory =  new EntityFactory();
	}
	
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
