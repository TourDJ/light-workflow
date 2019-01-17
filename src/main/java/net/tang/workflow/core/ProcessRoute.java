package net.tang.workflow.core;

import java.util.ArrayList;
import java.util.List;

import net.tang.workflow.WorkflowConstants;

/**
 * 业务流程.
 * 
 * @author service1
 *
 */
public final class ProcessRoute {

	/**
	 * 业务流程单步集合
	 */
	private List<ProcessPoint> points;

	/**
	 * 业务流程开始
	 */
	private ProcessPoint processBegin;

	/**
	 * 业务流程结束
	 */
	private ProcessPoint processEnd;

	private ProcessRoute() {
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		points = new ArrayList<>();
		drawBeginFlow();
	}

	/**
	 * 获取实例
	 * @return
	 */
	public static ProcessRoute getInstance() {
		return new ProcessRoute();
	}

	/**
	 * 流程开始步骤
	 */
	private void drawBeginFlow() {
		processBegin = add(0, WorkflowConstants.WORKFLOW_STEP_FIRST, false);
		points.add(processBegin);
	}

	/**
	 * 流程结束步骤
	 */
	private void drawEndFlow() {
		int end = points.size();
		processEnd = add(end, WorkflowConstants.WORKFLOW_STEP_LAST, false);
		points.add(processEnd);
	}

	/**
	 * 增加一个流程步骤
	 * @param end
	 * @param stepName
	 * @param process
	 * @return
	 */
	private ProcessPoint add(int end, String stepName, boolean process) {
		ProcessPoint point = new ProcessPoint();
		point.setStep(end);
		point.setStepName(stepName);
		point.setProcess(process);
		return point;
	}

	/**
	 * 增加一个流程步骤
	 * @param point
	 * @return
	 */
	public boolean add(ProcessPoint point) {
		boolean bool = false;
		if (points == null || points.size() == 0) {
			init();
		}
		
		if(point != null)
			bool = points.add(point);

		return bool;
	}
	
	/**
	 * 增加结束步骤
	 * @return
	 */
	public ProcessPoint addEnd() {
		drawEndFlow();
		return processEnd;
	}

	/**
	 * 获取所有流程步骤
	 * @return
	 */
	public List<ProcessPoint> get() {
		return points;
	}
	
	/**
	 * 获取开始步骤
	 * @return
	 */
	public ProcessPoint getBegin() {
		return processBegin;
	}
	
	/**
	 * 获取结束步骤
	 * @return
	 */
	public ProcessPoint getEnd() {
		return processEnd;
	}
}
