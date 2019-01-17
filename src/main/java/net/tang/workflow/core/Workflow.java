/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.tang.workflow.core;

import java.util.List;
import java.util.Map;

import net.tang.workflow.business.WorkflowModel;
import net.tang.workflow.model.WorkflowAction;
import net.tang.workflow.model.WorkflowDetail;
import net.tang.workflow.model.WorkflowDo;
import net.tang.workflow.model.WorkflowStep;
import net.tang.workflow.service.WorkflowActionService;
import net.tang.workflow.service.WorkflowDetailService;
import net.tang.workflow.service.WorkflowDoService;
import net.tang.workflow.service.WorkflowEntityService;
import net.tang.workflow.service.WorkflowStepService;

/**
 * 流程核心接口。
 * @author tang
 *
 */
public interface Workflow {
	
	/**
	 * 工作流处理化
	 * @param actionService
	 * @param stepService
	 * @param workflowDoService
	 * @param workflowDetailService
	 * @return
	 */
	Workflow init(WorkflowActionService actionService, WorkflowStepService stepService,
			WorkflowDoService workflowDoService, WorkflowDetailService workflowDetailService,
			WorkflowEntityService entityService);

	/**
	 * 获取下一步
	 * @param entityId 流程实体id
	 * @param current
	 * @return
	 */
	WorkflowStep getNextStep(Integer entityId, Integer current);

	/**
	 * 获取上一步
	 * @param entityId
	 * @param current
	 * @return
	 */
	WorkflowStep getPrevStep(Integer entityId, Integer current);

	/**
	 * 获取指定的步骤
	 * @param entityId
	 * @param current
	 * @param interval
	 * @return
	 */
	WorkflowStep getWantedStep(Integer entityId, Integer current, Integer interval);

	/**
	 * 获取工单的当前步骤
	 * @param entityId
	 * @param busyId
	 * @return
	 */
	WorkflowDo getCurrentStep(Integer entityId, Integer busyId);

	/**
	 * 获取流程的第一步
	 * @param entityId
	 * @return
	 */
	WorkflowStep getFirstStep(Integer entityId);

	/**
	 * 创建流程的下一步
	 * @param actionInfo
	 * @return
	 */
	Map<String, Object> createNewCurrentStep(ActionInfo actionInfo);

	/**
	 * 结束流程
	 * @param id
	 * @param userId
	 * @return
	 */
	boolean completeEntry(Integer id, Integer userId);

	/**
	 * 获取当前步骤的所有动作
	 * @param sid
	 * @return
	 */
	List<WorkflowAction> getActionsForStep(Integer stepId);

	/**
	 * 初始化流程
	 * @param step
	 * @param entityId
	 * @param userId
	 * @param id
	 * @return
	 */
	boolean initialize(WorkflowStep step, Integer entityId, Integer userId, Integer id);

	/**
	 * 获取流程名称
	 * @param entityId
	 * @return
	 */
	String getWorkflowName(Integer entityId);

	/**
	 * 获取当前流程的工单状态
	 * @param entityId
	 * @param busyId
	 * @return
	 */
	int getEntryState(Integer entityId, Integer busyId);

	/**
	 * 改变流程的工单状态
	 * @param id
	 * @param step
	 * @param userId
	 * @param flag
	 * @return
	 */
	boolean changeEntryState(Integer id, WorkflowStep step, Integer userId, boolean flag);

	/**
	 * 开启流程
	 * @param entity
	 * @param baseModel
	 * @param userId
	 * @return
	 */
	String startWorkflow(Integer entityId, Integer id, Integer userId);

	/**
	 * 
	 * @param entityId
	 * @param busyId
	 * @return
	 */
	List<WorkflowDo> currentWorkflows(Integer entityId, Integer busyId);

	/**
	 * 获取流程的所有步骤
	 * @param entityId
	 * @return
	 */
	List<WorkflowStep> getWorkflowSteps(Integer entityId);

	/**
	 * 获取流程的所有步骤
	 * @param entityId
	 * @return
	 */
	Map<Integer, WorkflowStep> getWorkflowStepsMap(Integer entityId);

	/**
	 * 获取流程详情
	 * @param entityId
	 * @param busyId
	 * @return
	 */
	WorkflowDetail getWorkflowDetail(Integer entityId, Integer busyId);

	/**
	 * 撤回流程
	 * @param doId
	 * @param userId
	 * @return
	 */
	boolean cancelWorkflow(Integer doId, Integer userId);

	/**
	 * 是否可以撤回流程
	 * @param entityId
	 * @param busyId
	 * @return
	 */
	boolean canCancel(Integer entityId, Integer busyId);
	
	/**
	 * 生成当前流程的流程图
	 * @param userMap
	 * @param steps
	 * @return
	 */
	List<ProcessPoint> buildProcess(Map<Integer, String> userMap, List<WorkflowStep> steps);
	
	/**
	 * 点亮截止当前步骤
	 * @param list
	 * @param flowDo
	 * @param workflowModel
	 */
	void lightProcess(List<ProcessPoint> list, WorkflowDo flowDo, WorkflowModel workflowModel);
	
	/**
	 * 转换审批进度中的步骤名称
	 * @param userMap
	 * @param flowDos
	 * @param workflowModel
	 */
	void changeStepName(Map<Integer, String> userMap, List<WorkflowDo> flowDos, WorkflowModel workflowModel);
	
	/**
	 * 获取流程的当前状态
	 * @param entityId
	 * @param busyId
	 * @param userId
	 * @return
	 */
	Integer currentStatus(Integer entityId, Integer busyId, Integer userId);
	
	/**
	 * 获取当前用户在当前流程中的身份
	 * @param entityId
	 * @param userId
	 * @return
	 */
	Integer workflowPart(Integer entityId, Integer userId);
	
	/**
	 * 获取当前处理人
	 * @return
	 */
	String getCurrentDealer();
	
	/**
	 * 根据code获取流程ID
	 * @param entityCode
	 * @return
	 */
	int getEntityId(String entityCode);
}