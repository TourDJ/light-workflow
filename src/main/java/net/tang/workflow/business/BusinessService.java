package net.tang.workflow.business;

import java.util.List;

import net.tang.service.UserService;
import net.tang.workflow.core.ActionInfo;
import net.tang.workflow.core.ActionResult;
import net.tang.workflow.core.ProcessPoint;
import net.tang.workflow.core.Workflow;
import net.tang.workflow.model.WorkflowDo;

/**
 * 流程业务服务接口
 * @author service1
 *
 */
public interface BusinessService {
	
	/**
	 * 更新申请时间
	 * @param id
	 * @return
	 */
	Integer updateApplyTime(Integer id);
	
	/**
	 * 获取模块信息
	 * @param id
	 * @return
	 */
	WorkflowModel getModel(Integer id);
	
	/**
	 * 更新模块
	 * @param workflowModel
	 * @return
	 */
	boolean updateModel(WorkflowModel workflowModel);
	
	/**
	 * 获取流程的编号
	 * @return
	 */
	String getWorkflowCode();

	/**
	 * 流程处理
	 * @param noticeService
	 * @param menuService
	 * @param workflow
	 * @param actionInfo
	 * @param userId
	 * @return
	 */
	ActionResult doAction(Workflow workflow, ActionInfo actionInfo, Integer userId);
	
	/**
	 * 查看流程审批进度
	 * @param userService
	 * @param workflow
	 * @param busyId
	 * @return
	 */
	List<WorkflowDo> history(UserService userService, Workflow workflow, Integer busyId);
	
	/**
	 * 流程撤回
	 * @param workflow
	 * @param userId
	 * @param actionInfo
	 * @return
	 */
	boolean cancel(Workflow workflow, Integer userId, ActionInfo actionInfo);	
	
	/**
	 * 是否可以撤回工单
	 * @param workflow
	 * @param busyId
	 * @return
	 */
	boolean canCancel(Workflow workflow, Integer busyId);
	
	/**
	 * 查看业务流程
	 * @param userService
	 * @param workflow
	 * @param busyId
	 * @return
	 */
	List<ProcessPoint> workflowProcess(UserService userService, Workflow workflow, Integer busyId);
}
