package ca.hedlund.jpraat;

import java.util.ArrayList;
import java.util.List;

import ca.hedlund.jpraat.binding.fon.Function;
import ca.hedlund.jpraat.binding.fon.IntervalTier;
import ca.hedlund.jpraat.binding.fon.TextGrid;
import ca.hedlund.jpraat.binding.fon.TextInterval;
import ca.hedlund.jpraat.exceptions.PraatException;

public class TextGridUtils {
	
	/**
	 * Get tier number from name.
	 * 
	 * @param textGrid
	 * @param tierName
	 */
	public static long tierNumberFromName(TextGrid textGrid, String tierName) {
		long retVal = 0;
		
		for(long i = 1; i <= textGrid.numberOfTiers(); i++) {
			Function tier = textGrid.tier(i);
			if(tier.getName().equals(tierName)) {
				retVal = i;
				break;
			}
		}
		
		return retVal;
	}
	
	/**
	 * Get contiguous intervals for a specified interval tier.
	 * 
	 * @param textGrid
	 * @param intervalTier
	 * @param tolerence the amount of 'space' that is allowed to exist between intervals
	 *  for them to be considered contiguous
	 * @param the maximum length of a segment, if 0 unlimited
	 * @param delim marker to delimit a segment, may be <code>null</code>
	 * 
	 * @return
	 * @throws PraatException
	 */
	public static List<TextInterval> getContiguousIntervals(TextGrid textGrid, long intervalTier, double tolerence,
			double maxLength, String delim)
		throws PraatException {
		final List<TextInterval> retVal = new ArrayList<TextInterval>();
	
		final IntervalTier tier = textGrid.checkSpecifiedTierIsIntervalTier(intervalTier);
		
		double xmin = -1;
		double xmax = -1;
		StringBuffer buffer = new StringBuffer();
		
		for(long i = 1; i <= tier.numberOfIntervals(); i++) {
			final TextInterval interval = tier.interval(i);
			
			// deal with delim
			if(delim != null && interval.getText().equals(delim)) {
				if(xmin >= 0) {
					// add interval and skip to next
					TextInterval newInterval = TextInterval.create(xmin, xmax, buffer.toString());
					retVal.add(newInterval);
				}
				// reset
				xmin = -1;
				buffer.setLength(0);
				
				continue; // next interval
			}
			
			if(xmin < 0) {
				// first interval in set
				xmin = interval.getXmin();
				xmax = interval.getXmax();
				buffer.append(interval.getText());
			} else {
				double distance = interval.getXmin() - xmax;
				double totalLen = interval.getXmax() - xmin;
				if( (distance > tolerence) 
						|| (maxLength > 0 && totalLen > maxLength)) {
					TextInterval newInterval = TextInterval.create(xmin, xmax, buffer.toString());
					retVal.add(newInterval);
					
					// reset
					xmin = interval.getXmin();
					buffer.setLength(0);
				}					
				xmax = interval.getXmax();
				buffer.append(interval.getText());
			}
		}
		// add last interval (if any)
		if(xmin >= 0 && xmax > xmin) {
			TextInterval lastInterval = TextInterval.create(xmin, xmax, buffer.toString());
			retVal.add(lastInterval);
		}
		
		return retVal;
	}

}
