package Model;

import java.util.List;

import Helpers.EnvSettings;
import Helpers.EnvSettings.CellState;
import Interfaces.IFireConductor;

public class FireConducter implements IFireConductor
{
    public void spreadFire( Cell cell, List<World.CellIndex> cellNeighboours, Cell[][][] _world )
    {
        // jezeli komórka jest paliwem i przekroczy flame point / flash point
        // zaczyna
        // produkowac opary. Opary produkowane do biezacej komórki oraz s¹siadów
        // bêd¹cych tlenem
        if( cell.get_temp() >= cell.get_material().get_flamePoint()
                && cell.get_material().is_fuel() && cell.get_cellState().equals( CellState.NEUTRAL ) )
        {
            cell.set_percentageOfVaporsInCell( 1.0f );
            for( int i = 0; i < cellNeighboours.size(); ++i )
            {
                World.CellIndex id = cellNeighboours.get( i );
                if( _world[ id.x ][ id.y ][ id.z ].get_material().get_name().equals( "Air" ) )
                {
                    _world[ id.x ][ id.y ][ id.z ]
                            .set_percentageOfVaporsInCell( (float)( EnvSettings.MIN_VAPORS_TO_FIRE ) );
                }
            }
        }
        // symualcja wypalania sie komórki. komórka mo¿e siê paliæ przez
        // okreslony czas.. je¿eli komórka ma stan palenia siê zmniejszany jest
        // licznik a¿ osiagniê 0 - komrka wypali³a siê
        if( cell.get_cellState().equals( CellState.ON_FIRE ) )
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
        // jezeli komórka nie pali sie a stê¿enie procentowe zawartych w niej
        // oparów jest takie ¿e
        // powinna siê paliæ oraz ma odpowiedni¹ temperaturê zapalamy tak¹
        // komórkê i ustawiamy zegar odliczajacy czas jej spalania
        else if( cell.get_percentageOfVaporsInCell() >= EnvSettings.MIN_VAPORS_TO_FIRE
                && cell.get_temp() > EnvSettings.FIRE_TEMP )
        {
            cell.set_fireClock( cell.get_material().get_howLongItBurns() );
            cell.set_cellState( CellState.ON_FIRE );
        }
    }
}
