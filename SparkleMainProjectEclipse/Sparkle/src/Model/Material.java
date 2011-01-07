package Model;

import javax.vecmath.Color3f;

import Helpers.EnvSettings;

public class Material
{
    private String _name;
    private Color3f _color;
    private double _heatedAirTransparency;
    private int _howLongItBurns;
    private boolean _fuel;
    
    //how much smoke we can push into a cell made of this material
    //FIXME what unit?
    private double _smokeCapacity;

    //TODO how much smoke it produces per second, or sth.

    private double _density; // gestosc
    private double _thermalConductivity;

    private double _specificHeat;
    private float _transparency;
    
    // for non - fuel materials infinite
    private float _flamePoint;

    //FIXME download Lombok and get rid of this Java accessor bloat...
    public double get_smokeCapacity()
    {
	return _smokeCapacity;
    }

    public void set_smokeCapacity(double smokeCapacity)
    {
	_smokeCapacity = smokeCapacity;
    }

    public boolean is_fuel()
    {
        return _fuel;
    }

    public final void set_fuel( boolean _fuel )
    {
        this._fuel = _fuel;
    }

    public int get_howLongItBurns()
    {
        return _howLongItBurns;
    }

    public final void set_howLongItBurns( int _howLongItBurns )
    {
        this._howLongItBurns = _howLongItBurns;
    }

    public double get_heatedAirTransparency()
    {
        return _heatedAirTransparency;
    }

    public void set_heatedAirTransparency( double _heatedAirTransparency )
    {
        this._heatedAirTransparency = _heatedAirTransparency;
    }


    public float get_flamePoint()
    {
        return _flamePoint;
    }

    public final void set_flamePoint( float _flamePoint )
    {
        this._flamePoint = _flamePoint;
    }


    public double get_thermalConductivity()
    {
        return _thermalConductivity;
    }

    public final void set_thermalConductivity( double _thermalConductivity )
    {
        this._thermalConductivity = _thermalConductivity;
    }

    public double get_density()
    {
        return _density;
    }

    public void set_density( double _density )
    {
        this._density = _density;
    }

    public Material( String name, Color3f color, double specificHeat, double transparency,
            double thermalConductivity, double flamePoint, int howLongItBurns, boolean fuel,
	    double smokeCapacity)
    {
        set_fuel( fuel );
        set_howLongItBurns( howLongItBurns );
        set_flamePoint( _flamePoint );
        set_name( name );
        set_color( color );
        set_specificHeat( specificHeat );
        set_transparency( transparency );
        set_thermalConductivity( thermalConductivity );
	set_smokeCapacity(smokeCapacity);
        if( name.equals( "Air" ) )
        {
            _heatedAirTransparency = EnvSettings.AIR_TEMP_TRANSPARENCY;
        }
    }

    public final void set_name( String _name )
    {
        this._name = _name;
    }

    public String get_name()
    {
        return _name;
    }

    public final void set_color( Color3f _color )
    {
        this._color = _color;
    }

    public Color3f get_color()
    {
        return _color;
    }

    public final void set_specificHeat( double _specificHeat )
    {
        this._specificHeat = _specificHeat;
    }

    public double get_specificHeat()
    {
        return _specificHeat;
    }

    public final void set_transparency( double _transparency )
    {
        this._transparency = (float)( _transparency );
    }

    public float get_transparency()
    {
        return _transparency;
    }

    @Override
    public String toString()
    {
        return _name;
    }
}