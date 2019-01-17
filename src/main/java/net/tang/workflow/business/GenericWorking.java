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
package net.tang.workflow.business;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.tang.core.tools.CommonUtils;
import net.tang.service.UserService;
import net.tang.workflow.core.ActionInfo;
import net.tang.workflow.core.ActionResult;
import net.tang.workflow.core.BasicWorkflow;
import net.tang.workflow.core.ProcessPoint;
import net.tang.workflow.core.Workflow;
import net.tang.workflow.model.WorkflowAction;
import net.tang.workflow.model.WorkflowDo;
import net.tang.workflow.model.WorkflowStep;
import net.tang.workflow.service.WorkflowActionService;
import net.tang.workflow.service.WorkflowDetailService;
import net.tang.workflow.service.WorkflowDoService;
import net.tang.workflow.service.WorkflowEntityService;
import net.tang.workflow.service.WorkflowStepService;

/**
 * 流程通用处理类
 *
 */
public class GenericWorking {
	
	private GenericWorking() {
		
	}

	/**
	 * 获取流程类对象
	 * @param actionService
	 * @param stepService
	 * @param doService
	 * @param detailService
	 * @param entityService
	 * @return
	 */
	public static Workflow getWorkflow(WorkflowActionService actionService, WorkflowStepService stepService,
			WorkflowDoService doService, WorkflowDetailService detailService, WorkflowEntityService entityService) {
		return BasicWorkflow.getInstance().init(actionService, stepService, doService, detailService, entityService);
	}
	
	/**
	 * 封装流程动作
	 * 
	 * @param actions
	 * @return
	 */
	public static List<Map<String, String>> warpAction(List<WorkflowAction> actions) {
		List<Map<String, String>> list = null;
		Map<String, String> map = null;
		if (actions != null) {
			list = new ArrayList<>();
			Collections.sort(actions, new Comparator<WorkflowAction>() {

				@Override
				public int compare(WorkflowAction o1, WorkflowAction o2) {
					return o1.getAction().compareTo(o2.getAction());
				}
			});
			for (WorkflowAction workflowAction : actions) {
				map = new HashMap<>();
				map.put("action", workflowAction.getAction().toString());
				map.put("actionName", workflowAction.getActionName());
				map.put("actionCode", workflowAction.getActionCode());
				list.add(map);
			}
		}

		return list;
	}

	/**
	 * 获取流程id
	 * @param workflow
	 * @param service
	 * @return
	 */
	private static Integer getEntityId(Workflow workflow, BusinessService service) {
		String workflowCode = service.getWorkflowCode();
		return workflow.getEntityId(workflowCode);
	}
	
	/**
	 * 是否可以撤回
	 * @param workflow
	 * @param baseModel
	 * @param entityId
	 * @param busyId
	 * @return
	 */
	public static boolean canCancel(Workflow workflow, Integer busyId, BusinessService service) {
		boolean flag = false;
		if(workflow != null) {
			Integer entityId = getEntityId(workflow, service);
			WorkflowModel workflowModel = service.getModel(busyId);
			flag = workflow.canCancel(entityId, busyId) && !isDraft(workflowModel);
		}
		return flag;
	}
	
	/**
	 * 处理之前检查一下当前步骤的状态
	 * @param workflowModel 流程基础模型
	 * @return
	 */
	public static ActionResult dealBeforeCheck(WorkflowModel workflowModel) {
		ActionResult result = null;
		if(workflowModel != null) {
			//确保该流程没有被返回
//			if(workflowModel.getStatus() == BUSY_ORDER_STATUS_DRAFT &&
//					workflowModel.getWorkflowStatus().equals(BUSY_WORKFLOW_STATUS_DRAFT)) {
//				result = ActionResult.instance(true, "该申请已经被撤回！本页面将在3秒后自动跳转", false);
//			}
		}
		return result;
	}
	
	/**
	 * 流程状态设置
	 * @param map
	 * @param workflowModel
	 * @return
	 */
	public static String dealAction(Map<String, Object> map, WorkflowModel workflowModel) {
		String message = "操作失败";
		if(map != null && workflowModel != null) {
			workflowModel.setAuditTime(LocalDateTime.now());
			WorkflowStep step = (WorkflowStep) map.get("step");
			boolean reject = (boolean) map.get("reject");
//			if (reject) {
//				workflowModel.setWorkflowStatus(BUSY_WORKFLOW_STATUS_FINISH);
//				workflowModel.setStatus(BUSY_ORDER_STATUS_DRAFT);
//				message = "流程被拒绝";
//			} else if (step != null) {
//				message = "流程进入下一步";
//			} else {
//				workflowModel.setWorkflowStatus(BUSY_WORKFLOW_STATUS_FINISH);
//				workflowModel.setStatus(BUSY_ORDER_STATUS_COMPLETE);
//				message = "流程结束";
//			}
		}
		return message;
	}	
	
	/**
	 * 增加 userId
	 * @param actionInfo
	 * @param bladeUser
	 */
	public static void addUserId(ActionInfo actionInfo, Integer userId) {
		if(actionInfo != null)
			actionInfo.setUserId(userId);
	}
	

	/**
	 * 是否是草稿状态
	 * 
	 * @param workflowModel
	 * @return
	 */
	public static boolean isDraft(WorkflowModel workflowModel) {
		Objects.requireNonNull(workflowModel);
		
		return workflowModel.getStatus() == 0;
	}

	/**
	 * 发起流程业务
	 * @param noticeService
	 * @param menuService
	 * @param workflowModel
	 * @param userId
	 * @param workflow
	 * @param workflowCode
	 * @return
	 */
	public static boolean beginWorkflow(NoticeService noticeService, MenuService menuService, WorkflowModel workflowModel,
			Integer userId, Workflow workflow, BusinessService service) {
		boolean flag = false;
		int id = workflowModel.getId();
		if (id > 0) {
			Integer entityId = getEntityId(workflow, service);
			String deal = workflow.startWorkflow(entityId, id, userId);
			if (deal != null && !"".equals(deal)) {
				//给审核人发送消息
				sendNotice(noticeService, menuService, workflow, entityId, deal);
				
				workflowModel.setAuditPerson(deal);
				flag = service.updateModel(workflowModel);
			}
		}
		return flag;
	}

	/**
	 * 流程发起成功后，发送消息提醒
	 * @param noticeService
	 * @param menuService
	 * @param workflow
	 * @param entityId
	 * @param deal
	 */
	private static void sendNotice(NoticeService noticeService, MenuService menuService, Workflow workflow,
			int entityId, String deal) {
		if(deal != null) {
			String[] deals = deal.split(",");
			for (int i = 0, len = deals.length; i < len; i++) {
				Integer dealer = CommonUtils.parseInt(deals[i]);
				if(dealer > 0) {
					String entityName = workflow.getWorkflowName(entityId);
					String url = menuService.getUrl(entityName);
					noticeService.sendNotice(entityName, url, dealer, NOTICE_CLASSIFY_AUDIT_REMIND);
				}
			}
		}
	}

	/**
	 * 设置流程是否可操作的状态信息
	 * @param workflow
	 * @param workflowModel
	 * @param id
	 * @param userId
	 * @param service
	 */
	public static void setOperateStatus(Workflow workflow, WorkflowModel workflowModel, Integer id, Integer userId, BusinessService service) {
		Integer entityId = getEntityId(workflow, service);
		Integer status = workflow.currentStatus(entityId, id, userId);
		workflowModel.setAstatus(status);
	}
	
	/**
	 * 审核人查询时设置查询信息
	 * @param workflowModel
	 * @param query
	 * @param workflow
	 * @param userId
	 * @param service
	 */
	public static void setAuditQueryInfo(WorkflowModel workflowModel, Workflow workflow, 
						Integer userId, BusinessService service) {
		int status = BUSY_WORKFLOW_DEAL_ONGING;
		if(BUSY_WORKFLOW_STATUS_PROGRESS.equals(workflowModel.getWorkflowStatus()))
			status = BUSY_WORKFLOW_DEAL_ONGING;
		else
			status = BUSY_WORKFLOW_DEAL_END;
		
		Integer entityId = getEntityId(workflow, service);
		workflowModel.setApplyTime();
		workflowModel.setAuditTime();
		workflowModel.setEntityId(entityId);
		workflowModel.assembling(10, 1);
	}

	/**
	 * 检查流程状态
	 * @param workflowModel
	 */
	public static void checkWorkstatus(WorkflowModel workflowModel) {
		if(workflowModel.getWorkflowStatus() == null || "".equals(workflowModel.getWorkflowStatus()))
			workflowModel.setWorkflowStatus(BUSY_WORKFLOW_STATUS_PROGRESS);
	}

	/**
	 * 获取操作人的查询角色
	 * @param workflow
	 * @param userId
	 * @param service
	 * @return
	 */
	public static int operatePart(Workflow workflow, Integer userId, BusinessService service) {
		Integer entityId = getEntityId(workflow, service);
		int part = workflow.workflowPart(entityId, userId);
		//判断是否是管理员
//		if(CommonUtils.isAdmin()) {
//			part = BUSY_USER_PART_ADMIN;
//		}
		return part;
	}

	/**
	 * 流程处理的核心逻辑
	 * @param noticeService
	 * @param menuService
	 * @param workflow
	 * @param actionInfo
	 * @param busiId
	 * @param entityCode
	 * @param service
	 * @return
	 */
	public static ActionResult dealWorkdlow(INoticeService noticeService, IMenuService menuService, Workflow workflow,
			ActionInfo actionInfo, Integer userId, BusinessService service) {
		ActionResult result = ActionResult.initial();
		addUserId(actionInfo, userId);
		//处理之前检查一下当前步骤的状态
		Integer busiId = actionInfo.getBusyId();
		if(busiId != null) {
			WorkflowModel workflowModel = service.getModel(busiId);
			if(workflowModel != null) {
				result = dealBeforeCheck(workflowModel);
				if(result == null) {
					result = dealBusiness(workflow, actionInfo, service);
					afterDealAction(noticeService, menuService, workflow, busiId, result, service);
				}
			}
		}
		return result;
	}

	/**
	 * 流程业务处理
	 * @param workflow
	 * @param actionInfo
	 * @param service
	 * @return
	 */
	private static ActionResult dealBusiness(Workflow workflow, ActionInfo actionInfo, BusinessService service) {
		ActionResult result = ActionResult.initial();
		boolean flag = result.isFlag();
		boolean status = result.isStatus();
		String message = result.getMessage();
		
		Map<String, Object> map = workflow.createNewCurrentStep(actionInfo);
		if (map != null) {
			flag = (boolean) map.get("flag");
			if (flag) {
				Integer id = (Integer) map.get("id");
				if (id != null) {
					WorkflowModel workflowModel = service.getModel(id);
					if(workflowModel != null) {
						message = dealAction(map, workflowModel);
						if (workflowModel.getStatus() == BUSY_ORDER_STATUS_ONGOING) {
							WorkflowStep step = (WorkflowStep) map.get("step");
							if(step != null) {
								String dealer = workflow.getCurrentDealer();
								workflowModel.setAuditPerson(dealer);
							}
						}
						
						flag = service.updateModel(workflowModel);
						if(flag)
							status = true;
					}
				}
			} else {
				message = "流程处理失败";
			}
		} else {
			message = "流程处理出现异常";
		}

		result.setFlag(flag);
		result.setMessage(message);
		result.setStatus(status);
		return result;
	}

	/**
	 * 流程处理后操作
	 * @param noticeService
	 * @param menuService
	 * @param workflow
	 * @param busiId
	 * @param entityCode
	 * @param result
	 * @param service
	 */
	private static void afterDealAction(INoticeService noticeService, IMenuService menuService, Workflow workflow,
			Integer busiId, ActionResult result, BusinessService service) {
		if(result != null) {
			boolean flag = (boolean) result.isFlag();
			if(flag) {
				WorkflowModel workflowModel = service.getModel(busiId);
				sendNoticeAfterDeal(noticeService, menuService, workflowModel, workflow, service);
			}
		}
	}

	/**
	 * 流程处理后发送消息
	 * @param noticeService
	 * @param menuService
	 * @param workflowModel
	 * @param workflow
	 * @param entityCode
	 */
	public static void sendNoticeAfterDeal(INoticeService noticeService, IMenuService menuService, WorkflowModel workflowModel, 
			Workflow workflow, BusinessService service) {
		if(workflowModel != null) {
			Integer userId = 1;
			Integer entityId = getEntityId(workflow, service);
			String entityName = workflow.getWorkflowName(entityId);
			String url = menuService.getUrl(entityName);
			if(workflowModel.getStatus() == 1 &&
					BUSY_WORKFLOW_STATUS_FINISH.equals(workflowModel.getWorkflowStatus().trim())) {
				
				//审批通过后发送消息
				noticeService.sendNotice(entityName, url, userId, NOTICE_CLASSIFY_APPLY_SUCCESS);
				
			} else if (workflowModel.getStatus() == BUSY_ORDER_STATUS_DRAFT &&
					BUSY_WORKFLOW_STATUS_FINISH.equals(workflowModel.getWorkflowStatus().trim())) {
				
				//审批拒绝后发送消息
				noticeService.sendNotice(entityName, url, userId, NOTICE_CLASSIFY_APPLY_FAIL);
				
			} else if (workflowModel.getStatus() == BUSY_ORDER_STATUS_ONGOING) {
				//给审核人发送消息
				String deal = workflowModel.getAuditPerson();
				String[] deals = null;
				if(deal != null) {
					deals = deal.split(",");
					for (int i = 0, len = deals.length; i < len; i++) {
						Integer dealer = CommonUtils.parseInt(deals[i]);
						if(dealer > 0) {
							noticeService.sendNotice(entityName, url, dealer, NOTICE_CLASSIFY_AUDIT_REMIND);
						}
					}
				}
			}
		}
	}

	/**
	 * 撤回流程
	 * @param workflow
	 * @param entityId
	 * @param actionInfo
	 * @param service
	 * @return
	 */
	public static boolean cancelWorkflow(Workflow workflow, Integer userId, ActionInfo actionInfo, BusinessService service) {
		boolean flag = false;
		if(actionInfo != null) {
			Integer busyId = actionInfo.getBusyId();
			Integer doId = actionInfo.getDoId();
			Integer entityId = getEntityId(workflow, service);
			if (busyId != null && doId != null && userId != null
					&& workflow.canCancel(entityId, busyId)) {
				flag = workflow.cancelWorkflow(doId, userId);
				if (flag) {
					WorkflowModel workflowModel = service.getModel(busyId);
					if (workflowModel != null) {
						workflowModel.setWorkflowStatus(1);
						workflowModel.setStatus(1);
						flag = service.updateModel(workflowModel);
						//清空申请时间
						if(flag)
							service.updateApplyTime(1);
					}
				}
			}
		}
		return flag;
	}
	
	/**
	 * 查看流程审批进度
	 * @param userService
	 * @param workflow
	 * @param busyId
	 * @param service
	 * @return
	 */
	public static List<WorkflowDo> workflowHistory(UserService userService, Workflow workflow, Integer busyId, BusinessService service) {
		Integer entityId = getEntityId(workflow, service);
		Map<Integer, String> userMap = userService.loginUsers();
		List<WorkflowDo> flowDos = null;
		if(busyId != null) {
			WorkflowModel workflowModel = service.getModel(busyId);
			flowDos = workflow.currentWorkflows(entityId, busyId);
			workflow.changeStepName(userMap, flowDos, workflowModel);
		}
		return flowDos;
	}
	
	/**
	 * 查看业务流程
	 * @param userService
	 * @param workflow
	 * @param busyId
	 * @param service
	 * @return
	 */
	public static List<ProcessPoint> workflowProcess(UserService userService, Workflow workflow, Integer busyId, BusinessService service) {
		Integer entityId = getEntityId(workflow, service);
		List<WorkflowStep> steps = workflow.getWorkflowSteps(entityId);
		Map<Integer, String> userMap = userService.loginUsers();
		List<ProcessPoint> list = workflow.buildProcess(userMap, steps);
		WorkflowDo flowDo = null;
		WorkflowModel workflowModel = null;
		if(busyId != null) {
			workflowModel = service.getModel(busyId);
			flowDo = workflow.getCurrentStep(entityId, busyId);
		}
		workflow.lightProcess(list, flowDo, workflowModel);
		return list;
	}
}
