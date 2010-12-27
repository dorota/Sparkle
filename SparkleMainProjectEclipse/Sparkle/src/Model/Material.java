package Model;

import javax.vecmath.Color3f;

import Helpers.EnvSettings;

public class Material
{
    private String _name;
    private Color3f _color;
    private double _heatedAirTransparency;
    private int _howLongItBurns;

    public int get_howLongItBurns()
    {
        return _howLongItBurns;
    }

    public void set_howLongItBurns( int _howLongItBurns )
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

    private double _specificHeat;
    private float _transparency;
    // for non - fuel materials infinite
    private float _flamePoint;

    public float get_flamePoint()
    {
        return _flamePoint;
    }

    public void set_flamePoint( float _flamePoint )
    {
        this._flamePoint = _flamePoint;
    }

    private double _density; // gestosc
    private double _thermalConductivity;

    public double get_thermalConductivity()
    {
        return _thermalConductivity;
    }

    public void set_thermalConductivity( double _thermalConductivity )
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
            double thermalConductivity, double flamePoint, int howLongItBurns )
    {
        set_howLongItBurns( howLongItBurns );
        set_flamePoint( _flamePoint );
        set_name( name );
        set_color( color );
        set_specificHeat( specificHeat );
        set_transparency( transparency );
        set_thermalConductivity( thermalConductivity );
        if( name.equals( "Air" ) )
        {
            _heatedAirTransparency = EnvSettings.AIR_TEMP_TRANSPARENCY;
        }
    }

    public void set_name( String _name )
    {
        this._name = _name;
    }

    public String get_name()
    {
        return _name;
    }

    public void set_color( Color3f _color )
    {
        this._color = _color;
    }

    public Color3f get_color()
    {
        return _color;
    }

    public void set_specificHeat( double _specificHeat )
    {
        this._specificHeat = _specificHeat;
    }

    public double get_specificHeat()
    {
        return _specificHeat;
    }

    public void set_transparency( double _transparency )
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