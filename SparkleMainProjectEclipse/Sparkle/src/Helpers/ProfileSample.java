/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

    public ProfileSample(String sampleName)
    {
	name = sampleName;
    }

    public ProfileSample start()
    {
	startTime = System.currentTimeMillis();
	return this;
    }

    public ProfileSample stop()
    {
	assert startTime != SAMPLE_NOT_INITIALIZED : "Hey, you've stopped the profiling without starting!";

	//add delta time to sample log
	entries.add(System.currentTimeMillis() - startTime);

	//reset counter
	startTime = SAMPLE_NOT_INITIALIZED;
	return this;
    }

    public void report()
    {
	//TODO
	System.out.println("ENTRIES FOR SAMPLE " + name);
	for(Long entry : entries)
	{
	    System.out.println(entry);
	}
    }

}
