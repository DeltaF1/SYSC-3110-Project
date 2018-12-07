package mainPackage;

import java.util.LinkedList;

public class EditableLevel extends Level{
	
	private LinkedList<View> views;
	
	public EditableLevel(View view) {
		views = new LinkedList<View>();
		views.add(view);
	}
	
	public void addView(View view) {
		views.add(view);
	}
	
	private ZombieSpawnSettings selectedZomb = null;
	public void setSelected(ZombieSpawnSettings selectedZomb) {
		this.selectedZomb = selectedZomb;
	}
	
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
	
	public void removeSpawn() {
		super.removeSpawn(selectedZomb.getSpawnTurn(), selectedZomb.getName());
		selectedZomb = null;
		for(View view : views) {
			view.updateZombSettings( spawns );
		}
	}
	

}
