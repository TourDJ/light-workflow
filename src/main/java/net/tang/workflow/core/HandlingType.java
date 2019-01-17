package net.tang.workflow.core;

/**
 * 处理人的获取方式
 * @author service1
 *
 */
public enum HandlingType {

	/**
	 * 用户id
	 */
	ID(1),
	
	/**
	 * 角色
	 */
	ROLE(2),
	
	/**
	 * 部门
	 */
	DEPARTMENT(3)
	;
	
	//
	private int deal;
	
	HandlingType(int deal) {
		this.deal = deal;
	}
	
	public int getDeal() {
		return this.deal;
	}
}
