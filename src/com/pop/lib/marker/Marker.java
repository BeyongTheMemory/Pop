
package com.pop.lib.marker;

import com.pop.lib.MixContextInterface;
import com.pop.lib.MixStateInterface;
import com.pop.lib.gui.Label;
import com.pop.lib.gui.PaintScreen;
import com.pop.lib.marker.draw.ParcelableProperty;
import com.pop.lib.marker.draw.PrimitiveProperty;
import com.pop.lib.render.Camera;
import com.pop.lib.render.MixVector;

import android.location.Location;

/**
 * The marker interface.
 * @author A. Egal
 */
public interface Marker extends Comparable<Marker>{

	String getTitle();//标志物关键词

	String getURL();

	double getLatitude();

	double getLongitude();

	double getAltitude();

	MixVector getLocationVector();

	void update(Location curGPSFix);

	void calcPaint(Camera viewCam, float addX, float addY);

	void draw(PaintScreen dw);

	double getDistance();

	void setDistance(double distance);

	String getID();

	void setID(String iD);

	boolean isActive();

	void setActive(boolean active);

	int getColour();
	
	public void setTxtLab(Label txtLab);

	Label getTxtLab();

	public boolean fClick(float x, float y, MixContextInterface ctx, MixStateInterface state);

	int getMaxObjects();
	
	void setExtras(String name, ParcelableProperty parcelableProperty);
	
	void setExtras(String name, PrimitiveProperty primitiveProperty);
	
	String toString();
	
	public String getType();
	
	public int getPopid();
	public void setPopid(int id);

}