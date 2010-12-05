package Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import Model.World;

public class SimulationMgr
{
    int timeDelay = 33;
    Timer samplingTimer;
    World _world;

    public SimulationMgr( World world )
    {
        _world = world;
    }

    public void manageSimulation()
    {
        samplingTimer = new Timer( timeDelay, new ActionListener()
        {
            final int FIRST_TIME_ACTION_PERFORMED = 0;
            int whichTimeActionPerformed = 0;

            public void actionPerformed( ActionEvent e )
            {
                if( whichTimeActionPerformed == FIRST_TIME_ACTION_PERFORMED )
                {
                    try
                    {
                        // wait a second after initializing and displaying
                        // start of fire
                        Thread.sleep( 500 );
                    }
                    catch( InterruptedException exception )
                    {
                    }
                }
                else
                {
                    _world.simulateHeatConduction();
                }
                ++whichTimeActionPerformed;
            }
        } );
        samplingTimer.start();
    }
}
