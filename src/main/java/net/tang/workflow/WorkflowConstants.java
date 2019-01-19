package net.tang.workflow;

import java.util.HashMap;
import java.util.Map;

import net.tang.workflow.core.HandlingType;

public class WorkflowConstants {

	public static final String DICT_WORKFLOW_STATUS_APPLY = "workflowapply"; //流程申请
	
	public static final String DICT_WORKFLOW_STATUS_AUDIT = "workflowaudit"; //流程审核
	
	/**
	 * 流程申请人查看工单的状态
	 */
	public static final String BUSY_WORKFLOW_APPLY_DRAFT = "1";	   //存草稿
	public static final String BUSY_WORKFLOW_APPLY_PROGRESS = "2"; //审批中(待审批)
	public static final String BUSY_WORKFLOW_APPLY_FINISH = "3";   //审批结束(已审批)
	
	/**
	 * 流程审核人查看工单的状态
	 */
	public static final String BUSY_WORKFLOW_AUDIT_PROGRESS = "2";	//待审批
	public static final String BUSY_WORKFLOW_AUDIT_FINISH = "3";	//已审批

	/**
	 * 流程工单的状态
	 */
	public static final String BUSY_WORKFLOW_STATUS_DRAFT = "1";	   	//存草稿
	public static final String BUSY_WORKFLOW_STATUS_PROGRESS = "2"; 	//审批中(待审批)
	public static final String BUSY_WORKFLOW_STATUS_FINISH = "3";   	//审批结束(已审批)	
	/**
	 * 流程处理工单流水状态
	 */
	public static final int BUSY_WORKFLOW_DEAL_ONGING = 0; 	//待审批
	public static final int BUSY_WORKFLOW_DEAL_END = 1;		//已审批
	public static final int BUSY_WORKFLOW_DEAL_CANCEL = -1;	//撤消
	public static final String BUSY_WORKFLOW_DEAL_ONGING_NAME = "待审批";
	public static final String BUSY_WORKFLOW_DEAL_END_NAME = "已审批";
	

	/**
	 * 业务工单的状态
	 */
	public static final int BUSY_ORDER_STATUS_DRAFT = 0;	//存草稿(初始化)
	public static final int BUSY_ORDER_STATUS_ONGOING = 2;	//流程中
	public static final int BUSY_ORDER_STATUS_COMPLETE = 1;	//流程结束

	/**
	 * 当前使用的获取审批人的方式
	 */
	public static final HandlingType workflowUse = HandlingType.ROLE; 
	
	/**
	 * 流程动作编码
	 */
	public static Map<String, Boolean> actionMap = new HashMap<>();
	static {
		actionMap.put("agree", true);
		actionMap.put("disagree", false);
		actionMap.put("yes", true);
		actionMap.put("no", false);
		actionMap.put("approve", true);
		actionMap.put("refuse", false);
	}
	
}
