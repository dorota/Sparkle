/*
 * Poor man's profiling tool - because the built-in ones don't seem to work.
 */
package Helpers;

import static Helpers.Statistics.computeKwartylDolny;
import static Helpers.Statistics.computeKwartylGorny;
import static Helpers.Statistics.computeMedian;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author TeMPOraL
 */
public class ProfileSample
{
    private static final long SAMPLE_NOT_INITIALIZED = -1;
    private long startTime = SAMPLE_NOT_INITIALIZED;
    private String name;
    private List<Long> entries = new ArrayList<Long>();
    private static List<ProfileSample> allSamples = new ArrayList<ProfileSample>();

    public ProfileSample( String sampleName )
    {
        name = sampleName;
        allSamples.add( this ); // AD. warning - yes, I know.
    }

    public ProfileSample start()
    {
        startTime = System.nanoTime();
        return this;
    }

    public ProfileSample stop()
    {
        assert startTime != SAMPLE_NOT_INITIALIZED: "Hey, you've stopped the profiling without starting!";
        // add delta time to sample log
        entries.add( System.nanoTime() - startTime );
        // reset counter
        startTime = SAMPLE_NOT_INITIALIZED;
        return this;
    }

    public void report()
    {
        System.out.println( "ENTRIES FOR SAMPLE " + name );
        for( Long entry: entries )
        {
            System.out.print( " " + entry );
        }
        System.out.println( "" );
    }

    public void reportStatistics()
    {
        if( entries.isEmpty() )
        {
            System.out.println( "STATS FOR SAMPLE " + name );
            System.out.println( "No measurements taken; nothing to report." );
            return;
        }
        List<Long> entriesCopy = new ArrayList<Long>( entries );
        Collections.sort( entriesCopy );
        long min = entriesCopy.get( 0 );
        long max = entriesCopy.get( entriesCopy.size() - 1 );
        // NOTE may be subject of value overflow!
        long avg = 0;
        for( long entry: entriesCopy )
        {
            avg += entry;
        }
        avg /= entriesCopy.size();
        // FIXME does anybody know how "kwartyle" are called in english?...
        long kwartylDolny = computeKwartylDolny( entriesCopy );
        long kwartylGorny = computeKwartylGorny( entriesCopy );
        long median = computeMedian( entriesCopy );
        System.out.println( "STATS FOR SAMPLE " + name );
        System.out.println( "Data points: " + entriesCopy.size() );
        System.out.println( "Min: " + min + "; Max " + max + "; Rozstep: " + ( max - min )
                + "; Srednia: " + avg );
        System.out.println( "Kwartyl dolny: " + kwartylDolny + "; Mediana: " + median
                + "; Kwartyl gorny: " + kwartylGorny + "; Rozstep miedzykwartylowy: "
                + ( kwartylGorny - kwartylDolny ) );
        System.out.println( "" ); // empty line
    }

    public static void reportAll()
    {
        for( ProfileSample sample: allSamples )
        {
            // sample.report(); //<-- uncomment if you absolutely, positively
            // have to look at all the data.
            sample.reportStatistics();
        }
    }
}
