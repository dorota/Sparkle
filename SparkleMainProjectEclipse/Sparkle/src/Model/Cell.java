package Model;

import Helpers.EnvSettings;
import Helpers.EnvSettings.CellState;

/**
 * @author Dorota
 * 
 * 
 */
public class Cell
{
    private Material _material;
    private double _mass;
    private double _temp;
    private EnvSettings.CellState _cellState;
    final double _cellLenfth = 0.5;

    // final double _flameTemp;
    @Override
    public String toString()
    {
        return _material + " temp " + _temp;
    }

    // mass is not needed; may be calculate from material's density and cell's V
    public Cell( Material material, double temp, double mass )
    {
        _material = material;
        _temp = temp;
        _mass = mass;
        _cellState = CellState.NEUTRAL;
    }

    public Material get_material()
    {
        return _material;
    }

    public void set_material( Material _material )
    {
        this._material = _material;
    }

    public double get_mass()
    {
        return _mass;
    }

    public void set_mass( double _mass )
    {
        this._mass = _mass;
    }

    public double get_temp()
    {
        return _temp;
    }

    public void set_temp( double _temp )
    {
        this._temp = _temp;
    }
}
