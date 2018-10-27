import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class EntityFactory {
	private static HashMap<String, Class<? extends Plant>> plantTypes = new HashMap<String, Class<? extends Plant>>();
	private static HashMap<String, Class<? extends Zombie>> zombieTypes = new HashMap<String, Class<? extends Zombie>>();
	private static enum EntityType {PLANT, ZOMBIE};
	
	//register all plant types here
	private static void registerPlants() {
		plantTypes.put("proj", ProjectilePlant.class);
	}
	
	//register all zombie types here
	private static void registerZombies() {
		
	}
	
	public EntityFactory() {
		registerPlants();
		registerZombies();
	}
	
	public Zombie makeZombie(String type) {
		return (Zombie) makeEntity(EntityType.ZOMBIE, type);
	}
	
	public Plant makePlant(String type) {
		return (Plant) makeEntity(EntityType.PLANT, type);
	}
	
	private Entity makeEntity(EntityType t, String type) {
		try {
			if (t == EntityType.PLANT)
				return plantTypes.get(type).getConstructor().newInstance();
			if (t == EntityType.ZOMBIE) 
				return plantTypes.get(type).getConstructor().newInstance();
		} catch (InvocationTargetException | InstantiationException | IllegalAccessException 
				| IllegalArgumentException | NoSuchMethodException | SecurityException err) {
			throw new RuntimeException("One of your registered entity subclasses doesn't exist, or doesn't have an empty constructor");
		}
		return null;
	}
}