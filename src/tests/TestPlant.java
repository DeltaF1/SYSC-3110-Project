package tests;
import mainPackage.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TestPlant{
	Plant plant;
	
	@Before
	public void setUp() {
		plant = new Plant("somePlant",3,2,2,6);
	}
	
	@Test
	public void testGetAtkSpd() {
		assertEquals("Attack speed should be 2",
				plant.getAtkSpd(),2);
	}
	
	@Test
	public void testGetCost() {
		assertEquals("Attack speed should be 2",
				plant.getAtkSpd(),2);
	}
	
	@Test
	public void testGetCoolDown() {
		assertEquals("Attack speed should be 2",
				plant.getAtkSpd(),2);
	}
	
	@Test
	public void testSetCoolDown() {
		plant.setCoolDown(3);
		assertEquals("Attack speed should be 3",
				plant.getCoolDown(),3);
	}
}

/*
 * 
 * 
 * 	public Plant(String name, int hp, int damage, int atkSpd, int cost) {
		super(name, hp, damage);
		this.atkSpd = atkSpd;
		this.cost = cost;
		this.coolDown = atkSpd;
	}


	public int getAtkSpd() {
		return atkSpd;
	}


	public int getCost() {
		return cost;
	}
	

	public int getCoolDown() {
		return coolDown;
	}
	

	public void setCoolDown(int newCoolDown) {
		coolDown = newCoolDown;
	}
 */
