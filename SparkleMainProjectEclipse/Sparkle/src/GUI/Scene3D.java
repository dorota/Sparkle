package GUI;

import java.util.ArrayList;
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

    public int get_contentsOffset()
    {
        return _contentsOffset;
    }

    public void set_contentsOffset( int offset )
    {
        _contentsOffset = offset;
    }

    public int getNumberOfBlocks()
    {
        return _contents.numChildren() - _contentsOffset;
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

    /**
     * Ad block with default size of 50cm x 50cm x 50cm
     * 
     * @param material
     *            - name of material. Must be rom World._availableMaterials
     *            list.
     * @param startCoordinates
     *            - given in cells unit
     * 
     */
    public void addNewBlock( Logic.Material material, Vector3d startCoordinates, Point3d blocSize )
    {
        BranchGroup childBG = new BranchGroup();
        TransformGroup tg = new TransformGroup();
        Transform3D transform = new Transform3D();
        Vector3d vector = new Vector3d( startCoordinates.x, startCoordinates.y, startCoordinates.z );
        transform.setTranslation( vector );
        tg.setTransform( transform );
        Appearance app = new Appearance();
        Color3f cellColor = new Color3f();
        float transparency = 0.8f;
        cellColor = material.get_color();
        transparency = (float)material.get_transparency();
        ColoringAttributes coloringAttributes = new ColoringAttributes( cellColor,
            ColoringAttributes.NICEST );
        app.setColoringAttributes( coloringAttributes );
        app.setTransparencyAttributes( new TransparencyAttributes( TransparencyAttributes.FASTEST,
            transparency ) );
        tg.addChild( new Box( (float)( blocSize.x / 2 ), (float)( blocSize.y / 2 ),
            (float)( blocSize.z / 2 ), Box.ENABLE_APPEARANCE_MODIFY, app ) );
        tg.getChild( 0 ).setCapability( Box.ENABLE_APPEARANCE_MODIFY );
        childBG.addChild( tg );
        _startsOfBlocks.add( vector );
        _contents.addChild( childBG );
    }

    /**
     * creates scene representation of world with given dimensions
     * World.getMAX_LENGTH. Created world contains only air.
     */
    public void buildWorld( Logic.Material defaultMaterial, int worldX, int worldY, int worldZ )
    {
        System.out.println( "how many world we build" );
        double blockSize = 0.05;
        for( int i = 0; i < worldX; ++i )
        {
            for( int j = 0; j < worldY; ++j )
            {
                for( int k = 0; k < worldZ; ++k )
                {
                    Vector3d blockPos = Helpers.WorldSceneMediator
                            .changeWorldPlacementToScenePlacement( i, j, k, blockSize );
                    addNewBlock( defaultMaterial, blockPos, new Point3d( blockSize, blockSize,
                        blockSize ) );
                }
            }
        }
    }

    private void setSceneAppearance()
    {
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

    public BranchGroup get_contents()
    {
        return _contents;
    }

    public void set_contents( BranchGroup _contents )
    {
        this._contents = _contents;
    }

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

    public List<Vector3d> get_startsOfBlocks()
    {
        return _startsOfBlocks;
    }

    public void set_startsOfBlocks( List<Vector3d> ofBlocks )
    {
        _startsOfBlocks = ofBlocks;
    }
}
