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

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.baomidou.mybatisplus.extension.api.R;

import net.tang.entity.User;
import net.tang.workflow.model.WorkflowDo;
import net.tang.workflow.model.WorkflowEntity;

/**
 * 工作流程业务的标准实现。实现了一些通用的流程业务，个性业务需要单独实现。
 */
public abstract class StandardWorkflowController extends WorkflowResourceProvider implements WorkflowBusiness {
	
	/**
	 * 获取流程id
	 * @param entityCode
	 * @return
	 */
	@GetMapping("/entity")
	public R<Integer> getEntityId(@RequestParam("entityCode") String entityCode) {
		WorkflowEntity entity = entityService.getEntity(entityCode);
		return R.data(entity.getId());
	}
    
	/**
	 * 获取流程某一步的所有可执行动作
	 * @param stepId the step id
	 * @return
	 */
    @GetMapping("/actions")
	public R<List<Map<String, String>>> getActions(@RequestParam("stepId") Integer stepId) {
		List<Map<String, String>> list = GenericWorking.warpAction(workflow.getActionsForStep(stepId));
		return R.data(list);
	}

    /**
     * 获取流程当前步骤的信息
     * 
     * @param entityCode the workflow's entity id, it's sole in one application.
     * @param busyId the business workflow's id
     * @return
     */
	@GetMapping("/current")
	public R<WorkflowDo> getCurrentStep(@RequestParam("entityCode") String entityCode, @RequestParam("busyId") Integer busyId) {
		WorkflowDo flowDo = workflow.getCurrentStep(workflow.getEntityId(entityCode), busyId);
		return R.data(flowDo);
	}

	/**
	 * 获取当前用户在该流程中的身份，包括申请人和处理人
	 * 
	 * @param entityCode the workflow's entity id, it's sole in one application.
	 * @param bladeUser
	 * @return
	 */
	@GetMapping("/part")
	public R<Integer> participant(@RequestParam("entityCode") String entityCode, User user) {
		Integer ident = workflow.workflowPart(workflow.getEntityId(entityCode), user.getUserId());
		return R.data(ident);
	}

}
