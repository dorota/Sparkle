package Model;

import Helpers.EnvSettings;

public class HeatConducterWithConvection extends HeataAndVaporsConducter
{
    @Override
    public double calculateEnergyFlow( Cell cell, Cell neigh, int whichNeighbour )
    {
        double energy = super.calculateEnergyFlow( cell, neigh, whichNeighbour );
        // wymiana ciepla w powietrzu
        if( cell.get_material().get_name().equals( "Air" )
                && neigh.get_material().get_name().equals( "Air" ) )
        {
            energy /= EnvSettings.CONSTANT_ENERGY_FACTOR;
            double sideEnergyFactor = 0.1;
            double topEnergyFactor = 0.2;
            double downEnergyFactor = 0.05;
            if( ( whichNeighbour == EnvSettings.TOP_NEIGHBOUR && energy < 0 )
                    || ( whichNeighbour == EnvSettings.BOTTOM_NEIGBOUR && energy > 0 ) )
            {
                energy *= topEnergyFactor;
            }
            else if( ( whichNeighbour == EnvSettings.BOTTOM_NEIGBOUR && energy < 0 )
                    || ( whichNeighbour == EnvSettings.TOP_NEIGHBOUR && energy > 0 ) )
            {
                energy *= downEnergyFactor;
            }
            else
            {
                energy *= sideEnergyFactor;
            }
        }
        return energy;
    }
}
