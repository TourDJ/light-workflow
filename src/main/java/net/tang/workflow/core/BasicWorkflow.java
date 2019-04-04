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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tang.workflow.WorkflowConstants;
import net.tang.workflow.business.WorkflowModel;
import net.tang.workflow.model.WorkflowAction;
import net.tang.workflow.model.WorkflowDetail;
import net.tang.workflow.model.WorkflowDo;
import net.tang.workflow.model.WorkflowEntity;
import net.tang.workflow.model.WorkflowStep;
import net.tang.workflow.service.WorkflowActionService;
import net.tang.workflow.service.WorkflowDetailService;
import net.tang.workflow.service.WorkflowDoService;
import net.tang.workflow.service.WorkflowEntityService;
import net.tang.workflow.service.WorkflowStepService;

/**
 * 基本流程实现。
 * @author service1
 * @since 2018
 */
public class BasicWorkflow implements Workflow {

	
	/**
	 * 流程动作服务
	 */
	private WorkflowActionService actionService;
	
	/**
	 * 流程步骤服务
	 */
	private WorkflowStepService stepService;
	
	/**
	 * 流程流水服务
	 */
	private WorkflowDoService workflowDoService;
	
	/**
	 * 流程详细服务
	 */
	private WorkflowDetailService workflowDetailService;
	
	/**
	 * 流程实体服务
	 */
	private WorkflowEntityService entityService;
	
	/**
	 * 实体业务名称
	 */
	private String entityName;
	
	/**
	 * 点亮长度
	 */
	private int light = 0;
	
	/**
	 * 流程详情
	 */
	private WorkflowDetail detail;
	
	/**
	 * 当前处理人
	 */
	private String currentDealer;

	/**
	 * 获取流程实例
	 * @return
	 */
	public static Workflow getInstance() {
		return new BasicWorkflow();
	}

	/**
	 * 工作流初始化
	 * @param actionService
	 * @param stepService
	 * @param workflowDoService
	 * @param workflowDetailService
	 * @param entityService
	 * @return
	 */
	public Workflow init(WorkflowActionService actionService, WorkflowStepService stepService,
                         WorkflowDoService workflowDoService, WorkflowDetailService workflowDetailService,
                         WorkflowEntityService entityService) {
		this.actionService = actionService;
		this.stepService = stepService;
		this.workflowDoService = workflowDoService;
		this.workflowDetailService = workflowDetailService;
		this.entityService = entityService;
		return this;
	}

	/**
	 * 获取下一步
	 * @param entityId
	 * @param current
	 * @return
	 */
	@Override
	public WorkflowStep getNextStep(Integer entityId, Integer current) {
		return getWantedStep(entityId, current, 1);
	}

	/**
	 * 获取上一步
	 * @param entityId
	 * @param current
	 * @return
	 */
	@Override
	public WorkflowStep getPrevStep(Integer entityId, Integer current) {
		return getWantedStep(entityId, current, -1);
	}

	/**
	 * 获取指定的步骤
	 * @param entityId
	 * @param current
	 * @param interval
	 * @return
	 */
	@Override
	public WorkflowStep getWantedStep(Integer entityId, Integer current, Integer interval) {
		WorkflowStep step = null;
		current = current == null ? 1 : current;
		interval = interval == null ? 0 : interval;
		Integer stepId = current + interval;
		if (stepId <= 0)
			stepId = 1;
		
		Map<Integer, WorkflowStep> stepMap = getWorkflowStepsMap(entityId);
		if(stepMap != null)
			step = stepMap.get(stepId);
		return step;
	}

	/**
	 * 获取工单的当前步骤
	 * @param entityId
	 * @param busyId
	 * @return
	 */
	@Override
	public WorkflowDo getCurrentStep(Integer entityId, Integer busyId) {
		return workflowDoService.getCurrentStep(entityId, busyId);
	}

	/**
	 * 获取流程的第一步
	 * @param entityId
	 * @return
	 */
	@Override
	public WorkflowStep getFirstStep(Integer entityId) {
		WorkflowStep step = null;
		List<WorkflowStep> steps = getWorkflowSteps(entityId);
		if (steps != null && steps.size() > 0)
			step = steps.get(0);
		return step;
	}
	
	/**
	 * 处理流程当前信息
	 * @param flowDo
	 * @param actionInfo
	 * @return
	 */
	public boolean dealCurrentAction(WorkflowDo flowDo, ActionInfo actionInfo) {
		boolean flag = false;
		if(flowDo != null && actionInfo != null) {
			Integer userId = actionInfo.getUserId();
			String remark = actionInfo.getRemark();
			Integer actionId = actionInfo.getActionId();
			String actionName = actionInfo.getActionName();
			flowDo.setDeal(userId);
			flowDo.setDealTime(LocalDateTime.now());
			flowDo.setRemark(remark);
			flowDo.setAction(actionId);
			flowDo.setActionName(actionName);
			flowDo.setStatus(1);
			flag = workflowDoService.updateById(flowDo);	
		}
		return flag;
	}
	
	/**
	 * 创建流程的下一步
	 * @param actionInfo
	 * @return
	 */
	@Override
	public Map<String, Object> createNewCurrentStep(ActionInfo actionInfo) {
		boolean flag = false;
		Map<String, Object> map = new HashMap<>();
		if(actionInfo != null) {
			String actionCode = actionInfo.getActionCode();
			Boolean b = WorkflowConstants.actionMap.get(actionCode);
			
			if(actionCode == null)
				b = true;
			
			if(b != null) {
				Integer doId = actionInfo.getDoId();
				if(doId != null) {
					WorkflowDo flowDo = workflowDoService.getById(doId);
					if (flowDo != null && flowDo.getStatus() == 0) {
						flag = dealCurrentAction(flowDo, actionInfo);
						if (flag) {
							map.put("id", flowDo.getBusyId());
							if(b) {
								map.put("reject", false);
								flag = resolveAction(map, flowDo, actionInfo);
							} else {
								int detailId = flowDo.getDetailId();
								int userId = actionInfo.getUserId();
								flag = rejectAction(map, detailId, userId);
							}
						}
					}
				}
			}
			map.put("flag", flag);
		}
		return map;
	}

	/**
	 * 流程拒绝动作
	 * @param map
	 * @param detailId
	 * @param userId
	 * @return
	 */
	private boolean rejectAction(Map<String, Object> map, int detailId, int userId) {
		map.put("reject", true);
		return changeEntryState(detailId, null, userId, false);
	}

	/**
	 * 流程接收动作
	 * @param map
	 * @param flowDo
	 * @param actionInfo
	 * @return
	 */
	private boolean resolveAction(Map<String, Object> map, WorkflowDo flowDo, ActionInfo actionInfo) {
		boolean flag = false;
		if(flowDo != null && actionInfo != null) {
			Integer entityId = flowDo.getEntityId();
			Integer busyId = actionInfo.getBusyId();
			Integer userId = actionInfo.getUserId();
			Integer detailId = flowDo.getDetailId();
			WorkflowDo workflowDo = null;
			WorkflowStep nextStep = getNextStep(entityId, flowDo.getStep());
			if (nextStep != null) {
				
				getWorkflowDetail(entityId, busyId);
				
				//find out the next step dealers
				//by role, department or user's id
				//depend on the configure
				parseDealer(nextStep, null);
				
				if(currentDealer != null && !"".equals(currentDealer)) {
					String[] deals = currentDealer.split(",");
					for (int i = 0, len = deals.length; i < len; i++) {
						workflowDoService.setDeal(Integer.parseInt(deals[i]));
						workflowDo = workflowDoService.wrapEntity(entityId, nextStep, userId, flowDo.getBusyId(), detailId);
						workflowDo.setDetailId(detailId);
						flag = workflowDoService.save(workflowDo);
					}
					if (flag) {
						map.put("step", nextStep);
						flag = changeEntryState(detailId, nextStep, userId, true);
					}
				}
			} else {
				flag = completeEntry(detailId, userId);
			}
		}
		return flag;
	}

	/**
	 * 结束流程
	 * @param id
	 * @param userId
	 * @return
	 */
	@Override
	public boolean completeEntry(Integer id, Integer userId) {
		return workflowDetailService.finishWorkflow(id, userId);
	}

	/**
	 * 获取当前步骤的所有动作
	 * @param sid
	 * @return
	 */
	@Override
	public List<WorkflowAction> getActionsForStep(Integer stepId) {
		return actionService.getAvailableActionsForStep(stepId);
	}

	/**
	 * 初始化流程
	 * @param step
	 * @param entityId
	 * @param userId
	 * @param id
	 * @return
	 */
	@Override
	public boolean initialize(WorkflowStep step, Integer entityId, Integer userId, Integer busyId) {
		boolean flag = false;
		getWorkflowDetail(entityId, busyId);
		if (detail == null) {
			flag = initialize0(step, entityId, userId, busyId);
		} else {
			WorkflowDo workflowDo = getCurrentStep(entityId, busyId);
			if(workflowDo == null && step != null) {
				flag = initialize2(step, entityId, userId, busyId/*, detail*/);
			}
		}
		return flag;
	}

	/**
	 * 初始化流程工单详情
	 * @param step
	 * @param entityId
	 * @param userId
	 * @param busyId
	 * @return
	 */
	private boolean initialize0(WorkflowStep step, int entityId, int userId, Integer busyId) {
		 boolean flag = false;
		if (step != null) {
			detail = workflowDetailService.preserveWorkflowDetail(entityId, step, userId, busyId);
			if (detail != null) {
				flag = initialize2(step, entityId, userId, busyId);
			}
		}
		return flag;
	}

	/**
	 * 初始化流程工单流水
	 * @param step
	 * @param entityId
	 * @param userId
	 * @param busyId
	 * @param detail
	 * @return
	 */
	private boolean initialize2(WorkflowStep step, Integer entityId, Integer userId, Integer busyId) {
		boolean flag = false;
		if(/*detail != null && */step != null) {
			int detailId = detail.getId();
			flag = workflowDoService.preserveWorkflowDo(entityId, step, userId, busyId, detailId, currentDealer);
		}
		return flag;
	}

	/**
	 * 获取流程名称
	 * @param entityId
	 * @return
	 */
	@Override
	public String getWorkflowName(Integer entityId) {
		WorkflowEntity entity = entityService.getById(entityId);
		if (entity != null) {
			entityName = entity.getName();
		}
		return entityName;
	}

	/**
	 * 获取当前流程的工单状态
	 * @param entityId
	 * @param busyId
	 * @return
	 */
	@Override
	public int getEntryState(Integer entityId, Integer busyId) {
		return workflowDetailService.getEntryState(entityId, busyId);
	}

	/**
	 * 改变流程的工单状态
	 * @param id
	 * @param step
	 * @param userId
	 * @param flag
	 * @return
	 */
	@Override
	public boolean changeEntryState(Integer id, WorkflowStep step, Integer userId, boolean flag) {
		Integer status = 1;
		if(flag)
			status = 2;
		return workflowDetailService.dealWorkflowDetail(id, status, step, userId);
	}

	/**
	 * 开启流程
	 * @param entity
	 * @param baseModel
	 * @param userId
	 * @return
	 */
	@Override
	public String startWorkflow(Integer entityId, Integer id, Integer userId) {
		String deal = null;
		if (entityId != null && id != null && userId != null) {
			WorkflowStep step = getFirstStep(entityId);

			if (step != null) {
				parseDealer(step, userId);
				
				if (initialize(step, entityId, userId, id)) {
					deal = currentDealer;
				}
			}
		}
		return deal;
	}

	/**
	 * 查找处理人
	 * @param step
	 * @param userId
	 */
	private void parseDealer(WorkflowStep step, Integer userId) {
		
		if(step != null) {
			HandlingType flag = WorkflowConstants.workflowUse;
			
			switch (flag) {
			case ID:
				currentDealer = step.getDeal();
				break;
				
			case ROLE:
				if(userId == null && detail != null)
					userId = detail.getCreateUser();
				//currentDealer = stepService.parseDealByRole(step, userId);
				break;
				
			case DEPARTMENT:
				//currentDealer = stepService.parseDealByDept(step);
				break;

			default:
				currentDealer = "";
				break;
			}
		}
		
	}

	/**
	 * 查看历史步骤
	 * @param entityId
	 * @param busyId
	 * @return
	 */
	@Override
	public List<WorkflowDo> currentWorkflows(Integer entityId, Integer busyId) {
		return workflowDoService.currentWorkflows(entityId, busyId);
	}

	/**
	 * 获取流程的所有步骤
	 * @param entityId
	 * @return
	 */
	@Override
	public List<WorkflowStep> getWorkflowSteps(Integer entityId) {
		return stepService.getEntitySteps(entityId);
	}

	/**
	 * 获取流程的所有步骤，以map的形式
	 * @param entityId
	 * @return
	 */
	@Override
	public Map<Integer, WorkflowStep> getWorkflowStepsMap(Integer entityId) {
		List<WorkflowStep> steps = stepService.getEntitySteps(entityId);
		Map<Integer, WorkflowStep> stepMap = null;
		if (steps != null && steps.size() > 0) {
			stepMap = new HashMap<>();
			for (WorkflowStep step : steps) {
				stepMap.put(step.getStep(), step);
			}
		}
		return stepMap;
	}

	/**
	 * 获取流程详情
	 * @param entityId
	 * @param busyId
	 * @return
	 */
	@Override
	public WorkflowDetail getWorkflowDetail(Integer entityId, Integer busyId) {
		detail = workflowDetailService.getCurrentDetail(entityId, busyId);
		return detail;
	}
	
	/**
	 * 撤回流程
	 * @param doId
	 * @param userId
	 * @return
	 */
	@Override
	public boolean cancelWorkflow(Integer doId, Integer userId) {
		boolean flag = false;
		WorkflowDo flowDo = workflowDoService.getById(doId);
		if(flowDo != null) {
			flowDo.setStatus(-1);
			flag = workflowDoService.updateById(flowDo);
		}
		return flag;
	}
	
	/**
	 * 是否可以撤回流程
	 * @param entityId
	 * @param busyId
	 * @return
	 */
	@Override
	public boolean canCancel(Integer entityId, Integer busyId) {
		boolean flag = false;
		if(entityId != null && busyId != null) {
			flag = getEntryState(entityId, busyId) == 0;
		}
		return flag;
	}


	/**
	 * 生成当前流程的流程图
	 * @param userMap
	 * @param steps
	 * @return
	 */
	@Override
	public List<ProcessPoint> buildProcess(Map<Integer, String> userMap, List<WorkflowStep> steps) {
		ProcessRoute processRoute = ProcessRoute.getInstance();
		
		if(steps != null) {
			ProcessPoint process = null;
			for (WorkflowStep step : steps) {
				if(step != null) {
					process = new ProcessPoint();
					process.setStep(step.getStep());
//					if(userMap != null)
//						process.setStepName(userMap.get(step.getDeal()));
//					else
						process.setStepName(step.getStepName());
					process.setProcess(false);
					processRoute.add(process);
				}
			}
			
			processRoute.addEnd();
		}
		return processRoute.get();
	}

	/**
	 * 点亮截止当前步骤
	 * @param list
	 * @param flowDo
	 * @param workflowModel
	 */
	@Override
	public void lightProcess(List<ProcessPoint> list, WorkflowDo flowDo, WorkflowModel workflowModel) {
		if(list != null) {
			if(workflowModel != null) {
				Integer status = workflowModel.getStatus();
				Integer workStatus = 0;
				
				try {
//					workStatus = Integer.parseInt(workflowModel.getWorkflowStatus());
				} catch (NumberFormatException e) {
					workStatus = 0;
				}
				
				if(flowDo == null) {
					if(status == 0) {
						if(workStatus != 2)
							light = 0;
					} else {
						light = list.size() - 1;
					}
				} else {
					light = flowDo.getStep();
				}
			}
			
			for (ProcessPoint process : list) {
				if(process != null) {
					if(process.getStep() <= light) {
						process.setProcess(true);
					}
				}
			}
		}
	}

	/**
	 * 转换审批进度中的步骤名称
	 * @param userMap
	 * @param flowDos
	 * @param workflowModel
	 */
	@Override
	public void changeStepName(Map<Integer, String> userMap, List<WorkflowDo> flowDos, WorkflowModel workflowModel) {
		if(flowDos != null) {
			Integer deal = null;
			String dealer = "";
			for (WorkflowDo workflowDo : flowDos) {
				if(workflowDo != null) {
					deal = workflowDo.getDeal();
					//if(deal == null && workflowDo.getStatus() == 0 && workflowModel != null) {
					//	deal = workflowModel.getAuditPerson();
					//}
					if(userMap != null && deal != null) {
						dealer = userMap.get(deal);
						if(dealer != null)
							workflowDo.setStepName(dealer);
					}
				}
			}
		}
	}
	
	/**
	 * 获取流程对于当前用户的当前状态
	 * @param entityId
	 * @param busyId
	 * @param userId
	 * @return
	 */
	@Override
	public Integer currentStatus(Integer entityId, Integer busyId, Integer userId) {
		return workflowDoService.currentStatus(entityId, busyId, userId);
	}

	/**
	 * 获取当前用户在当前流程中的身份
	 * @param entityId
	 * @param userId
	 * @return
	 */
	@Override
	public Integer workflowPart(Integer entityId, Integer userId) {
		return stepService.workflowPart(entityId, userId);
	}
	
	/**
	 * 获取当前处理人
	 * @return
	 */
	@Override
	public String getCurrentDealer() {
		return currentDealer;
	}

	/**
	 * 根据code获取流程ID
	 * @param entityCode
	 * @return
	 */
	@Override
	public int getEntityId(String entityCode) {
		WorkflowEntity entity = entityService.getEntity(entityCode);
		int entityId = -1;
		if(entity != null) {
			entityId = entity.getId();
		}
		return entityId;
	}
}
