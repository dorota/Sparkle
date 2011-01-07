package Model;

import java.util.List;

import Helpers.EnvSettings;
import Interfaces.IHeatAndVaporsConductor;
import Model.World.CellIndex;

public class HeataAndVaporsConducter implements IHeatAndVaporsConductor
{
    public void conductHeat( Cell cell, Cell worldCurrentValues[][][],
            List<World.CellIndex> cellNeighboours, Cell oldValue, Cell worldOldValues[][][],
            CellIndex cellId )
    {
        for( int i = 0; i < cellNeighboours.size(); ++i )
        {
            World.CellIndex neighId = cellNeighboours.get( i );
            Cell oldNeigh = worldOldValues[ neighId.x ][ neighId.y ][ neighId.z ];
            Cell neigh = worldCurrentValues[ neighId.x ][ neighId.y ][ neighId.z ];
            int whichNeighbour = EnvSettings.DOESNT_MATTER;
            if( cell.get_material().get_name().equals( "Air" )
                    && neigh.get_material().get_name().equals( "Air" ) )
            {
                whichNeighbour = getWhichNeighbour( cellId, neighId );
            }
            double energy = calculateEnergyFlow( oldValue, oldNeigh, whichNeighbour );
            exchangeEnergy( cell, neigh, energy );
        }
    }

    public static int getWhichNeighbour( CellIndex cellId, CellIndex neighId )
    {
        if( cellId.y < neighId.y )
        {
            return EnvSettings.TOP_NEIGHBOUR;
        }
        else if( cellId.y > neighId.y )
        {
            return EnvSettings.BOTTOM_NEIGHBOUR;
        }
        else
        {
            return EnvSettings.SIDE_NEIGHBOUR;
        }
    }

    public double calculateEnergyFlow( Cell cell, Cell neigh, int whichNighbour )
    {
        double cellHeatCapacity = cell.get_material().get_specificHeat() * cell.get_mass();
        double neighHeatCapacity = neigh.get_material().get_specificHeat() * cell.get_mass();
        double energyFlow = neigh.get_temp() - cell.get_temp();
        // System.out.println( "temperatury " + cell.get_temp() + " " +
        // neigh.get_temp() );
        // System.out.println( "energ y low" + neigh.get_temp() );
        // double thermalConductivity = (
        // cell.get_material().get_thermalConductivity() + neigh
        // .get_material().get_thermalConductivity() ) / 2.0;
        // if( cell.get_material().get_thermalConductivity() !=
        // neigh.get_material()
        // .get_thermalConductivity() )
        // {
        // thermalConductivity = Math.min(
        // cell.get_material().get_thermalConductivity(), neigh
        // .get_material().get_thermalConductivity() );
        // }
        if( energyFlow > 0.0 )
        {
            energyFlow *= neighHeatCapacity;
            energyFlow *= neigh.get_material().get_thermalConductivity();
        }
        else
        {
            energyFlow *= cellHeatCapacity;
            energyFlow *= cell.get_material().get_thermalConductivity();
        }
        // energyFlow *= thermalConductivity;
        // opoznienie w czasue
        energyFlow *= EnvSettings.CONSTANT_ENERGY_FACTOR;
        return energyFlow;
    }

    public void exchangeEnergy( Cell cell, Cell neigh, double energy )
    {
        double cellHeatCapacity = cell.get_material().get_specificHeat() * cell.get_mass();
        double neighHeatCapacity = neigh.get_material().get_specificHeat() * cell.get_mass();
        double percentageOfEnergyPassed;
        // //////////////////////////// VAPORS PASSING VIA
        // CONVECTION//////////////////
        // percentage of vapors passed equals percentage of energy passed
        // energy passed from neigh to cell
        if( neigh.get_temp() > cell.get_temp() )
        {
            percentageOfEnergyPassed = ( energy / neighHeatCapacity ) / neigh.get_temp();
        }
        // energy passed from cell to neigh
        else
        {
            percentageOfEnergyPassed = ( energy / cellHeatCapacity ) / cell.get_temp();
        }
        // exchange of vapors is simulated only between air cells - convection
        if( cell.get_material().get_name().equals( "Air" )
                && neigh.get_material().get_name().equals( "Air" ) )
        {
            percentageOfEnergyPassed *= 10000;
            if( neigh.get_temp() > cell.get_temp() )
            {
                neigh.set_percentageOfVaporsInCell( (float)( neigh.get_percentageOfVaporsInCell() - neigh
                        .get_percentageOfVaporsInCell() * percentageOfEnergyPassed ) );
                cell.set_percentageOfVaporsInCell( (float)( cell.get_percentageOfVaporsInCell() + neigh
                        .get_percentageOfVaporsInCell() * percentageOfEnergyPassed ) );
            }
            else
            {
                cell.set_percentageOfVaporsInCell( (float)( cell.get_percentageOfVaporsInCell() - cell
                        .get_percentageOfVaporsInCell() * percentageOfEnergyPassed ) );
                neigh.set_percentageOfVaporsInCell( (float)( neigh.get_percentageOfVaporsInCell() + cell
                        .get_percentageOfVaporsInCell() * percentageOfEnergyPassed ) );
            }
        }
        // /////////////////////////////END OF VAPORS PASSING VIA
        // CONVECTION//////////////
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
            percentageOfEnergyPassed = l_avarageTemp / ( cellHeatCapacity * cell.get_temp() );
            cell.set_temp( l_avarageTemp );
            neigh.set_temp( l_avarageTemp );
            // System.out.println( "temps " + cell.get_temp() + " " +
            // neigh.get_temp() );
        }
    }
}
