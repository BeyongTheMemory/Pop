
package com.pop.gui;

import com.pop.show.DataView;
import com.pop.lib.marker.Marker;
import com.pop.lib.gui.PaintScreen;
import com.pop.lib.gui.ScreenObj;
import com.pop.data.DataHandler;

import android.graphics.Color;

/** Takes care of the small radar in the top left corner and of its points
 * 描绘左上角雷达上的点
 * @author daniele
 *
 */
public class RadarPoints implements ScreenObj {
	/** The screen */
	public DataView view;
	/** The radar's range */
	float range;
	/** Radius in pixel on screen */
	/**雷达半径*/
	public static float RADIUS = 40;
	/** Position on screen */
	static float originX = 0 , originY = 0;
	/** Color */
	static int radarColor = Color.argb(20,255,255,255);
	
	public void paint(PaintScreen dw) {
		/** radius is in KM. */
		range = view.getRadius() * 1000;
		/** Draw the radar */
		dw.setFill(true);
		dw.setColor(radarColor);
		//绘制雷达的圆
		dw.paintCircle(originX + RADIUS, originY + RADIUS, RADIUS);

		/** put the markers in it */
		/**在雷达中绘制标示的点*/
		float scale = range / RADIUS;

		DataHandler jLayer = view.getDataHandler();

		for (int i = 0; i < jLayer.getMarkerCount(); i++) {
			Marker pm = jLayer.getMarker(i);
			float x = pm.getLocationVector().x / scale;
			float y = pm.getLocationVector().z / scale;

			if (pm.isActive() && (x * x + y * y < RADIUS * RADIUS)) {//判断是否在圆中
				dw.setFill(true);//实心
				
				// For OpenStreetMap the color is changing based on the URL
				//设置颜色，不同的地方得到的数据显示颜色 不同
					dw.setColor(pm.getColour());
				//绘制点
				dw.paintRect(x + RADIUS - 1, y + RADIUS - 1, 2, 2);
			}
		}
	}

	/** Width on screen */
	public float getWidth() {
		return RADIUS * 2;
	}

	/** Height on screen */
	public float getHeight() {
		return RADIUS * 2;
	}
}

