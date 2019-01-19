package net.tang.workflow.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.tang.workflow.mapper.WorkflowActionMapper;
import net.tang.workflow.model.WorkflowAction;
import net.tang.workflow.page.WrapperProcess;

@Service
public class WorkflowActionService extends ServiceImpl<WorkflowActionMapper, WorkflowAction> {
	
	/**
	 * 查询某一流程步骤的所有流程动作
	 * @param sid
	 * @return
	 */
	public List<WorkflowAction> getAvailableActionsForStep(int stepId) {
		List<WorkflowAction> actions = null;
		IPage<WorkflowAction> pages = new Page<>(1, 10);
		WorkflowAction action = new WorkflowAction();
		action.setSid(stepId);
		QueryWrapper<WorkflowAction> qw = new QueryWrapper<WorkflowAction>(action);
		WrapperProcess.ensureStatus(qw, null);
		this.page(pages, qw);

		if (pages != null)
			actions = pages.getRecords();

		return actions;
	}
}
