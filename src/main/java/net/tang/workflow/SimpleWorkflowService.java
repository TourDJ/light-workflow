package net.tang.workflow;

import net.tang.workflow.business.BusinessService;
import net.tang.workflow.core.ActionInfo;
import net.tang.workflow.core.ActionResult;
import net.tang.workflow.core.Workflow;

public interface SimpleWorkflowService {

	
	/**
	 * 处理流程
	 * @param workflow 流程类
	 * @param actionInfo 动作封装类
	 * @return
	 */
	ActionResult doAction(Workflow workflow, ActionInfo actionInfo, BusinessService service);
	
	/**
	 * 撤回流程
	 * @param workflow 流程类
	 * @param entityId
	 * @param actionInfo 动作封装类
	 * @return
	 */
	boolean cancel(Workflow workflow, int entityId, ActionInfo actionInfo, BusinessService service);
}
