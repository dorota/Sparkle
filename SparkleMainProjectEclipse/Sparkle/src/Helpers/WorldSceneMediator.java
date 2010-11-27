package Helpers;

import Logic.World;

//not tested - don't know if it works
public class WorldSceneMediator
{
    public static int changeWorldIndexToSceneIndex( int worldX, int worldY, int worldZ )
    {
        return worldY * ( worldX * worldZ ) + worldZ * worldX + worldX;
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
    };
}
