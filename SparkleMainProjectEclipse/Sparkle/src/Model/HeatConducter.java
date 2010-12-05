package Model;

import java.util.List;

public class HeatConducter
{
    public static void conductHeat( Cell cell, Cell worldCurrentValues[][][],
            List<World.CellIndex> cellNeighboours )
    {
        for( int i = 0; i < cellNeighboours.size(); ++i )
        {
            World.CellIndex id = cellNeighboours.get( i );
            Cell neigh = worldCurrentValues[ id.x ][ id.y ][ id.z ];
            double energy = calculateEnergyFlow( cell, neigh );
            exchangeEnergy( cell, neigh, energy );
        }
    }

    public static double calculateEnergyFlow( Cell cell, Cell neigh )
    {
        double cellHeatCapacity = cell.get_material().get_specificHeat() * cell.get_mass();
        double neighHeatCapacity = neigh.get_material().get_specificHeat() * cell.get_mass();
        double energyFlow = neigh.get_temp() - cell.get_temp();
        if( energyFlow > 0.0 )
        {
            energyFlow *= neighHeatCapacity;
        }
        else
        {
            energyFlow *= cellHeatCapacity;
        }
        // opoznienie w czasue
        double constantEnergyFacotr = 0.02;
        energyFlow *= constantEnergyFacotr;
        return energyFlow;
    }

    public static void exchangeEnergy( Cell cell, Cell neigh, double energy )
    {
        double cellHeatCapacity = cell.get_material().get_specificHeat() * cell.get_mass();
        double neighHeatCapacity = neigh.get_material().get_specificHeat() * cell.get_mass();
        neigh.set_temp( neigh.get_temp() - energy / neighHeatCapacity );
        cell.set_temp( cell.get_temp() + energy / cellHeatCapacity );
        if( ( energy > 0 && neigh.get_temp() < cell.get_temp() )
                || ( energy <= 0 && neigh.get_temp() > cell.get_temp() ) )
        {
            double l_totalEnergy = cellHeatCapacity * cell.get_temp() + neighHeatCapacity
                    * cell.get_temp();
            l_totalEnergy = cellHeatCapacity * cell.get_temp() + neighHeatCapacity
                    * neigh.get_temp();
            double l_avarageTemp = l_totalEnergy / ( cellHeatCapacity + neighHeatCapacity );
            cell.set_temp( l_avarageTemp );
            neigh.set_temp( l_avarageTemp );
        }
    }
}
