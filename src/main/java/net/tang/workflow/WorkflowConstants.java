package net.tang.workflow;

import java.util.HashMap;
import java.util.Map;

import net.tang.workflow.core.HandlingType;

public class WorkflowConstants {

	public static final String WORKFLOW_STEP_SORT = "step";
	
	public static final int WORKFLOW_STEP_ROWS = 20;
	
	public static final String WORKFLOW_ACTION_SORT = "action";
	
	public static final int WORKFLOW_ACTION_ROWS = 20;
	
	public static final String WORKFLOW_DO_SORT = "id";
	
	public static final int WORKFLOW_DO_ROWS = 20;
	
	public static final String WORKFLOW_STEP_FIRST = "申请人";
	
	public static final String WORKFLOW_STEP_LAST = "完成";


	/**
	 * 当前使用的获取审批人的方式
	 */
	public static final HandlingType workflowUse = HandlingType.ROLE; 
	
	/**
	 * 流程动作编码
	 */
	public static Map<String, Boolean> actionMap = new HashMap<>();
	static {
		actionMap.put("choose", true);
		actionMap.put("disuse", false);
		actionMap.put("agree", true);
		actionMap.put("disagree", false);
		actionMap.put("yes", true);
		actionMap.put("no", false);
		actionMap.put("approve", true);
		actionMap.put("refuse", false);
	}
	
}
