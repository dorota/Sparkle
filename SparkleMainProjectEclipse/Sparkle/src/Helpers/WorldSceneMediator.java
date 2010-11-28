package Helpers;

import javax.vecmath.Vector3d;

import Model.World;

//not tested - don't know if it works
public class WorldSceneMediator
{
    public static int changeWorldIndexToSceneIndex( int worldX, int worldY, int worldZ )
    {
        return worldY * ( World.getMAX_LENGTH() * World.getMAX_LENGTH() ) + worldZ
                * World.getMAX_LENGTH() + worldX;
    }

    public static WorldIndex changeSceneIndexToWorldIndex( int i )
    {
        WorldIndex index = new WorldIndex();
        index.y = i / ( World.getMAX_LENGTH() * World.getMAX_LENGTH() );
        int onTheFloor = i % ( World.getMAX_LENGTH() * World.getMAX_LENGTH() );
        index.z = onTheFloor / World.getMAX_LENGTH();
        index.x = onTheFloor % World.getMAX_LENGTH();
        return index;
    }

    public static Vector3d changeWorldPlacementToScenePlacement( int x, int y, int z,
            double blockSize )
    {
        Vector3d offset = new Vector3d( 0, 0, 0 );
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
