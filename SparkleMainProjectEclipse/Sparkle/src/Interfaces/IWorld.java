package Interfaces;

import java.util.List;

import javax.vecmath.Point3d;

import Model.Material;
import View.Scene3D;

public interface IWorld
{
    void addBuildingPart( Point3d leftBottomBackCorner, Point3d size, String materialName,
            Scene3D scene );

    void restartTemperatures( Scene3D scene );

    List<Material> get_availableMaterials();

    void setStartOfFire( int x, int y, int z );

    void simulateHeatConduction();

    void clearMaterials( Scene3D scene );
}
