package net.tang.workflow.service;

import java.util.Map;

import net.tang.workflow.core.ActionInfo;
import net.tang.workflow.core.Workflow;

public interface WorkflowService {

	Map<String, Object> doAction0(Workflow workflow, ActionInfo actionInfo);
	
	boolean cancel0(Workflow workflow, ActionInfo actionInfo);
	
}
