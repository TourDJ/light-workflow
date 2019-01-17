package net.tang.workflow.core;

import lombok.Data;

/**
 * 流程操作返回结果
 *
 */
@Data
public class ActionResult {

	/**
	 * 操作标记
	 */
	private boolean flag;
	
	/**
	 * 操作信息
	 */
	private String message;
	
	/**
	 * 操作状态
	 */
	private boolean status;
	
	/**
	 * 初始化
	 * @return
	 */
	public static ActionResult initial() {
		ActionResult actionResult = new ActionResult();
		actionResult.flag = false;
		actionResult.message = "操作失败";
		actionResult.status = false;
		return actionResult;
	}
	
	/**
	 * 创建空实例
	 * @return
	 */
	public static ActionResult instance() {
		return new ActionResult();
	}
	
	public static ActionResult instance(boolean flag, String message, boolean status) {
		ActionResult actionResult = new ActionResult();
		actionResult.setFlag(flag);
		actionResult.setMessage(message);
		actionResult.setStatus(status);
		return actionResult;
	}
}
