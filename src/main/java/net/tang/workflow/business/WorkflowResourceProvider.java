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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import net.tang.workflow.core.Workflow;
import net.tang.workflow.service.WorkflowActionService;
import net.tang.workflow.service.WorkflowDetailService;
import net.tang.workflow.service.WorkflowDoService;
import net.tang.workflow.service.WorkflowEntityService;
import net.tang.workflow.service.WorkflowStepService;

/**
 * 基础服务提供者
 * @author service1
 *
 */
public class WorkflowResourceProvider {

	/** The workflow's step service */
	@Autowired
	public WorkflowStepService stepService;
	
	/** The workflow's action service */
	@Autowired
	public WorkflowActionService actionService;
	
	/** The workflow's detail service */
	@Autowired
	public WorkflowDetailService detailService;
	
	/** The workflow's flow service */
	@Autowired
	public WorkflowDoService doService;
	
	/** The workflow's entity service */
	@Autowired
	public WorkflowEntityService entityService;
	
	@Autowired
	/** Workflow's instance */
	public Workflow workflow;
	
	/**
	 * Initial workflow's instance.
	 * @return
	 */
	@Bean("workflow")
	public Workflow initWorkflow() {
		return GenericWorking.getWorkflow(actionService, stepService, doService, detailService, entityService);
	}
}
