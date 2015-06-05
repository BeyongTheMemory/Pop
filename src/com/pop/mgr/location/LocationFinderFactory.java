
package com.pop.mgr.location;

import com.pop.show.MixContext;

/**
 * Factory Of  LocationFinder
 *
 */
public class LocationFinderFactory {
	
	/**
	 * Hide implementation Of LocationFinder
	 * @param mixContext
	 * @return LocationFinder
	 */
	public static LocationFinder makeLocationFinder(MixContext mixContext){
		return new LocationMgrImpl(mixContext);
	}

}
