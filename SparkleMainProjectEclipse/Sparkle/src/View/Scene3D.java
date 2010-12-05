package View;

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

import Helpers.EnvSettings;
import Model.Material;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.universe.SimpleUniverse;

public class Scene3D
{
    private static Scene3D _instance = null;
    private static int _contentsOffset = Helpers.EnvSettings.HOW_MANY_GARBAGE_BRANCH_GROUP_CHILDREN;
    private BoundingSphere _bounds;
    private Point3d _centerRepresentationPosition;

    public static Scene3D getScene( Canvas3D canvas )
    {
        if( _instance == null )
        {
            _instance = new Scene3D( canvas );
        }
        return _instance;
    }

    private Scene3D( Canvas3D canvas )
    {
        _contents = new BranchGroup();
        _contents.setCapability( BranchGroup.ALLOW_CHILDREN_EXTEND );
        _contents.setCapability( BranchGroup.ALLOW_CHILDREN_READ );
        _contents.setCapability( BranchGroup.ALLOW_CHILDREN_WRITE );
        _contents.setCapability( BranchGroup.ALLOW_DETACH );
        _universe = new SimpleUniverse( canvas );
        setSceneAppearance();
        handleUserSceneInteractions();
        _universe.getViewingPlatform().setNominalViewingTransform();
        _universe.addBranchGraph( _contents );
    }

    public static int get_contentsOffset()
    {
        return _contentsOffset;
    }

    public static void set_contentsOffset( int offset )
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
        // BoundingSphere movingBounds = new BoundingSphere( new Point3d( 0.0,
        // 0.0, 0.0 ), 10.0 );
        keyInteractor.setSchedulingBounds( _bounds );
        _contents.addChild( keyInteractor );
        MouseRotate behavior = new MouseRotate();
        behavior.setTransformGroup( viewTransformGroup );
        _contents.addChild( behavior );
        behavior.setSchedulingBounds( _bounds );
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
    public void addNewBlock( Model.Material material, Vector3d startCoordinates, Point3d blocSize )
    {
        BranchGroup childBG = new BranchGroup();
        TransformGroup tg = new TransformGroup();
        Transform3D transform = new Transform3D();
        Vector3d vector = new Vector3d( startCoordinates.x, startCoordinates.y, startCoordinates.z );
        transform.setTranslation( vector );
        tg.setTransform( transform );
        Appearance app = new Appearance();
        Color3f cellColor = new Color3f();
        // float transparency = 0.8f;
        cellColor = material.get_color();
        float transparency = (float)material.get_transparency();
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
        childBG.setCapability( BranchGroup.ALLOW_DETACH );
        _contents.addChild( childBG );
    }

    private Box getBlockWithGivenId( int blockIndex )
    {
        Box cell = (Box)( ( (TransformGroup)( ( (BranchGroup)( _contents.getChild( blockIndex ) ) )
                .getChild( 0 ) ) ).getChild( 0 ) );
        return cell;
    }

    public void addNewBlockToScene( Material material, int blockIndex )
    {
        // System.out.println( "block index " + blockIndex );
        Box cell = getBlockWithGivenId( blockIndex );
        Appearance app = new Appearance();
        Color3f cellColor = material.get_color();
        ColoringAttributes coloringAttributes = new ColoringAttributes( cellColor,
            ColoringAttributes.NICEST );
        float transparency = (float)material.get_transparency();
        app.setColoringAttributes( coloringAttributes );
        app.setTransparencyAttributes( new TransparencyAttributes( TransparencyAttributes.FASTEST,
            transparency ) );
        cell.setAppearance( app );
    }

    private void setCellColor( Color3f color, int blockId )
    {
        Box cell = getBlockWithGivenId( blockId );
        Appearance app = new Appearance();
        ColoringAttributes coloringAttributes = new ColoringAttributes( color,
            ColoringAttributes.NICEST );
        app.setColoringAttributes( coloringAttributes );
        cell.setAppearance( app );
    }

    public void updateBlockWhileSimulation( int blockIndex, double temp )
    {
        float scale = clamp( (float)( temp / EnvSettings.FIRE_TEMP ), 0.0f, 1.0f );
        setCellColor( new Color3f( lerp( 0.0f, 1.0f, scale ), // red
            0.0f, // green
            lerp( 1.0f, 0.0f, scale ) ), blockIndex );
    }

    private float lerp( float from, float to, float scale )
    {
        return from * ( 1.0f - scale ) + to * scale;
    }

    private float clamp( float what, float min, float max )
    {
        return Math.max( Math.min( what, max ), min );
    }

    /**
     * creates scene representation of world with given dimensions
     * World.getMAX_LENGTH. Created world contains only air.
     */
    public void createdWorldRepresentation( Model.Material defaultMaterial, int worldX, int worldY,
            int worldZ )
    {
        double blockSize = EnvSettings.CELL_REPRESENTATION_LENGTH;
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
        int centerX = worldX / 2;
        int centerY = worldY / 2;
        int centerZ = worldZ / 2;
        int sceneCenterIndex = Helpers.WorldSceneMediator.changeWorldIndexToSceneIndex( centerX,
            centerY, centerZ );
        // System.out.println( "center bounds " + getBlockWithGivenId(
        // sceneCenterIndex ).getBounds(). );
        // _centerRepresentationPosition=getBlockWithGivenId( sceneCenterIndex
        // ).
        // System.out.println( "number of nodes " + _contents.numChildren() );
    }

    private void setSceneAppearance()
    {
        AmbientLight lightA = new AmbientLight();
        lightA.setColor( new Color3f( 255, 0, 100 ) );
        _contents.addChild( lightA );
        Background background = new Background();
        background.setColor( EnvSettings.BACKGROUND_COLOR );
        _bounds = new BoundingSphere( new Point3d( 0.0, 0.0, 0.0 ), 0.1 );
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

    /**
     * _contentsOffset - number of children added before blocks (lights,
     * background i inne syfy) needed as offset. Add all necessary non-block
     * stuff before initialization this variable!
     */
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
