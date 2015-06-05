
package com.pop.show;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.view.Display;
import android.view.WindowManager;

/**
 * Ensures compatibility with older and newer versions of the API. 
 * See the SDK docs for comments
 * 
 * @author daniele
 *
 */
public class Compatibility {
	private static Method mParameters_getSupportedPreviewSizes;
	private static Method mDefaultDisplay_getRotation;

	static {  
		initCompatibility();
	};

	/** this will fail on older phones (Android version < 2.0) */
	private static void initCompatibility() {
		try {
			mParameters_getSupportedPreviewSizes = Camera.Parameters.class.getMethod(
					"getSupportedPreviewSizes", new Class[] { } );
			mDefaultDisplay_getRotation = Display.class.getMethod("getRotation", new Class[] { } );

			/* success, this is a newer device */
		} catch (NoSuchMethodException nsme) {
			/* failure, must be older device */
		}
	}

	/** If it's running on a new phone, let's get the supported preview sizes, before it was fixed to 480 x 320*/
	@SuppressWarnings("unchecked")
	public static List<Camera.Size> getSupportedPreviewSizes(Camera.Parameters params) {
		List<Camera.Size> retList = null;

		try {
			Object retObj = mParameters_getSupportedPreviewSizes.invoke(params);
			if (retObj != null) {
				retList = (List<Camera.Size>)retObj;
			}
		}
		catch (InvocationTargetException ite) {
			/* unpack original exception when possible */
			Throwable cause = ite.getCause();
			if (cause instanceof RuntimeException) {
				throw (RuntimeException) cause;
			} else if (cause instanceof Error) {
				throw (Error) cause;
			} else {
				/* unexpected checked exception; wrap and re-throw */
				throw new RuntimeException(ite);
			}
		} catch (IllegalAccessException ie) {
			//System.err.println("unexpected " + ie);
		}
		return retList;
	}

	static public int getRotation(final Activity activity) {
		int result = 1;
		try {
				Display display = ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
				Object retObj = mDefaultDisplay_getRotation.invoke(display);
				if( retObj != null) {
					result = (Integer) retObj;
				}
		} catch (Exception ex) {
			//ex.printStackTrace();
		}
		return result;
	}

}