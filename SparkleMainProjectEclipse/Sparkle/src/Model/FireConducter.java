package Model;

import java.util.List;

import Helpers.EnvSettings;
import Helpers.EnvSettings.CellState;

public class FireConducter
{
    public void spreadFire( Cell cell, List<World.CellIndex> cellNeighboours )
    {
        String materialName = cell.get_material().get_name();
        System.out.println( "vapors " + cell.get_percentageOfVaporsInCell() );
        if( cell.get_temp() >= cell.get_material().get_flamePoint()
                && materialName.equals( "Air" ) == false )
        {
            cell.set_fireClock( cell.get_material().get_howLongItBurns() );
            cell.set_percentageOfVaporsInCell( 1.0f );
        }
        else if( cell.get_cellState() == CellState.ON_FIRE )
        {
            if( cell.get_fireClock() > 0 )
            {
                cell.set_fireClock( cell.get_fireClock() - 1 );
            }
            else
            {
                cell.set_cellState( CellState.FIRED );
            }
        }
        else if( materialName.equals( "Air" )
                && cell.get_percentageOfVaporsInCell() >= EnvSettings.MIN_VAPORS_TO_FIRE
                && cell.get_percentageOfVaporsInCell() < EnvSettings.MAX_VAPORS_TO_FIRE )
        {
            System.out.println( "cell on fire" );
            cell.set_cellState( CellState.ON_FIRE );
        }
        else
        {
            // System.out.println( cell.get_material() + " " +
            // cell.get_percentageOfVaporsInCell() );
        }
    }
}
