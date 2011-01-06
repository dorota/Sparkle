/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Helpers;

import java.util.List;

/**
 *
 * @author TeMPOraL
 */
public final class Statistics {
    public static long computeKwartylDolny(List<Long> data)
    {
        int n = data.size();

        if( (n % 4) == 0)
        {
            return (data.get(n/4) + data.get((n/4)+1))/2;
        }
        else
        {
            return (data.get((n/4))+1);
        }
    }

    public static long computeKwartylGorny(List<Long> data)
    {
        int n = data.size();

        if( (n % 4) == 0)
        {
            return (data.get((3*n)/4) + data.get(((3*n)/4)+1))/2;
        }
        else
        {
            return (data.get(((3*n)/4))+1);
        }
    }

    public static long computeMedian(List<Long> data)
    {
        int n = data.size();

        if( (n % 2) == 0)
        {
            return (data.get(n/2) + data.get((n/2)+1))/2;
        }
        else
        {
            return (data.get((n/2))+1);
        }
    }
}
