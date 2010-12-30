package Controller;

import Helpers.ProfileSample;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import Model.World;

public class SimulationMgr
{
    int timeDelay = 50;
    Timer samplingTimer;
    World _world;
    private boolean isRunning = false;

    private ProfileSample profiler = new ProfileSample("simulation: timer tick - isRunning=t");

    public boolean isRunning()
    {
        return isRunning;
    }

    public void setRunning( boolean isRunning )
    {
        this.isRunning = isRunning;
    }

    public SimulationMgr( World world )
    {
        _world = world;
    }

    public void manageSimulation()
    {
        shutdownPreviousTimerIfApplicable();
        samplingTimer = new Timer( timeDelay, new ActionListener()
        {
            final int FIRST_TIME_ACTION_PERFORMED = 0;
            int whichTimeActionPerformed = 0;
            int counter = 0;

	    long executionTimer = 0; //used to measure execution time

            public void actionPerformed( ActionEvent e )
            {
                if( whichTimeActionPerformed == FIRST_TIME_ACTION_PERFORMED )
                {
                    try
                    {
                        // wait a second after initializing and displaying
                        // start of fire
                        System.out.println( "first time" );
                        Thread.sleep( 2000 );
                    }
                    catch( InterruptedException exception )
                    {
                    }
                }
                else
                {

                    if( isRunning )
                    {
			//executionTimer = System.currentTimeMillis();
			profiler.start();
                        _world.simulateHeatConduction();
                        counter++;

			//seen LOST?
			//System.out.println("ITERATION " + counter
			//	+ " " + executionTimer
			//	+ " " + (System.currentTimeMillis() - executionTimer));
			profiler.stop();
                    }
                    else
                    {
                        // DO NOTHING
			//System.out.println("EMPTY-CYCLE " + System.currentTimeMillis());
                    }
                }
                ++whichTimeActionPerformed;
            }
        } );
        samplingTimer.start();
    }

    private void shutdownPreviousTimerIfApplicable()
    {
	if (samplingTimer != null)
	{
	    samplingTimer.stop();
	}
    }
}
