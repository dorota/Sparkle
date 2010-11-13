package Interface;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;

import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Scene3D
{
    public Scene3D( Canvas3D canvas )
    {
        _contents = new BranchGroup();
        _contents.addChild( new ColorCube( 0.3 ) );
        _universe = new SimpleUniverse( canvas );
        _universe.getViewingPlatform().setNominalViewingTransform();
        _universe.addBranchGraph( _contents );
    }

    private final SimpleUniverse _universe;
    private final BranchGroup _contents;
}
