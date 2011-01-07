package Model;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;

import Helpers.EnvSettings;
import Helpers.ProfileSample;
import Interfaces.IFireConductor;
import Interfaces.IHeatAndVaporsConductor;
import Interfaces.IScene3D;
import Interfaces.IWorld;
import View.Scene3D;

public class World implements IWorld
{
    private static List<Material> _availableMaterials = new ArrayList<Material>();
    public Cell _worldCurrentValues[][][];
    public Cell _worldOldValues[][][]; // needed for double-buffering
    private static World _instance = new World(
        Scene3D.getScene( Controller.MainWindow._sceneCanvas ) );
    private IScene3D _scene;
    private double _worldInitTemp = 20.0;
    private IHeatAndVaporsConductor _heatConducter = new HeatConducterWithConvection();
    private IFireConductor _fireConducter = new FireConducter();

    // private VaporConducter _vaporConducter = new VaporConducter();
    private void initMaterials()
    {
        get_availableMaterials().add(
            new Material( "Wood", EnvSettings.WOOD_COLOR, EnvSettings.WOOD_SPECIFIC_HEAT,
                EnvSettings.WOOD_TRANSPARENCY, EnvSettings.WOOD_THERMAL_CONDUCTIVITY,
                EnvSettings.WOOD_FLAME_POINT, EnvSettings.WOOD_DURABILITY, true, EnvSettings.WOOD_SMOKE_CAPACITY ) );
        get_availableMaterials().add(
            new Material( "Air", Scene3D.setBlue( _worldInitTemp ), EnvSettings.AIR_SPECIFIC_HEAT,
                EnvSettings.AIR_TRANSPARENCY, EnvSettings.AIR_THERMAL_CONDUCTIVITY,
                EnvSettings.VAPOR_FLAME_POINT, (int)( EnvSettings.INFINITIVE ), false , EnvSettings.AIR_SMOKE_CAPACITY) );
        get_availableMaterials().add(
            new Material( "Metal", EnvSettings.METAL_COLOR, EnvSettings.METAL_SPECIFIC_HEAT,
                EnvSettings.METAL_TRANSPARENCY, EnvSettings.METAL_THERMAL_CONDUCTIVITY,
                EnvSettings.INFINITIVE, (int)( EnvSettings.INFINITIVE ), false, EnvSettings.METAL_SMOKE_CAPACITY ) );
    }

    private static Material getMaterial( String materialName )
    {
        for( int i = 0; i < _availableMaterials.size(); ++i )
        {
            if( _availableMaterials.get( i ).get_name().equals( materialName ) )
            {
                return _availableMaterials.get( i );
            }
        }
        return null;
    }

    public void addBuildingPart( Point3d leftBottomBackCorner, Point3d size, String materialName,
            Scene3D scene ) throws ArrayIndexOutOfBoundsException
    {
        for( int j = (int)leftBottomBackCorner.y; j < leftBottomBackCorner.y + size.y; ++j )
        {
            for( int k = (int)leftBottomBackCorner.z; k < leftBottomBackCorner.z + size.z; ++k )
            {
                for( int i = (int)leftBottomBackCorner.x; i < leftBottomBackCorner.x + size.x; ++i )
                {
                    // TODO refactor this out soon.
                    Material mat = getMaterial( materialName );
                    _worldCurrentValues[ i ][ j ][ k ].set_material( mat );
                    _worldOldValues[ i ][ j ][ k ].set_material( mat );
                    int blockIndex = Helpers.WorldSceneMediator.changeWorldIndexToSceneIndex( i, j,
                        k );
                    // NOTE when we add, then delete, then add again, will it
                    // add same nodes
                    // many times?
                    scene.addNewBlockToScene( mat, blockIndex );
                }
            }
        }
    }

    public void restartTemperatures( Scene3D scene )
    {
        for( int i = 0; i < EnvSettings.getMAX_X(); ++i )
        {
            for( int j = 0; j < EnvSettings.getMAX_Y(); ++j )
            {
                for( int k = 0; k < EnvSettings.getMAX_Z(); ++k )
                {
                    _worldCurrentValues[ i ][ j ][ k ].set_temp( _worldInitTemp );
                    _worldOldValues[ i ][ j ][ k ].set_temp( _worldInitTemp );
                    _scene.updateBlockWhileSimulation(
                        Helpers.WorldSceneMediator.changeWorldIndexToSceneIndex( i, j, k ),
                        _worldCurrentValues[ i ][ j ][ k ].get_temp(),
                        _worldCurrentValues[ i ][ j ][ k ].get_material(),
                        _worldCurrentValues[ i ][ j ][ k ] );
                }
            }
        }
    }

    private void initWorld( Scene3D scene )
    {
        double airMass = 2.0;
        for( int i = 0; i < EnvSettings.getMAX_X(); ++i )
        {
            for( int j = 0; j < EnvSettings.getMAX_Y(); ++j )
            {
                for( int k = 0; k < EnvSettings.getMAX_Z(); ++k )
                {
                    _worldCurrentValues[ i ][ j ][ k ] = new Cell( getMaterial( "Air" ),
                        _worldInitTemp, airMass, 0 );
                    _worldOldValues[ i ][ j ][ k ] = new Cell( getMaterial( "Air" ),
                        _worldInitTemp, airMass, 0 );
                }
            }
        }
        Material defaultMaterial = getMaterial( "Air" );
        assert ( defaultMaterial != null );
        scene.createdWorldRepresentation( defaultMaterial, EnvSettings.getMAX_X(),
            EnvSettings.getMAX_Y(), EnvSettings.getMAX_Z() );
    }

    public static World getWorld( Scene3D scene )
    {
        return _instance;
    }

    private World( Scene3D scene )
    {
        // FIXME get this code out of c-tor; let someone else call it
        _scene = scene;
        _worldCurrentValues = new Cell[ EnvSettings.getMAX_X() ][ EnvSettings.getMAX_Y() ][ EnvSettings
                .getMAX_Z() ];
        set_worldOldValues( new Cell[ EnvSettings.getMAX_X() ][ EnvSettings.getMAX_Y() ][ EnvSettings
                .getMAX_Z() ] );
        initMaterials();
        initWorld( scene );
    }

    public List<Material> get_availableMaterials()
    {
        return _availableMaterials;
    }

    public void set_worldOldValues( Cell _worldOldValues[][][] )
    {
        this._worldOldValues = _worldOldValues;
    }

    public void clearMaterials( Scene3D scene )
    {
        Material air = getMaterial( "Air" );
        for( int i = 0; i < _worldCurrentValues.length; ++i )
        {
            for( int j = 0; j < _worldCurrentValues[ i ].length; ++j )
            {
                for( int k = 0; k < _worldCurrentValues[ i ][ j ].length; ++k )
                {
                    _worldCurrentValues[ i ][ j ][ k ].set_material( air );
                    _worldOldValues[ i ][ j ][ k ].set_material( air );
                    scene.addNewBlockToScene( air,
                        Helpers.WorldSceneMediator.changeWorldIndexToSceneIndex( i, j, k ) );
                }
            }
        }
    }

    public static class CellIndex
    {
        public CellIndex( int x, int y, int z )
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        int x;
        int y;
        int z;
    }

    private List<CellIndex> getNeighbours( CellIndex index )
    {
        // NOTE optimization ideas TODO
        // * get rid of this CellIndex stuff (replace with 1D index)
        // * try a != && != instead of one % !=
        // * HACK maybe we could precompute or memoize this stuff?
        List<CellIndex> neighbours = new ArrayList<World.CellIndex>();
        if( index.x % EnvSettings.getMAX_X() != 0 )
        {
            neighbours.add( new CellIndex( index.x - 1, index.y, index.z ) );
        }
        if( index.x % EnvSettings.getMAX_X() != EnvSettings.getMAX_X() - 1 )
        {
            neighbours.add( new CellIndex( index.x + 1, index.y, index.z ) );
        }
        if( index.y % EnvSettings.getMAX_Y() != 0 )
        {
            neighbours.add( new CellIndex( index.x, index.y - 1, index.z ) );
        }
        if( index.y % EnvSettings.getMAX_Y() != EnvSettings.getMAX_Y() - 1 )
        {
            neighbours.add( new CellIndex( index.x, index.y + 1, index.z ) );
        }
        if( index.z % EnvSettings.getMAX_Z() != 0 )
        {
            neighbours.add( new CellIndex( index.x, index.y, index.z - 1 ) );
        }
        if( index.z % EnvSettings.getMAX_Z() != EnvSettings.getMAX_Z() - 1 )
        {
            neighbours.add( new CellIndex( index.x, index.y, index.z + 1 ) );
        }
        return neighbours;
    }

    public void setStartOfFire( int x, int y, int z )
    {
        _worldCurrentValues[ x ][ y ][ z ].set_temp( EnvSettings.START_OF_FIRE_TEMP );
        _worldOldValues[ x ][ y ][ z ].set_temp( EnvSettings.START_OF_FIRE_TEMP );
        _scene.updateBlockWhileSimulation(
            Helpers.WorldSceneMediator.changeWorldIndexToSceneIndex( x, y, z ),
            _worldCurrentValues[ x ][ y ][ z ].get_temp(),
            _worldCurrentValues[ x ][ y ][ z ].get_material(), _worldCurrentValues[ x ][ y ][ z ] );
        _scene.markStartOfFire( Helpers.WorldSceneMediator.changeWorldIndexToSceneIndex( x, y, z ),
            _worldCurrentValues[ x ][ y ][ z ].get_material() );
    }

    private void updateOldValues()
    {
        for( int i = 0; i < EnvSettings.getMAX_X(); ++i )
        {
            for( int j = 0; j < EnvSettings.getMAX_Y(); ++j )
            {
                for( int k = 0; k < EnvSettings.getMAX_Z(); ++k )
                {
                    _worldOldValues[ i ][ j ][ k ] = _worldCurrentValues[ i ][ j ][ k ];
                }
            }
        }
    }

    ProfileSample profileOldVals = new ProfileSample( "simulation: back-buffering" );
    ProfileSample profileFullUpdate = new ProfileSample( "simulation: full update" );
    ProfileSample profileConductHeat = new ProfileSample( "simulation: conduct heat" );
    ProfileSample profileSpreadFire = new ProfileSample( "simulation: spread fire" );
    ProfileSample profileUpdateJ3D = new ProfileSample( "simulation: update block while simulation" );

    public void simulateHeatConduction()
    {
        profileOldVals.start();
        updateOldValues();
        profileOldVals.stop();
        profileFullUpdate.start();
        for( int i = 0; i < EnvSettings.getMAX_X(); ++i )
        {
            for( int j = 0; j < EnvSettings.getMAX_Y(); ++j )
            {
                for( int k = 0; k < EnvSettings.getMAX_Z(); ++k )
                {
                    // NOTE possible optimizations TODO
                    // * cellId goes away
                    // * replace 3D arrays with 1D
                    // * update rendering after simulation loop
                    CellIndex cellId = new CellIndex( i, j, k );
                    // profileConductHeat.start();
                    _heatConducter.conductHeat( _worldCurrentValues[ i ][ j ][ k ],
                        _worldCurrentValues, getNeighbours( cellId ),
                        _worldOldValues[ i ][ j ][ k ], _worldOldValues, cellId );
                    // profileConductHeat.stop();
                    // profileSpreadFire.start();
		    
		    //FIXME FIXME FIXME Double-buffering, anyone?
                    _fireConducter.spreadFire( _worldCurrentValues[ i ][ j ][ k ],
                        getNeighbours( cellId ), _worldCurrentValues );
                    // profileSpreadFire.stop();
                    // profileUpdateJ3D.start();
                    _scene.updateBlockWhileSimulation(
                        Helpers.WorldSceneMediator.changeWorldIndexToSceneIndex( i, j, k ),
                        _worldCurrentValues[ i ][ j ][ k ].get_temp(),
                        _worldCurrentValues[ i ][ j ][ k ].get_material(),
                        _worldCurrentValues[ i ][ j ][ k ] );
                    // profileUpdateJ3D.stop();
                    // _vaporConducter.conductVepors( _worldCurrentValues[ i ][
                    // j ][ k ],
                    // _worldCurrentValues, getNeighbours( cellId ), cellId );
                    // System.out.println( "value " + _worldCurrentValues[ i ][
                    // j ][ k ] );
                }
            }
        }
        profileFullUpdate.stop();
        // gosh, Java sucks so much...
    }
}
