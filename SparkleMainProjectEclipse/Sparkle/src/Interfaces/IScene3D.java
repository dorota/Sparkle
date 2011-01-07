package Interfaces;

import java.util.List;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import Model.Cell;
import Model.Material;

public interface IScene3D
{
    void addNewBlock( Model.Material material, Vector3d startCoordinates, Point3d blocSize );

    void markStartOfFire( int blockIndex, Material material );

    void updateBlockWhileSimulation( int blockIndex, double temp, Material material, Cell cell );

    void createdWorldRepresentation( Model.Material defaultMaterial, int worldX, int worldY,
            int worldZ );

    List<Vector3d> get_startsOfBlocks();
}
