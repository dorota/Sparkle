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
            int counter = 0;

            public void actionPerformed( ActionEvent e )
            {
                if( whichTimeActionPerformed == FIRST_TIME_ACTION_PERFORMED )
                {
                    try
                    {
                        // wait a second after initializing and displaying
                        // start of fire
                        System.out.println( "first time" );
                        Thread.sleep( 500 );
                    }
                    catch( InterruptedException exception )
                    {
                    }
                }
                else
                {
                    // if( counter == 0 )
                    // {
                    // for( int i = 0; i < EnvSettings.getMAX_LENGTH(); ++i )
                    // {
                    // for( int j = 0; j < EnvSettings.getMAX_LENGTH(); ++j )
                    // {
                    // for( int k = 0; k < EnvSettings.getMAX_LENGTH(); ++k )
                    // {
                    // System.out.println( "value "
                    // + _world._worldCurrentValues[ i ][ j ][ k ] );
                    // }
                    // }
                    // }
                    // System.out.println( "before first call of algo" );
                    // }
                    _world.simulateHeatConduction();
                    counter++;
                }
                ++whichTimeActionPerformed;
            }
        } );
        samplingTimer.start();
    }
}
