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
package net.tang.workflow.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.tang.workflow.page.PageModel;

/**
 * Workflow's action model, describe the action's properties.
 * @author tang
 *
 */
@Data
@EqualsAndHashCode(callSuper=true)
@TableName("workflow_action")
public class WorkflowAction extends PageModel {

	private static final long serialVersionUID = 1L;

	/**
	 * action belong step's id
	 */
	private Integer sid;
	
	/**
	 * action belong step's name
	 */
	@TableField("step_name")
	private String stepName;
	
	/**
	 * action no
	 */
	private Integer action;
	
	/**
	 * action name
	 */
	@TableField("action_name")
	private String actionName;
	
	/**
	 * action code
	 */
	@TableField("action_code")
	private String actionCode;
	
	/**
	 * action belong entity's id
	 */
	private Integer entity;
	
	/**
	 * action belong entity's name
	 */
	@TableField("entity_name")
	private String entityName;
}
