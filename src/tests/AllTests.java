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
       TestZombie.class,
       TestController.class,
       TestView.class

})

public class AllTests extends TestSuite {
	
}
