package Interface;

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

    /**
     * Checks whether block A is inside block B
     * 
     * @param startPointA
     *            - left-front-bottom Point of A box
     * @param sizeA
     *            - A dimensions
     * @param startPointB
     * @param sizeB
     * @return
     */
    // private boolean deteckBlockCollision( Point3d startPointA, Point3d sizeA,
    // Point3d startPointB,
    // Point3d sizeB )
    // {
    // if( startPointA.x >= startPointB.x && startPointA.x + sizeA.x <=
    // startPointB.x + sizeB.x
    // && startPointA.y >= startPointB.y
    // && startPointA.y + sizeA.y <= startPointB.y + sizeB.y && startPointA.z )
    // {
    // }
    // }
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
        for( int i = 0; i < _world._availableMaterials.size(); ++i )
        {
            System.out.println( material + " " + _world._availableMaterials.get( i )._name );
            if( _world._availableMaterials.get( i )._name.equals( material ) )
            {
                cellColor = _world._availableMaterials.get( i )._color;
                transparency = (float)_world._availableMaterials.get( i )._transparency;
            }
        }
        ColoringAttributes coloringAttributes = new ColoringAttributes( cellColor,
            ColoringAttributes.NICEST );
        app.setColoringAttributes( coloringAttributes );
        app.setTransparencyAttributes( new TransparencyAttributes( TransparencyAttributes.FASTEST,
            transparency ) );
        System.out.println( size.x + " " + size.y + " " + size.z );
        tg.addChild( new Box( (float)size.x / 100.0f, (float)size.y / 100.0f,
            (float)size.z / 100.0f, Box.ENABLE_APPEARANCE_MODIFY, app ) );
        tg.getChild( 0 ).setCapability( Box.ENABLE_APPEARANCE_MODIFY );
        childBG.addChild( tg );
        _contents.addChild( childBG );
    }

    private void setSceneAppearance()
    {
        float transparency = 0.9f;
        Appearance app = new Appearance();
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
}
