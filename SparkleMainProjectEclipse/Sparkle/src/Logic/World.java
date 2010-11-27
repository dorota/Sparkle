package Logic;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Color3f;

public class World
{
    private List<Material> _availableMaterials = new ArrayList<Material>();
    private Cell _worldCurrentValues[][][];
    private Cell _worldOldValues[][][]; // needed for double-buffering
    private static int MAX_LENGTH = 50;

    private void initMaterials()
    {
        get_availableMaterials().add(
            new Material( "Wood", new Color3f( 1.0f, 0.0f, 0.0f ), 1240, 0.8 ) );
        get_availableMaterials()
                .add( new Material( "Air", new Color3f( 0.0f, 0.0f, 1.0f ), 1, 0.9 ) );
    }

    private void initWorld()
    {
        int airId = 1;
        for( int i = 0; i < getMAX_LENGTH(); ++i )
        {
            for( int j = 0; j < getMAX_LENGTH(); ++j )
            {
                for( int k = 0; k < getMAX_LENGTH(); ++k )
                {
                    _worldCurrentValues[ i ][ j ][ k ] = new Cell( get_availableMaterials().get(
                        airId ), 20.0 );
                }
            }
        }
    }

    public World()
    {
        _worldCurrentValues = new Cell[ getMAX_LENGTH() ][ getMAX_LENGTH() ][ getMAX_LENGTH() ];
        set_worldOldValues( new Cell[ getMAX_LENGTH() ][ getMAX_LENGTH() ][ getMAX_LENGTH() ] );
        initMaterials();
        initWorld();
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
    }

    public static void setMAX_LENGTH( int mAX_LENGTH )
    {
        MAX_LENGTH = mAX_LENGTH;
    }

    public static int getMAX_LENGTH()
    {
        return MAX_LENGTH;
    }
}
