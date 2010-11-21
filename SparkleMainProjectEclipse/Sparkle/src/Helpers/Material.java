package Helpers;

import javax.vecmath.Color3f;

public class Material
{
    public String _name;
    public Color3f _color;
    public double _specificHeat;
    public double _transparency;

    public Material( String name, Color3f color, double specificHeat, double transparency )
    {
        _name = name;
        _color = color;
        _specificHeat = specificHeat;
        _transparency = transparency;
    }
}