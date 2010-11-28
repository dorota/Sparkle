package Helpers;

import Logic.World;

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
