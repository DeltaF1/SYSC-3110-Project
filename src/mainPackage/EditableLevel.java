package mainPackage;

import java.util.LinkedList;

public class EditableLevel extends Level{
	
	private LinkedList<View> views;
	
	public EditableLevel(View view) {
		views = new LinkedList<View>();
		views.add(view);
	}
	
	/**
	 * add another view
	 * @param view
	 */
	public void addView(View view) {
		views.add(view);
	}
	
	
	private ZombieSpawnSettings selectedZomb = null;
	/**
	 * set the selected zombie
	 * 
	 * @param selectedZomb the properties of the zombie to select
	 */
	public void setSelected(ZombieSpawnSettings selectedZomb) {
		this.selectedZomb = selectedZomb;
	}
	
	/**
	 * add another zombie to be spawned
	 * 
	 * @param wave the turn to spawn the zombie
	 * @param string the type name of the zombie
	 */
	public void addSpawn(int wave, String zombieType) {
		super.addSpawn(wave, zombieType);
		for(View view : views) {
			view.updateZombSettings( spawns );
		}
	}
	
	public void setSpawn(int wave, LinkedList<String> zombieTypes) {
		super.setSpawn(wave, zombieTypes);
		for(View view : views) {
			view.updateZombSettings( spawns );
		}
	}
	
	/**
	 * remove the selected zombie from the zombies to be spawned
	 */
	public void removeSpawn() {
		super.removeSpawn(selectedZomb.getSpawnTurn(), selectedZomb.getName());
		selectedZomb = null;
		for(View view : views) {
			view.updateZombSettings( spawns );
		}
	}
	

}
