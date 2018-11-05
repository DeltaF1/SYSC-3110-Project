package tests;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import mainPackage.BasicZombie;
import mainPackage.Entity;
import mainPackage.Tile;

public class TestTile {
	Tile tile;
	
	@Before
	public void setUp() {
		tile = new Tile();
	}
	
	@Test
	public void testSetAndGetTile() {
		Entity occupant = new BasicZombie();
		tile.setOccupant(occupant);
		assertTrue("set tile was not returned by get",
				occupant == tile.getOccupant());
	}
}
