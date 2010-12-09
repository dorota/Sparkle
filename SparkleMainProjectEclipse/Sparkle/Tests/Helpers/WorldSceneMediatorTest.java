package Helpers;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;


public class WorldSceneMediatorTest
{
    private int _realWorldSize = EnvSettings.getMAX_LENGTH();

    @Before
    public void testPrep()
    {
        EnvSettings.setMAX_LENGTH( 4 );
    }

    public void testCleanUp()
    {
        EnvSettings.setMAX_LENGTH( _realWorldSize );
    }

    @Test
    public void testChangeWorldIndexToSceneIndex()
    {
        assertEquals( "2 od lewej, 1 piêtro, przedostatni rz¹d",
            WorldSceneMediator.changeWorldIndexToSceneIndex( 1, 0, 1 ), 5 );
        assertEquals( 12, WorldSceneMediator.changeWorldIndexToSceneIndex( 0, 0, 3 ) );
    }

    @Test
    public void testChangeSceneIndexToWorldIndex()
    {
        WorldSceneMediator.WorldIndex indexResult = new WorldSceneMediator.WorldIndex();
        indexResult.x = 0;
        indexResult.y = 0;
        indexResult.z = 3;
        assertEquals( indexResult, WorldSceneMediator.changeSceneIndexToWorldIndex( 12 ) );
        indexResult.x = 1;
        indexResult.y = 0;
        indexResult.z = 1;
        assertEquals( indexResult, WorldSceneMediator.changeSceneIndexToWorldIndex( 5 ) );
    }
}
