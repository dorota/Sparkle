package Interfaces;

import java.util.List;

import Model.Cell;
import Model.World;
import Model.World.CellIndex;

public interface IHeatAndVaporsConductor
{
    void conductHeat( Cell cell, Cell worldCurrentValues[][][],
            List<World.CellIndex> cellNeighboours, Cell oldValue, Cell worldOldValues[][][],
            CellIndex cellId );

    double calculateEnergyFlow( Cell cell, Cell neigh, int whichNighbour );

    void exchangeEnergy( Cell cell, Cell neigh, double energy );
}
