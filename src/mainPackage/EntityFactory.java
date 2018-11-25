package mainPackage;
/**
 * A factory class to spawn Entities
 * Check GitHub for authors
 */

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import mainPackage.plants.Melonpult;
import mainPackage.plants.Plant;
import mainPackage.plants.ProjectilePlant;
import mainPackage.plants.Repeater;
import mainPackage.plants.Sunflower;
import mainPackage.plants.Wallnut;
import mainPackage.zombies.BasicZombie;
import mainPackage.zombies.Boomer;
import mainPackage.zombies.Doomer;
import mainPackage.zombies.Zombie;
import mainPackage.zombies.Zoomer;

public class EntityFactory {
	private static HashMap<String, Class<? extends Plant>> plantTypes = new HashMap<String, Class<? extends Plant>>();
	private static HashMap<String, Class<? extends Zombie>> zombieTypes = new HashMap<String, Class<? extends Zombie>>();
	private static enum EntityType {PLANT, ZOMBIE};
	
	// Initialize the entity types
	static {
		plantTypes.put("proj", ProjectilePlant.class);
		plantTypes.put("sunflower", Sunflower.class);
		plantTypes.put("wallnut", Wallnut.class);
		plantTypes.put("repeater", Repeater.class);
		plantTypes.put("melon", Melonpult.class);
		
		zombieTypes.put("basic", BasicZombie.class);
		zombieTypes.put("boomer",Boomer.class);
		zombieTypes.put("zoomer",Zoomer.class);
		zombieTypes.put("doomer",Doomer.class);
	}

	/**
	 * makes a new zombie
	 * @param type type of zombie to create
	 * @return null if zombie type doesn't exist, otherwise returns a genuine zombie
	 */
	public static Zombie makeZombie(String type) {
		return (Zombie) makeEntity(EntityType.ZOMBIE, type);
	}
	
	/**
	 * makes a new plant
	 * @param type type of plant to create
	 * @return a zombie. JUST KIDDING its a plant. unless the type doesn't exist, then it returns null
	 */
	public static Plant makePlant(String type) {
		return (Plant) makeEntity(EntityType.PLANT, type);
	}
	
	/**
	 * makes a plant or a zombie of specified plant/zombie type
	 * @param t the type of entity to make either EntityType.ZOMBIE or EntityType.PLANT
	 * @param type more specific String type of entity
	 * @return a new Entity if it was made successfully, otherwise null
	 */
	private static Entity makeEntity(EntityType t, String type) {
		Class<? extends Entity> entitySubclass = plantTypes.get(type);
		if (entitySubclass == null)
			entitySubclass = zombieTypes.get(type);
			if (entitySubclass == null)
				return null;
		try {
			if (t == EntityType.PLANT)
				return entitySubclass.getDeclaredConstructor(String.class).newInstance(type);
			if (t == EntityType.ZOMBIE)
				return entitySubclass.getDeclaredConstructor(String.class).newInstance(type);
		} catch (InvocationTargetException | InstantiationException | IllegalAccessException 
				| IllegalArgumentException | NoSuchMethodException | SecurityException err) {
			throw new RuntimeException("One of your registered entity subclasses doesn't exist, or doesn't have a valid constructor");
		}
		return null;
	}
}
