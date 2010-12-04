package Helpers;

import javax.vecmath.Vector3d;

import View.Scene3D;

//not tested - don't know if it works
public class WorldSceneMediator
{
    public static int changeWorldIndexToSceneIndex( int worldX, int worldY, int worldZ )
    {
        return worldY * ( EnvSettings.getMAX_LENGTH() * EnvSettings.getMAX_LENGTH() ) + worldZ
                * EnvSettings.getMAX_LENGTH() + worldX + Scene3D.get_contentsOffset();
    }

    public static WorldIndex changeSceneIndexToWorldIndex( int i )
    {
        WorldIndex index = new WorldIndex();
        index.y = i / ( EnvSettings.getMAX_LENGTH() * EnvSettings.getMAX_LENGTH() );
        int onTheFloor = i % ( EnvSettings.getMAX_LENGTH() * EnvSettings.getMAX_LENGTH() );
        index.z = onTheFloor / EnvSettings.getMAX_LENGTH();
        index.x = onTheFloor % EnvSettings.getMAX_LENGTH();
        return index;
    }

    public static Vector3d changeWorldPlacementToScenePlacement( int x, int y, int z,
            double blockSize )
    {
        Vector3d offset = new Vector3d( -0.25, -0.25, 0 );
        Vector3d blockPlacement = new Vector3d( 0, 0, 0 );
        blockPlacement.x = offset.x + x * blockSize + blockSize / 2.0;
        blockPlacement.y = offset.y + y * blockSize + blockSize / 2.0;
        blockPlacement.z = offset.z + z * blockSize + blockSize / 2.0;
        return blockPlacement;
    }

    public static class WorldIndex
    {
        public int x;
        public int y;
        public int z;

        @Override
        public String toString()
        {
            return "[ " + x + " " + y + " " + z + " ]";
        }

        @Override
        public boolean equals( Object o )
        {
            if( o == this )
            {
                return true;
            }
            if( !( o instanceof WorldIndex ) )
            {
                return false;
            }
            WorldIndex wi = (WorldIndex)o;
            return wi.x == this.x && wi.y == this.y && wi.z == this.z;
        }
    };
}
