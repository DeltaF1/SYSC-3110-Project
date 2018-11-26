package tests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import mainPackage.Board;
import mainPackage.Entity;

public class TestEntity {
	Entity entity;
	
	@Before
	public void setUp() {
		entity =  new Entity("testEntity",5,2);
	}
	
	/**
	 * tests that an entity returns the right name
	 */
	@Test
	public void testGetName() {
		assertEquals("unexpected name returned by getName",
				entity.getName(),"testEntity");
	}
	
	/**
	 * test that an entity returns the right HP
	 */
	@Test
	public void testGetHP() {
		assertEquals("unexpected return by getHp",
				entity.getHp(),5);
	}
	
	/**
	 * test that an entity can have its hp set right
	 */
	@Test
	public void testSetHP() {
		entity.setHp(3);
		assertEquals("unexpected return by getHp after setHp",
				entity.getHp(),3);
	}
	
	/**
	 * test that an entity can be damaged properly
	 */
	@Test
	public void testGetDamage() {
		assertEquals("unexpected damage returned by getDamage",
				entity.getDamage(),2);
	}
}
