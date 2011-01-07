/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

import Helpers.EnvSettings;
import Model.World.CellIndex;
import java.util.List;

/**
 *
 * @author TeMPOraL
 */

// <editor-fold defaultstate="collapsed" desc="">
// <editor-fold defaultstate="collapsed" desc="">
// <editor-fold defaultstate="collapsed" desc="">
// <editor-fold defaultstate="collapsed" desc="">
// <editor-fold defaultstate="collapsed" desc="">
// <editor-fold defaultstate="collapsed" desc="Nothing to see here, move along.">
// <editor-fold defaultstate="collapsed" desc="">
// <editor-fold defaultstate="collapsed" desc="No, really, drop it.">
// <editor-fold defaultstate="collapsed" desc="Quit now, and cake will be served immediately.">
// Thought so...
class Nothing extends Object {}
//FIXME ok, ok... one day change this to iSmoke interface, or sth ;)
// The cake is a lie!
// </editor-fold>
// </editor-fold>
// </editor-fold>
// </editor-fold>
// </editor-fold>
// </editor-fold>
// </editor-fold>
// </editor-fold>
// </editor-fold>

public class SmokeConductor extends Nothing { // ;)
    public void conductSmoke( Cell cell, Cell worldCurrentValues[][][],
            List<World.CellIndex> cellNeighboours, Cell oldValue, Cell worldOldValues[][][],
            CellIndex cellId )
    {
	double total = 0.0;
	for(CellIndex idx : cellNeighboours)
	{
	    switch(HeataAndVaporsConducter.getWhichNeighbour(cellId, idx))
	    {
		case EnvSettings.TOP_NEIGHBOUR:
		{
		    total += smokeTransferToCell(worldOldValues[cellId.x][cellId.y][cellId.z],
			    worldOldValues[idx.x][idx.y][idx.z], 0.5, 1.0);
		    break;
		}

		case EnvSettings.BOTTOM_NEIGHBOUR:
		{
		    total += smokeTransferToCell(worldOldValues[cellId.x][cellId.y][cellId.z],
			    worldOldValues[idx.x][idx.y][idx.z], 1.0, 0.5);
		    break;
		}

		case EnvSettings.SIDE_NEIGHBOUR:
		{
		    total += smokeTransferToCell(worldOldValues[cellId.x][cellId.y][cellId.z],
			    worldOldValues[idx.x][idx.y][idx.z], 0.75, 0.75);
		    break;
		}
		default:
		{
		    assert false : "Invalid neighbour type!";
		}
	    }
	}

	total /= 6.0;
	cell.set_smoke(cell.get_smoke() + total);
    }

    protected double availableCellSmoke(Cell cell)
    {
	return cell.get_smoke() / 6.0;
    }

    protected double availableCellSpace(Cell cell)
    {
	return (cell.get_material().get_smokeCapacity() - cell.get_smoke())/6.0;
    }

    protected double smokeTransferToCell(Cell myCell, Cell otherCell, double coeffIncoming, double coeffOutgoing)
    {
	return (Math.min(availableCellSpace(myCell), availableCellSmoke(otherCell)) * coeffIncoming)
		+
               (Math.min(availableCellSmoke(myCell), availableCellSpace(otherCell)) * coeffOutgoing);
    }
}
