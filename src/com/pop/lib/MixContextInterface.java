
package com.pop.lib;

/**
 * An interface for MixContext, so that it can be used in the libary / Plugin side, without knowing
 * the implementation.
 * MixContext使用的接口
 * @author A. Egal
 */
public interface MixContextInterface {

	void loadMixViewWebPage(String url)  throws Exception ;

}