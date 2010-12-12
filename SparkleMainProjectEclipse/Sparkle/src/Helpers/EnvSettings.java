package Helpers;

import javax.vecmath.Color3f;

//all needed constans put here
public class EnvSettings
{
    private static int MAX_LENGTH = 15;
    public final static int HOW_MANY_GARBAGE_BRANCH_GROUP_CHILDREN = 4;
    public final static Color3f AIR_COLOR2 = new Color3f( 0f, 0f, 50f );
    public final static Color3f AIR_COLOR = new Color3f( 0f, 0f, 50f );
    public final static Color3f BACKGROUND_COLOR = new Color3f( 173f, 216f, 230f );
    // public final static Color3f WOOD_COLOR = new Color3f( 255, 0, 0 );
    public final static double AIR_TRANSPARENCY = 0.9;
    public final static double WOOD_TRANSPARENCY = 0.5;
    public final static double METAL_TRANSPARENCY = 0.5;
    public static final int WOOD_SPECIFIC_HEAT = 1240;
    public static final int AIR_SPECIFIC_HEAT = 1;
    public static final int METAL_SPECIFIC_HEAT = 20;
    public static final double CELL_REPRESENTATION_LENGTH = 0.1;
    // public static final Color3f WOOD_COLOR = new Color3f( 139, 0, 0 );
    public static final Color3f WOOD_COLOR = new Color3f( 205, 133, 63 );
    public static final Color3f METAL_COLOR = new Color3f( 255, 255, 0 );
    public static final double FIRE_TEMP = 100;
    public static final double START_OF_FIRE_TEMP = 150000;

    public static void setMAX_LENGTH( int mAX_LENGTH )
    {
        MAX_LENGTH = mAX_LENGTH;
    }

    public static int getMAX_LENGTH()
    {
        return MAX_LENGTH;
    }
}
