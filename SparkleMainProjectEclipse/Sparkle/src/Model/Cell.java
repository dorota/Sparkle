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
    private int _fireClock = 0;

    final double _cellLength = 0.5;
    private float _percentageOfVaporsInCell = 0.0f;

    private double _smoke; //how much smoke is in the cell?

    public int get_fireClock()
    {
        return _fireClock;
    }

    public void set_fireClock( int _fireClock )
    {
        this._fireClock = _fireClock;
    }

    public EnvSettings.CellState get_cellState()
    {
        return _cellState;
    }

    public void set_cellState( EnvSettings.CellState _cellState )
    {
        this._cellState = _cellState;
    }

    public float get_percentageOfVaporsInCell()
    {
        return _percentageOfVaporsInCell;
    }

    public void set_percentageOfVaporsInCell( float _percentageOfVaporsInCell )
    {
        this._percentageOfVaporsInCell = _percentageOfVaporsInCell;
    }

    // final double _flameTemp;
    @Override
    public String toString()
    {
        return _material + " temp " + _temp;
    }

    // mass is not needed; may be calculate from material's density and cell's V
    public Cell( Material material, double temp, double mass, double smoke )
    {
        _material = material;
        _temp = temp;
        _mass = mass;
        _cellState = CellState.NEUTRAL;
	_smoke = smoke;
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

    //beat me with a sledgehammer, but I really hate writing accessors...
    //TODO REFACTOR ME PLS BY INSTALLING LOMBOK
    public double get_smoke()
    {
	return _smoke;
    }

    public void set_smoke(double smoke)
    {
	_smoke = smoke;
    }
}
