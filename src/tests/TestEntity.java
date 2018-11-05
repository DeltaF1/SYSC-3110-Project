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
	
	@Test
	public void testGetName() {
		assertEquals("unexpected name returned by getName",
				entity.getName(),"testEntity");
	}
	
	@Test
	public void testGetHP() {
		assertEquals("unexpected return by getHp",
				entity.getHp(),5);
	}
	
	@Test
	public void testSetHP() {
		entity.setHp(3);
		assertEquals("unexpected return by getHp after setHp",
				entity.getHp(),3);
	}
	
	@Test
	public void testGetDamage() {
		assertEquals("unexpected damage returned by getDamage",
				entity.getDamage(),2);
	}
}

/*

	public String getName() {
		return name;
	}
	
	public int getHp() {
		return hp;
	}
	
	public void setHp(int hp) {
		this.hp = hp;
	}
	
	public int getDamage() {
*/