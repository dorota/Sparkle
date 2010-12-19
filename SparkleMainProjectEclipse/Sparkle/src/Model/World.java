package Model;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3d;

import Helpers.EnvSettings;
import View.Scene3D;

public class World
{
    private static List<Material> _availableMaterials = new ArrayList<Material>();
    public Cell _worldCurrentValues[][][];
    public Cell _worldOldValues[][][]; // needed for double-buffering
    private static World _instance = new World(
        Scene3D.getScene( Controller.MainWindow._sceneCanvas ) );
    private Scene3D _scene;
    private double _worldInitTemp = 20.0;
    private HeatConducterWithConvection _heatConducter = new HeatConducterWithConvection();

    private void initMaterials()
    {
        get_availableMaterials().add(
            new Material( "Wood", EnvSettings.WOOD_COLOR, EnvSettings.WOOD_SPECIFIC_HEAT,
                EnvSettings.WOOD_TRANSPARENCY, EnvSettings.WOOD_THERMAL_CONDUCTIVITY ) );
        get_availableMaterials().add(
            new Material( "Air", Scene3D.setBlue( _worldInitTemp ), EnvSettings.AIR_SPECIFIC_HEAT,
                EnvSettings.AIR_TRANSPARENCY, EnvSettings.AIR_THERMAL_CONDUCTIVITY ) );
        get_availableMaterials().add(
            new Material( "Metal", EnvSettings.METAL_COLOR, EnvSettings.METAL_SPECIFIC_HEAT,
                EnvSettings.METAL_TRANSPARENCY, EnvSettings.METAL_THERMAL_CONDUCTIVITY ) );
    }

    public static Material getMaterial( String materialName )
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
                    Material mat = getMaterial( materialName );
                    _worldCurrentValues[ i ][ j ][ k ].set_material( mat );
                    _worldOldValues[ i ][ j ][ k ].set_material( mat );
                    int blockIndex = Helpers.WorldSceneMediator.changeWorldIndexToSceneIndex( i, j,
                        k );
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
                    // System.out.println( "material  under air name " +
                    // getMaterial( "Air" ) );
                    _worldCurrentValues[ i ][ j ][ k ].set_temp( _worldInitTemp );
                    _worldOldValues[ i ][ j ][ k ].set_temp( _worldInitTemp );
                    _scene.updateBlockWhileSimulation(
                        Helpers.WorldSceneMediator.changeWorldIndexToSceneIndex( i, j, k ),
                        _worldCurrentValues[ i ][ j ][ k ].get_temp(),
                        _worldCurrentValues[ i ][ j ][ k ].get_material() );
                }
            }
        }
    }

    public void initWorld( Scene3D scene )
    {
        for( int i = 0; i < EnvSettings.getMAX_X(); ++i )
        {
            for( int j = 0; j < EnvSettings.getMAX_Y(); ++j )
            {
                for( int k = 0; k < EnvSettings.getMAX_Z(); ++k )
                {
                    // System.out.println( "material  under air name " +
                    // getMaterial( "Air" ) );
                    double airMass = 2.0;
                    _worldCurrentValues[ i ][ j ][ k ] = new Cell( getMaterial( "Air" ),
                        _worldInitTemp, airMass );
                    _worldOldValues[ i ][ j ][ k ] = new Cell( getMaterial( "Air" ),
                        _worldInitTemp, airMass );
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
        _scene = scene;
        _worldCurrentValues = new Cell[ EnvSettings.getMAX_X() ][ EnvSettings.getMAX_Y() ][ EnvSettings
                .getMAX_Z() ];
        set_worldOldValues( new Cell[ EnvSettings.getMAX_X() ][ EnvSettings.getMAX_Y() ][ EnvSettings
                .getMAX_Z() ] );
        initMaterials();
        initWorld( scene );
    }

    public void set_availableMaterials( List<Material> _availableMaterials )
    {
        this._availableMaterials = _availableMaterials;
    }

    public List<Material> get_availableMaterials()
    {
        return _availableMaterials;
    }

    public void set_worldOldValues( Cell _worldOldValues[][][] )
    {
        this._worldOldValues = _worldOldValues;
    }

    public Cell[][][] get_worldOldValues()
    {
        return _worldOldValues;
    }

    public Cell[][][] get_worldCurrentValues()
    {
        return _worldCurrentValues;
    }

    public void set_worldCurrentValues( Cell[][][] currentValues )
    {
        _worldCurrentValues = currentValues;
        _worldOldValues = currentValues;
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

    public List<CellIndex> getNeighbours( CellIndex index )
    {
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
            _worldCurrentValues[ x ][ y ][ z ].get_material() );
        _scene.markStartOfHeatConduction(
            Helpers.WorldSceneMediator.changeWorldIndexToSceneIndex( x, y, z ),
            _worldCurrentValues[ x ][ y ][ z ].get_material() );
        // for( int i = 0; i < EnvSettings.getMAX_LENGTH(); ++i )
        // {
        // for( int j = 0; j < EnvSettings.getMAX_LENGTH(); ++j )
        // {
        // for( int k = 0; k < EnvSettings.getMAX_LENGTH(); ++k )
        // {
        // System.out.println( _worldCurrentValues[ i ][ j ][ k ] );
        // }
        // }
        // }
        // System.out.println( "world po inicie" );
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

    public void simulateHeatConduction()
    {
        // System.out.println( "material drugiego rzedu "
        // + _worldCurrentValues[ 2 ][ 1 ][ 4 ].get_material() );
        // System.out.println( "print all scene" );
        // for( int i = 0; i < EnvSettings.getMAX_X(); ++i )
        // {
        // for( int j = 0; j < EnvSettings.getMAX_Y(); ++j )
        // {
        // for( int k = 0; k < EnvSettings.getMAX_Z(); ++k )
        // {
        // System.out
        // .println( "cell " + i + " " + j + " " + _worldOldValues[ i ][ j ][ k
        // ] );
        // }
        // }
        // }
        updateOldValues();
        System.out.println( "done1" );
        for( int i = 0; i < EnvSettings.getMAX_X(); ++i )
        {
            for( int j = 0; j < EnvSettings.getMAX_Y(); ++j )
            {
                for( int k = 0; k < EnvSettings.getMAX_Z(); ++k )
                {
                    CellIndex cellId = new CellIndex( i, j, k );
                    _heatConducter.conductHeat( _worldCurrentValues[ i ][ j ][ k ],
                        _worldCurrentValues, getNeighbours( cellId ),
                        _worldOldValues[ i ][ j ][ k ], _worldOldValues, cellId );
                    _scene.updateBlockWhileSimulation(
                        Helpers.WorldSceneMediator.changeWorldIndexToSceneIndex( i, j, k ),
                        _worldCurrentValues[ i ][ j ][ k ].get_temp(),
                        _worldCurrentValues[ i ][ j ][ k ].get_material() );
                    // System.out.println( "value " + _worldCurrentValues[ i ][
                    // j ][ k ] );
                }
            }
        }
        // System.out.println( "done" );
    }
}
