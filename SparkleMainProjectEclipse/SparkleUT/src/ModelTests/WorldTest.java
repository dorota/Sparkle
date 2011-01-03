package ModelTests;

import static junit.framework.Assert.assertEquals;

import java.util.List;

import javax.media.j3d.Canvas3D;

import org.junit.Before;
import org.junit.Test;

import Helpers.EnvSettings;
import Model.World;
import View.Scene3D;

import com.sun.j3d.utils.universe.SimpleUniverse;

public class WorldTest
{
    World _w;
    Scene3D _scene;
    Canvas3D _canvas;

    @Before
    public void setUp()
    {
        _canvas = new Canvas3D( SimpleUniverse.getPreferredConfiguration() );
        _scene = Scene3D.getScene( _canvas );
        _w = World.getWorld( _scene );
        EnvSettings.setMAX_LENGTH( 4 );
    }

    @Test
    public void getNeighboursTest()
    {
        List<World.CellIndex> neighs = _w.getNeighbours( new World.CellIndex( 0, 0, 0 ) );
        assertEquals( 3, neighs.size() );
        neighs = _w.getNeighbours( new World.CellIndex( 3, 3, 3 ) );
        assertEquals( 3, neighs.size() );
        neighs = _w.getNeighbours( new World.CellIndex( 2, 2, 2 ) );
        assertEquals( 6, neighs.size() );
    }
}
