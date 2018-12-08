package tests;

import mainPackage.*;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;



public class TestLevel {
	Level level;
	LinkedList<String> zombs;
	
	@Before
	public void setup() {
		level = new Level();
		level.addSpawn(10, "doomer");
	}
	
	/**
	 * test that add spawn works
	 */
	@Test
	public void testAddSpawn() {
		level.addSpawn(0, "boomer");
		LinkedList<String> spawns = level.getSpawn(0);
		assertEquals("boomer should be added at wave 0",
				spawns.get(0) , "boomer");
		level.addSpawn(-1, "zoomer"); //make sure this doesn't throw an exception
		level.addSpawn(10, "basic");
		spawns = level.getSpawn(10);
		assertEquals("basic should be added at wave 10",
				spawns.get(1) , "basic");
	}
	
	/**
	 * test that setSpawn sets spawns
	 */
	@Test
	public void testSetSpawn() {
		zombs = new LinkedList<String>();
		zombs.add("basic");
		zombs.add("zoomer");
		zombs.add("doomer");
		zombs.add("zoomer");
		zombs.add("boomer");
		level.setSpawn(5, zombs);
		assertEquals("all zombs should have been added to wave 5", level.getSpawn(5), zombs);
		level.setSpawn(5, zombs);
		assertEquals("zombs on wave 5 should be the same after being added twice", level.getSpawn(5), zombs);
	}
	
	/**
	 * test that removeSpawn removes spawns
	 */
	@Test
	public void testRemoveSpawn() {
		level.removeSpawn(10, "doomer");
		assertEquals("wave 10 zomb should be gone", level.getSpawn(10).size(), 0);
		level.removeSpawn(10, "doomer");
		assertEquals("wave 10 zomb should still be gone", level.getSpawn(10).size(), 0);
		level.removeSpawn(69, "doomer"); //shouldn't throw an error
		level.removeSpawn(-1, "doomer"); //also shouldn't throw an error
	}
	
	/**
	 * test that getTotalZombies does what it says it does
	 */
	@Test
	public void testgetTotalZombies() {
		assertEquals("level should have 1 zombie", level.getTotalZombies(), 1);
		level.removeSpawn(10, "doomer");
		assertEquals("level should have 0 zombies after removing them all", 0, level.getTotalZombies());
		level.addSpawn(0, "basic");
		level.addSpawn(0, "basic");
		level.addSpawn(1, "basic");
		assertEquals("level should have 3 zombies after adding 3", level.getTotalZombies(), 3);
	}
	
	/**
	 * test another thing to make sure it does what it says it does
	 */
	@Test
	public void testGetSpawn() {
		zombs = new LinkedList<String>();
		zombs.add("basic");
		zombs.add("zoomer");
		zombs.add("doomer");
		zombs.add("zoomer");
		zombs.add("boomer");
		level.setSpawn(5, zombs);
		assertEquals("all zombs should have been added to wave 5", level.getSpawn(5), zombs);
		assertEquals("there should be no zombs in wave 0", level.getSpawn(0), null);
		assertEquals("there should be no zombs in wave -1", level.getSpawn(-1), null);
	}
	
	
	/**
	 * test setXML and set XML
	 */
	@Test
	public void testToSetXML() {
		zombs = new LinkedList<String>();
		zombs.add("basic");
		zombs.add("zoomer");
		zombs.add("doomer");
		zombs.add("zoomer");
		zombs.add("boomer");
		level.setSpawn(5, zombs);
		Level newLevel = new Level();
		newLevel.setXML(level.toXML());
		assertEquals("level created from XML should have same zombies as original level", level.getSpawn(5), newLevel.getSpawn(5));
		assertEquals("level created from XML should have same zombies as original level", level.getSpawn(10), newLevel.getSpawn(10));
	}
	
	/**
	 * ensures that testWriteToDisk works
	 * @throws IOException
	 */
	@Test
	public void testWriteToDisk() throws IOException {
		level.writeToDisk("testLevelExport.xml");
		String fileStr = new String(Files.readAllBytes(Paths.get("testLevelExport.xml")));
		assertEquals("file should contain the same json that level creates", fileStr, level.toXML());
	}
}
