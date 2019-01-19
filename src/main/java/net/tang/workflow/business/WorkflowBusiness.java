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

import net.tang.workflow.core.ActionInfo;
import net.tang.workflow.model.User;

/**
 * 流程业务接口
 * @author tang
 * 
 */
public interface WorkflowBusiness {
	
	/**
	 * Get workflow's entity id by entity code.
	 * @param entityCode
	 * @return
	 */
	Object getEntityId(String entityCode);
	  
	/**
	 * Return all executable actions of a workflow's step. A action represents the specific  
	 * behavior which you want execute, and it will contained by the workflow's step, e.g., yes, no etc.
	 * @param stepId the step id
	 * @return
	 */
	Object getActions(Integer stepId);

	/**
	 * Deal workflow's business logic.
	 * @param actionInfo
	 * @param bladeUser
	 * @return
	 */
	Object doAction(ActionInfo actionInfo, User user);

    /**
     * Return current in processing workflow's info. It indicate that where the progress 
     * in this process now.
     * 
     * Here now is not supported multiple flow info.
     * @param entityCode the workflow's entity id, it's sole in one application.
     * @param busyId the business workflow's id
     * @return
     */
	public Object getCurrentStep(String entityCode, Integer busyId);

	/**
	 * Obtain all executed steps of current workflow.
	 * @param busyId
	 * @return
	 */
	Object getHistorySteps(Integer busyId);

	/**
	 * Cancel the current step of current workflow.
	 * @param actionInfo
	 * @param bladeUser
	 * @return
	 */
	Object cancel(ActionInfo actionInfo, User user);

	/**
	 * Decide the current step of current workflow whether can be cancel.
	 * @param busyId
	 * @return
	 */
	Object status(Integer busyId);

	/**
	 * Draw the business workflow's move towards, which include the all steps.
	 * @param busyId
	 * @return
	 */
	Object process(Integer busyId);
	
	/**
	 * Query one participant's role in one specific workflow's business. The different participant's role 
	 * has different operate right.
	 * 
	 * One can be an applier in a process, and meanwhile can be an auditor in another process.
	 * @param entityCode the workflow's entity id, it's sole in one application.
	 * @param bladeUser
	 * @return
	 */
	Object participant(String entityCode, User user);

}
