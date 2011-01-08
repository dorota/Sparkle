package Interfaces;

import java.util.List;

import Model.Cell;
import Model.World;

public interface IFireConductor
{
    void spreadFire( Cell cell, List<World.CellIndex> cellNeighboours, Cell[][][] _world );
}
