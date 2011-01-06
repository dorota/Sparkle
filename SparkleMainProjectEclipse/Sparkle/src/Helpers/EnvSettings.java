package Helpers;

import javax.vecmath.Color3f;

//all needed constans put here
public class EnvSettings
{
    private static int MAX_X = 13;
    private static int MAX_Z = 13;
    private static int MAX_Y = 13;
    public final static int HOW_MANY_GARBAGE_BRANCH_GROUP_CHILDREN = 5;
    public final static Color3f AIR_COLOR2 = new Color3f( 0f, 0f, 50f );
    public final static Color3f AIR_COLOR = new Color3f( 0f, 0f, 255f );
    public final static Color3f BACKGROUND_COLOR = new Color3f( 173f, 216f, 230f );
    // public final static Color3f WOOD_COLOR = new Color3f( 255, 0, 0 );
    public final static double AIR_TEMP_TRANSPARENCY = 0.9;
    // ///////////////////////material transparency
    public final static double AIR_TRANSPARENCY = 0.9;
    public final static double WOOD_TRANSPARENCY = 0.5;
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
    public static final double START_OF_FIRE_TEMP = 40000;
    // //////////////////// material conductivities
    public static final double AIR_THERMAL_CONDUCTIVITY = 0.15; // 0.025 real
    // value
    public static final double WOOD_THERMAL_CONDUCTIVITY = 1.7;
    public static final double METAL_THERMAL_CONDUCTIVITY = 10;
    public static final double CONSTANT_ENERGY_FACTOR = 0.002;
    // ///////////////////end of material conductivities
    public static final int TOP_NEIGHBOUR = 0;
    public static final int BOTTOM_NEIGBOUR = 1;
    public static final int SIDE_NEIGHBOUR = 2;
    public static final int DOESNT_MATTER = 3;
    // regular display mode colors
    public static final Color3f SMOKE_COLOR = new Color3f( 0.5f, 0.5f, 0.5f );
    public static final Color3f FIRE_COLOR = new Color3f( 1.0f, 0.0f, 0.0f );
    public static final double INFINITIVE = 10000000.0;
    public static final double WOOD_FLAME_POINT = 100.0;
    public static final double VAPOR_FLAME_POINT = 50.0;
    // how long wood burns, before its completely burnt
    public static final int WOOD_DURABILITY = 20;
    public static final double MIN_VAPORS_TO_FIRE = 0.6;
    public static final double MAX_VAPORS_TO_FIRE = 0.95;
    // ///////////////////user control constants
    public static final double MOUSE_X_FACTOR = -0.15;
    public static final double MOUSE_Y_FACTOR = -0.15;

    public enum CellState
    {
        NEUTRAL, ON_FIRE, FIRED, SMOKE
    };

    public enum DisplayMode
    {
        FIRE, SMOKE, TEMPERATURE
    };

    public enum FileChooserAction
    {
        SAVE_STATES, SAVE_TEMPERATURE, READ_BUILDING_FROM_FILE
    };

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
