package Model;

import javax.vecmath.Color3f;

public class Material
{
    private String _name;
    private Color3f _color;
    private double _specificHeat;
    private double _transparency;
    private double _density; // gestosc

    public double get_density()
    {
        return _density;
    }

    public void set_density( double _density )
    {
        this._density = _density;
    }

    public Material( String name, Color3f color, double specificHeat, double transparency )
    {
        set_name( name );
        set_color( color );
        set_specificHeat( specificHeat );
        set_transparency( transparency );
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
        this._transparency = _transparency;
    }

    public double get_transparency()
    {
        return _transparency;
    }

    @Override
    public String toString()
    {
        return _name;
    }
}