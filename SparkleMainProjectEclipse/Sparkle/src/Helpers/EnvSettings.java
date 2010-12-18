package Helpers;

import javax.vecmath.Color3f;

//all needed constans put here
public class EnvSettings
{
    private static int MAX_X = 8;
    private static int MAX_Z = 8;
    private static int MAX_Y = 8;
    public final static int HOW_MANY_GARBAGE_BRANCH_GROUP_CHILDREN = 4;
    public final static Color3f AIR_COLOR2 = new Color3f( 0f, 0f, 50f );
    public final static Color3f AIR_COLOR = new Color3f( 0f, 0f, 255f );
    public final static Color3f BACKGROUND_COLOR = new Color3f( 173f, 216f, 230f );
    // public final static Color3f WOOD_COLOR = new Color3f( 255, 0, 0 );
    // ///////////////////////material transparency
    public final static double AIR_TRANSPARENCY = 0.9;
    public final static double WOOD_TRANSPARENCY = 0.7;
    public final static double METAL_TRANSPARENCY = 0.7;
    // ///////////////////end of material transparency
    public static final int WOOD_SPECIFIC_HEAT = 2400;
    public static final double AIR_SPECIFIC_HEAT = 1000;
    public static final int METAL_SPECIFIC_HEAT = 10; // should be 2
    public static final double CELL_REPRESENTATION_LENGTH = 0.1;
    public static final Color3f WOOD_COLOR = new Color3f( 0.5f, 0.25f, 0 );
    // public static final Color3f WOOD_COLOR = new Color3f( 205, 133, 63 );
    public static final Color3f METAL_COLOR = new Color3f( 255, 255, 0 );
    public static final double FIRE_TEMP = 50;
    public static final double START_OF_FIRE_TEMP = 8000000;
    // //////////////////// material conductivities
    public static final double AIR_THERMAL_CONDUCTIVITY = 0.025;
    public static final double WOOD_THERMAL_CONDUCTIVITY = 0.17;
    public static final double METAL_THERMAL_CONDUCTIVITY = 5;
    public static final double CONSTANT_ENERGY_FACTOR = 0.002;
    // ///////////////////end of material conductivities
    public static final int TOP_NEIGHBOUR = 0;
    public static final int BOTTOM_NEIGBOUR = 1;
    public static final int SIDE_NEIGHBOUR = 2;
    public static final int DOESNT_MATTER = 3;

    public static int getMAX_X()
    {
        return MAX_X;
    }

    public static void setMAX_X( int mAX_X )
    {
        MAX_X = mAX_X;
    }

    public static int getMAX_Z()
    {
        return MAX_Z;
    }

    public static void setMAX_Z( int mAX_Z )
    {
        MAX_Z = mAX_Z;
    }

    public static int getMAX_Y()
    {
        return MAX_Y;
    }

    public static void setMAX_Y( int mAX_Y )
    {
        MAX_Y = mAX_Y;
    }
}
