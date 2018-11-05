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
	}
}
/*
	
	 * makes a new zombie
	 * @param type type of zombie to create
	 * @return null if zombie type doesn't exist, otherwise returns a genuine zombie
	
	public Zombie makeZombie(String type) {
		return (Zombie) makeEntity(EntityType.ZOMBIE, type);
	}
	
	
	 * makes a new plant
	 * @param type type of plant to create
	 * @return a zombie. JUST KIDDING its a plant. unless the type doesn't exist, then it returns null
	 
	public Plant makePlant(String type) {
		return (Plant) makeEntity(EntityType.PLANT, type);
	}
	
	
	 * makes a plant or a zombie of specified plant/zombie type
	 * @param t the type of entity to make either EntityType.ZOMBIE or EntityType.PLANT
	 * @param type more specific String type of entity
	 * @return a new Entity if it was made successfully, otherwise null
	 
	private Entity makeEntity(EntityType t, String type) {
		Class<? extends Entity> entitySubclass = plantTypes.get(type);
		if (entitySubclass == null)
			return null;
		try {
			if (t == EntityType.PLANT)
				return entitySubclass.getConstructor().newInstance();
			if (t == EntityType.ZOMBIE) 
				return entitySubclass.getConstructor().newInstance();
		} catch (InvocationTargetException | InstantiationException | IllegalAccessException 
				| IllegalArgumentException | NoSuchMethodException | SecurityException err) {
			throw new RuntimeException("One of your registered entity subclasses doesn't exist, or doesn't have an empty constructor");
		}
		return null;
	}
*/