
package com.pop.lib;

/**
 * An interface for MixState, so that it can be used in the library / plugin side, without knowing
 * the implementation.
 * û��
 * ����Ϊһ�����͵�����ʹ�ã�ͳһ���������еĴ�����ʽ
 * @author A. Egal
 */
public interface MixStateInterface {

	boolean handleEvent(MixContextInterface ctx, String onPress);
	
}