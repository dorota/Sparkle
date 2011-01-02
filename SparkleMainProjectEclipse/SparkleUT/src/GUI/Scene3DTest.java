package GUI;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.vecmath.Vector3d;

import org.junit.Test;

import Model.Material;
import Model.World;
import View.Scene3D;

import com.sun.j3d.utils.universe.SimpleUniverse;

public class Scene3DTest
{
    private World _world;
    private Scene3D _scene;
    private Canvas3D _sceneCanvas;

    public Scene3DTest()
    {
        _sceneCanvas = new Canvas3D( SimpleUniverse.getPreferredConfiguration() );
        _scene = Scene3D.getScene( _sceneCanvas );
        _world = World.getWorld( _scene );
    }

    @Test
    public void testBuildWorld()
    {
        Material mat = _world.get_availableMaterials().get( 1 );
        _scene.createdWorldRepresentation( mat, 2, 1, 1 );
        BranchGroup worldContens = _scene.get_contents();
        System.out.println( _scene.getNumberOfBlocks() );
        assertEquals( 2, _scene.getNumberOfBlocks() );
        List<Vector3d> startsOfBlocks = _scene.get_startsOfBlocks();
        assertEquals( 2, startsOfBlocks.size() );
        assertEquals( new Vector3d( 0.025, 0.025, 0.025 ), startsOfBlocks.get( 0 ) );
        assertEquals( new Vector3d( 0.07500000000000001, 0.025, 0.025 ), startsOfBlocks.get( 1 ) );
    }

    @Test
    public void addNewBlockTest()
    {
        Material mat = _world.get_availableMaterials().get( 0 ); // wood
    }
}
