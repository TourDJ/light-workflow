package net.tang.workflow.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.tang.workflow.mapper.WorkflowDetailMapper;
import net.tang.workflow.model.WorkflowDetail;
import net.tang.workflow.model.WorkflowStep;

@Service
public class WorkflowDetailService extends ServiceImpl<WorkflowDetailMapper, WorkflowDetail> {

	/**
	 * 获取流程详情
	 * @param entityId
	 * @param id
	 * @return
	 */
	public WorkflowDetail getCurrentDetail(Integer entityId, Integer id) {
		WorkflowDetail detail = null;
		
		if(entityId != null && id != null) {
			WorkflowDetail temp = new WorkflowDetail();
			temp.setStatus(0);
			temp.setBusyId(id);
			temp.setEntityId(entityId);
			QueryWrapper<WorkflowDetail> qw = new QueryWrapper<>(temp);
			detail = this.getOne(qw);
		}
		
		return detail;
	}
	
	/**
	 * 初始化流程详情
	 * @param entityId
	 * @param step
	 * @param userId
	 * @param id
	 * @return
	 */
	public WorkflowDetail preserveWorkflowDetail(Integer entityId, WorkflowStep step, Integer userId, Integer id) {
		WorkflowDetail detail = null;
		if(entityId != null && step != null && id != null) {
			detail = new WorkflowDetail();
			detail.setEntityId(entityId);
			detail.setStep(step.getStep());
			detail.setStepName(step.getStepName());
			detail.setWorkflowStatus(0);
			detail.setBusyId(id);
			detail.setStatus(0);
			detail.setCreateUser(userId);
			this.save(detail);
		}
		return detail;
	}
	
	/**
	 * 更新流程详情
	 * @param id
	 * @param status
	 * @param step
	 * @param userId
	 * @return
	 */
	public boolean dealWorkflowDetail(Integer id, int status, WorkflowStep step, Integer userId) {
		boolean flag = false;
		
		if(id != null && userId != null) {
			WorkflowDetail detail = this.getById(id);
			if(detail != null) {
				detail.setWorkflowStatus(status);
				detail.setCreateUser(userId);
				if(step != null) {
					detail.setStep(step.getId());
					detail.setStepName(step.getStepName());
				}
				if(status == 1)
					detail.setStatus(1);
				flag = this.updateById(detail);
			}
		}
		
		return flag;
	}
	
	/**
	 * 流程结束时设置工单状态
	 * @param id
	 * @param userId
	 * @return
	 */
	public boolean finishWorkflow(Integer id, Integer userId) {
		return dealWorkflowDetail(id, 0, null, userId);
	}
	
	/**
	 * 获取当前流程的工单状态
	 * @param entityId
	 * @param busyId
	 * @return
	 */
	public int getEntryState(Integer entityId, Integer busyId) {
		int result = 0;
		if(entityId != null && busyId != null) {
			WorkflowDetail temp = new WorkflowDetail();
			temp.setBusyId(busyId);
			temp.setEntityId(entityId);
			QueryWrapper<WorkflowDetail> wrapper = new QueryWrapper<>(temp);
			WorkflowDetail detail = this.getOne(wrapper);
			if (detail != null) {
				result = detail.getWorkflowStatus();
			}
		}
		return result;
	}
}
