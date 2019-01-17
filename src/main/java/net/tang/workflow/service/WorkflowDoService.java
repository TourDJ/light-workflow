package net.tang.workflow.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.tang.tools.Pages;
import net.tang.workflow.mapper.WorkflowDoMapper;
import net.tang.workflow.model.WorkflowDo;
import net.tang.workflow.model.WorkflowStep;

/**
 * 流程处理流水服务
 * @author service1
 *
 */
@Service
public class WorkflowDoService extends ServiceImpl<WorkflowDoMapper, WorkflowDo> {
	
	// 当前处理人
	private int deal;
	
	private static final int BUSY_WORKFLOW_DEAL_CANCEL = -1;
	
	private static final int BUSY_ORDER_STATUS_DRAFT = 0;
	
	/**
	 * 设置当前处理人
	 * @param deal
	 */
	public void setDeal(int deal) {
		this.deal = deal;
	}

	/**
	 * 包装流程处理数据
	 * @param entityId
	 * @param step
	 * @param userId
	 * @param id
	 * @param detailId
	 * @return
	 */
	public WorkflowDo wrapEntity(Integer entityId, WorkflowStep step, Integer userId, Integer id, Integer detailId) {
		WorkflowDo workflowDo = new WorkflowDo();
		workflowDo.setEntityId(entityId);
		workflowDo.setStep(step.getStep());
		workflowDo.setStepName(step.getStepName());
		workflowDo.setDeal(deal);
		workflowDo.setStatus(BUSY_ORDER_STATUS_DRAFT);
		workflowDo.setBusyId(id);
		workflowDo.setSid(step.getId());
		workflowDo.setDetailId(detailId);
		return workflowDo;
	}

	/**
	 * 流程处理
	 * @param entityId
	 * @param step
	 * @param userId
	 * @param id
	 * @param detailId
	 * @return
	 */
	public boolean preserveWorkflowDo(Integer entityId, WorkflowStep step, Integer userId, Integer id, Integer detailId, String deal) {
		boolean flag = false;

		if (entityId != null && step != null && userId != null && id != null && deal != null) {
			String[] deals = deal.split(",");
			for (int i = 0, len = deals.length; i < len; i++) {
				setDeal(Integer.parseInt(deals[i]));
				WorkflowDo workflowDo = wrapEntity(entityId, step, userId, id, detailId);
				flag = this.save(workflowDo);
			}
		}

		return flag;
	}

	/**
	 * 获取流程的所有处理信息
	 * @param entityId
	 * @param busyId
	 * @return
	 */
	public List<WorkflowDo> currentWorkflows(Integer entityId, Integer busyId) {
		List<WorkflowDo> workflowDos = null;

		if(entityId != null && busyId != null) {
			WorkflowDo workflowDo = new WorkflowDo();
			workflowDo.setBusyId(busyId);
			workflowDo.setEntityId(entityId);
			QueryWrapper<WorkflowDo> qw = new QueryWrapper<WorkflowDo>(workflowDo);

			IPage<WorkflowDo> pages = Pages.getPage();
			this.page(pages, qw);

			if (pages != null)
				workflowDos = pages.getRecords();
			
			//撤回的记录不显示
			if(workflowDos != null) {
				for (Iterator<WorkflowDo> iterator = workflowDos.iterator(); iterator.hasNext();) {
					WorkflowDo w = iterator.next();
					if(w.getStatus() == BUSY_WORKFLOW_DEAL_CANCEL)
						iterator.remove();
				}
			}
		}

		return workflowDos;
	}
	
	/**
	 * 获取待处理的流程处理信息
	 * @param entityId
	 * @param busyId
	 * @return
	 */
	public WorkflowDo getCurrentStep(Integer entityId, Integer busyId) {
		WorkflowDo flowDo = null;
		if(entityId != null && busyId != null) {
			WorkflowDo workflowDo = new WorkflowDo();
			workflowDo.setEntityId(entityId);
			workflowDo.setBusyId(busyId);
			workflowDo.setStatus(BUSY_ORDER_STATUS_DRAFT);
			workflowDo.setDeal(null);
			workflowDo.setDealTime(null);
			QueryWrapper<WorkflowDo> qw = new QueryWrapper<>(workflowDo);
			List<WorkflowDo> dos = this.list(qw);
			flowDo = dos != null && dos.size() == 1 ? dos.get(0) : null;
		}
		return flowDo;
	}
	
	/**
	 * 获取流程当前状态
	 * @param entityId
	 * @param busyId
	 * @param userId
	 * @return
	 */
	public Integer currentStatus(Integer entityId, Integer busyId, Integer userId) {
		int status = 1;
		if(entityId != null && busyId != null && userId != null) {
			QueryWrapper<WorkflowDo> qw = new QueryWrapper<>();
			qw.setEntity(new WorkflowDo());
			qw.eq("entity_id", entityId);
			qw.eq("busy_id", busyId);
			qw.eq("deal", userId);
			qw.ne("status", BUSY_WORKFLOW_DEAL_CANCEL);
			List<WorkflowDo> dos = this.list(qw);
			if(dos != null) {
				for (WorkflowDo flowDo : dos) {
					if(flowDo.getStatus() == 0) {
						status = flowDo.getStatus();
						break;
					}
				}
			}
		}
		return status;
	}
}
