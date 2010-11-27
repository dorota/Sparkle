package GUI;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import Logic.World;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Scene3D
{
    public Scene3D( Canvas3D canvas )
    {
        _world = new World();
        _contents = new BranchGroup();
        _contents.setCapability( BranchGroup.ALLOW_CHILDREN_EXTEND );
        _contents.setCapability( BranchGroup.ALLOW_DETACH );
        _universe = new SimpleUniverse( canvas );
        setSceneAppearance();
        handleUserSceneInteractions();
        _universe.getViewingPlatform().setNominalViewingTransform();
        _universe.addBranchGraph( _contents );
        _contentsOffset = _contents.numChildren();
    }

    private void handleUserSceneInteractions()
    {
        TransformGroup viewTransformGroup = _universe.getViewingPlatform()
                .getViewPlatformTransform();
        KeyNavigatorBehavior keyInteractor = new KeyNavigatorBehavior( viewTransformGroup );
        BoundingSphere movingBounds = new BoundingSphere( new Point3d( 0.0, 0.0, 0.0 ), 10.0 );
        keyInteractor.setSchedulingBounds( movingBounds );
        _contents.addChild( keyInteractor );
        MouseRotate behavior = new MouseRotate();
        behavior.setTransformGroup( viewTransformGroup );
        _contents.addChild( behavior );
        behavior.setSchedulingBounds( _bounds );
    }

    public void updateScene()
    {
    }

    public void addNewBlock( String material, Point3d startCoordinates, Point3d size )
    {
        BranchGroup childBG = new BranchGroup();
        TransformGroup tg = new TransformGroup();
        Transform3D transform = new Transform3D();
        Vector3d vector = new Vector3d( startCoordinates.x / 100.0f, startCoordinates.y / 100.0f,
            startCoordinates.z / 100.0f );
        transform.setTranslation( vector );
        tg.setTransform( transform );
        Appearance app = new Appearance();
        Color3f cellColor = new Color3f();
        float transparency = 0.8f;
        for( int i = 0; i < _world.get_availableMaterials().size(); ++i )
        {
            System.out.println( material + " " + _world.get_availableMaterials().get( i ).get_name() );
            if( _world.get_availableMaterials().get( i ).get_name().equals( material ) )
            {
                cellColor = _world.get_availableMaterials().get( i ).get_color();
                transparency = (float)_world.get_availableMaterials().get( i ).get_transparency();
            }
        }
        ColoringAttributes coloringAttributes = new ColoringAttributes( cellColor,
            ColoringAttributes.NICEST );
        app.setColoringAttributes( coloringAttributes );
        app.setTransparencyAttributes( new TransparencyAttributes( TransparencyAttributes.FASTEST,
            transparency ) );
        System.out.println( size.x + " " + size.y + " " + size.z );
        tg.addChild( new Box( (float)( size.x / _defaultZoomOut ),
            (float)( size.y / _defaultZoomOut ), (float)( size.z / _defaultZoomOut ),
            Box.ENABLE_APPEARANCE_MODIFY, app ) );
        tg.getChild( 0 ).setCapability( Box.ENABLE_APPEARANCE_MODIFY );
        childBG.addChild( tg );
        _startsOfBlocks.add( vector );
        // detect collision
        Enumeration children = _contents.getAllChildren();
        // for( int i = 0; i < _startsOfBlocks.size(); ++i )
        // {
        // Object child = children.nextElement();
        // if( child instanceof BranchGroup )
        // {
        // TransformGroup transfG = (TransformGroup)( (BranchGroup)( child )
        // ).getChild( 0 );
        // Box el = (Box)( transfG.getChild( 0 ) );
        // Point3d newBlockSize = new Point3d( el.getXdimension(),
        // el.getYdimension(), el
        // .getZdimension() );
        // Vector3d blockCenter = _startsOfBlocks.get( i );
        // Point3d newBlockStart = new Point3d( blockCenter.x - newBlockSize.x /
        // 2.0,
        // blockCenter.y - newBlockSize.y / 2.0, blockCenter.z - newBlockSize.z
        // / 2.0 );
        // if( deteckBlockCollision( startCoordinates, size, newBlockStart,
        // newBlockSize ) )
        // {
        // ( (BranchGroup)child ).detach();
        // _startsOfBlocks.remove( i );
        // }
        // }
        // }
        _contents.addChild( childBG );
    }

    private void setSceneAppearance()
    {
        // float transparency = 0.9f;
        // Appearance app = new Appearance();
        // app.setTransparencyAttributes( new TransparencyAttributes(
        // TransparencyAttributes.FASTEST,
        // transparency ) );
        AmbientLight lightA = new AmbientLight();
        lightA.setColor( new Color3f( 255, 0, 100 ) );
        _contents.addChild( lightA );
        Background background = new Background();
        background.setColor( new Color3f( 0.2f, 0.7f, 0.1f ) );
        _bounds = new BoundingSphere( new Point3d( 0.0, 0.0, 0.0 ), 10.0 );
        background.setApplicationBounds( _bounds );
        _contents.addChild( background );
    }

    private SimpleUniverse _universe;
    private BranchGroup _contents;
    private World _world;
    private BoundingSphere _bounds;
    private double _defaultZoomOut = 100.0;
    /**
     * _contentsOffset - number of children added before blocks (lights,
     * background i inne syfy) needed as offset. Add all necessary non-block
     * stuff before initialization this variable!
     */
    private int _contentsOffset = 0;
    List<Vector3d> _startsOfBlocks = new ArrayList<Vector3d>();
}
