package Model;

import java.util.List;

import Helpers.EnvSettings;

public class HeatConducter
{
    public static void conductHeat( Cell cell, Cell worldCurrentValues[][][],
            List<World.CellIndex> cellNeighboours, Cell oldValue, Cell worldOldValues[][][] )
    {
        for( int i = 0; i < cellNeighboours.size(); ++i )
        {
            World.CellIndex id = cellNeighboours.get( i );
            Cell oldNeigh = worldOldValues[ id.x ][ id.y ][ id.z ];
            Cell neigh = worldCurrentValues[ id.x ][ id.y ][ id.z ];
            double energy = calculateEnergyFlow( oldValue, oldNeigh );
            exchangeEnergy( cell, neigh, energy );
        }
    }

    public static double calculateEnergyFlow( Cell cell, Cell neigh )
    {
        double cellHeatCapacity = cell.get_material().get_specificHeat() * cell.get_mass();
        double neighHeatCapacity = neigh.get_material().get_specificHeat() * cell.get_mass();
        double energyFlow = neigh.get_temp() - cell.get_temp();
        // System.out.println( "energ y low" + neigh.get_temp() );
        if( energyFlow > 0.0 )
        {
            energyFlow *= neighHeatCapacity;
            energyFlow *= cell.get_material().get_thermalConductivity();
        }
        else
        {
            energyFlow *= cellHeatCapacity;
            energyFlow *= neigh.get_material().get_thermalConductivity();
        }
        // opoznienie w czasue
        energyFlow *= EnvSettings.CONSTANT_ENERGY_FACTOR;
        return energyFlow;
    }

    public static void exchangeEnergy( Cell cell, Cell neigh, double energy )
    {
        double cellHeatCapacity = cell.get_material().get_specificHeat() * cell.get_mass();
        double neighHeatCapacity = neigh.get_material().get_specificHeat() * cell.get_mass();
        neigh.set_temp( neigh.get_temp() - energy / neighHeatCapacity );
        cell.set_temp( cell.get_temp() + energy / cellHeatCapacity );
        // System.out.println( "temp " + cell.get_temp() + energy /
        // cellHeatCapacity );
        if( ( energy > 0 && neigh.get_temp() < cell.get_temp() )
                || ( energy <= 0 && neigh.get_temp() > cell.get_temp() ) )
        {
            double l_totalEnergy = cellHeatCapacity * cell.get_temp() + neighHeatCapacity
                    * neigh.get_temp();
            // l_totalEnergy = cellHeatCapacity * cell.get_temp() +
            // neighHeatCapacity
            // * neigh.get_temp();
            double l_avarageTemp = l_totalEnergy / ( cellHeatCapacity + neighHeatCapacity );
            cell.set_temp( l_avarageTemp );
            neigh.set_temp( l_avarageTemp );
            // System.out.println( "temps " + cell.get_temp() + " " +
            // neigh.get_temp() );
        }
    }
}
