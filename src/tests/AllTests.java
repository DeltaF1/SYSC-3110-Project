package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import junit.framework.TestSuite;


@RunWith(Suite.class)
@SuiteClasses({
       TestBoard.class,
       TestEntity.class,
       TestEntityFactory.class,
       TestPlant.class,
       //TestSunflowerPlant.class,
       //TestProjectilePlant.class,
       TestTile.class,
       TestZombie.class,

})

public class AllTests extends TestSuite {
	
	/*public static void main(String[] args) {
		junit.textui.TestRunner.run(AllTests.class);
	}
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for com.saorsa.nowplaying.tests");
		suite.addTest(new TestSuite(TestMovieListWithEmptyList.class)); 
		suite.addTest(new TestSuite(TestMovieListWithOneMovie.class)); 
		suite.addTest(new TestSuite(TestMovieListWithTwoMovies.class));
		return suite;
	}*/
}
