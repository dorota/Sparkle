/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Helpers;

/**
 * 
 * @author TeMPOraL
 */
public class MathStuff
{
    public static float lerp( float from, float to, float scale )
    {
        return from * ( 1.0f - scale ) + to * scale;
    }

    public static float clamp( float what, float min, float max )
    {
        return Math.max( Math.min( what, max ), min );
    }

    public static double clamp( double what, double min, double max )
    {
        return Math.max( Math.min( what, max ), min );
    }
}
