
package com.pop.lib;

/**
 * An interface for MixState, so that it can be used in the library / plugin side, without knowing
 * the implementation.
 * 没用
 * 仅做为一种类型的声明使用，统一其他函数中的传参形式
 * @author A. Egal
 */
public interface MixStateInterface {

	boolean handleEvent(MixContextInterface ctx, String onPress);
	
}