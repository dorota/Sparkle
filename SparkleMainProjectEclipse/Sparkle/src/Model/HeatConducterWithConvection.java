package Model;

import Helpers.EnvSettings;

public class HeatConducterWithConvection extends HeatConducter
{
    @Override
    public double calculateEnergyFlow( Cell cell, Cell neigh, int whichNeighbour )
    {
        double energy = super.calculateEnergyFlow( cell, neigh, whichNeighbour );
        // wymiana ciepla w powietrzu
        if( cell.get_material().get_name().equals( "Air" )
                && neigh.get_material().get_name().equals( "Air" ) )
        {
            System.out.println( "we are here" );
            energy /= EnvSettings.CONSTANT_ENERGY_FACTOR;
            double sideEnergyFactor = 0.015;
            double topEnergyFactor = 0.2;
            double downEnergyFactor = 0.05;
            if( ( whichNeighbour == EnvSettings.TOP_NEIGHBOUR && energy < 0 )
                    || ( whichNeighbour == EnvSettings.BOTTOM_NEIGBOUR && energy > 0 ) )
            {
                System.out.println( "konwecja pracuje w gore" );
                energy *= topEnergyFactor;
            }
            else if( ( whichNeighbour == EnvSettings.BOTTOM_NEIGBOUR && energy < 0 )
                    || ( whichNeighbour == EnvSettings.TOP_NEIGHBOUR && energy > 0 ) )
            {
                System.out.println( "konwecja pracuje w dol " );
                energy *= downEnergyFactor;
            }
            else
            {
                System.out.println( "konwecja pracuje tylko na boki" );
                energy *= sideEnergyFactor;
            }
        }
        return energy;
    }
}
