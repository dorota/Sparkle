/*
 * Poor man's profiling tool - because the built-in ones don't seem to work.
 */

package Helpers;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author TeMPOraL
 */
public class ProfileSample {

    private static final long SAMPLE_NOT_INITIALIZED = -1;

    private long startTime = SAMPLE_NOT_INITIALIZED;
    private String name;

    private List<Long> entries = new ArrayList<Long>();

    private static List<ProfileSample> allSamples = new ArrayList<ProfileSample>();

    public ProfileSample(String sampleName)
    {
	name = sampleName;
	allSamples.add(this);
    }

    public ProfileSample start()
    {
	startTime = System.nanoTime();
	return this;
    }

    public ProfileSample stop()
    {
	assert startTime != SAMPLE_NOT_INITIALIZED : "Hey, you've stopped the profiling without starting!";

	//add delta time to sample log
	entries.add(System.nanoTime() - startTime);

	//reset counter
	startTime = SAMPLE_NOT_INITIALIZED;
	return this;
    }

    public void report()
    {
	System.out.println("ENTRIES FOR SAMPLE " + name);
	for(Long entry : entries)
	{
	    System.out.print(" " + entry);
	}
	System.out.println("");
    }

    public void reportStatistics()
    {
	//TODO to be implemented when I figure out how one sorts lists in Java...
    }

    public static void reportAll()
    {
	for(ProfileSample sample : allSamples)
	{
	    sample.report();
	}
    }

}
