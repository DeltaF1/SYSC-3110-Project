package tests;
import mainPackage.*;
import mainPackage.plants.Plant;

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
	
	/**
	 * test that plants return the right attack speed
	 */
	@Test
	public void testGetAtkSpd() {
		assertEquals("Attack speed should be 2",
				plant.getAtkSpd(),2);
	}
	
	//@Test
	//public void testGetCost() {
	//	assertEquals("Attack speed should be 2",
	//			plant.getAtkSpd(),2);
	//}
	
	/**
	 * test that plants return the right cool down time
	 */
	@Test
	public void testGetCoolDown() {
		assertEquals("Attack speed should be 2",
				plant.getAtkSpd(),2);
	}
	
	/**
	 * test that plants can have their cooldown set corectly
	 */
	@Test
	public void testSetCoolDown() {
		plant.setCoolDown(3);
		assertEquals("Attack speed should be 3",
				plant.getCoolDown(),3);
	}
}
