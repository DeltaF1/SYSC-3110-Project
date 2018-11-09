package mainPackage;
/**
 * A factory class to spawn Entities
 * Check GitHub for authors
 */

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class EntityFactory {
	private static HashMap<String, Class<? extends Plant>> plantTypes = new HashMap<String, Class<? extends Plant>>();
	private static HashMap<String, Class<? extends Zombie>> zombieTypes = new HashMap<String, Class<? extends Zombie>>();
	private static enum EntityType {PLANT, ZOMBIE};
	
	//register all plant types here
	private static void registerPlants() {
		plantTypes.put("proj", ProjectilePlant.class);
		plantTypes.put("sunflower", Sunflower.class);
	}
	
	//register all zombie types here
	private static void registerZombies() {
		
	}
	
	/**
	 * creates a new EntityFactory
	 */
	public EntityFactory() {
		registerPlants();
		registerZombies();
	}
	
	/**
	 * makes a new zombie
	 * @param type type of zombie to create
	 * @return null if zombie type doesn't exist, otherwise returns a genuine zombie
	 */
	public Zombie makeZombie(String type) {
		return (Zombie) makeEntity(EntityType.ZOMBIE, type);
	}
	
	/**
	 * makes a new plant
	 * @param type type of plant to create
	 * @return a zombie. JUST KIDDING its a plant. unless the type doesn't exist, then it returns null
	 */
	public Plant makePlant(String type) {
		return (Plant) makeEntity(EntityType.PLANT, type);
	}
	
	/**
	 * makes a plant or a zombie of specified plant/zombie type
	 * @param t the type of entity to make either EntityType.ZOMBIE or EntityType.PLANT
	 * @param type more specific String type of entity
	 * @return a new Entity if it was made successfully, otherwise null
	 */
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
}