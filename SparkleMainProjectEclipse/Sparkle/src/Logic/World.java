package Logic;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Color3f;

public class World
{
    public List<Material> _availableMaterials = new ArrayList<Material>();
    private Cell _worldCurrentValues[][][];
    private Cell _worldOldValues[][][]; // needed for double-buffering

    private void initMaterials()
    {
        _availableMaterials
                .add( new Material( "Wood", new Color3f( 1.0f, 0.0f, 0.0f ), 1240, 0.8 ) );
        _availableMaterials.add( new Material( "Air", new Color3f( 0.0f, 0.0f, 1.0f ), 1, 0.9 ) );
    }

    public World()
    {
        initMaterials();
    }
}
