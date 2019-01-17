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

import java.io.Serializable;

import lombok.Data;

/**
 * 流程动作执行实体
 *
 */
@Data
public class ActionInfo implements Serializable {

	private static final long serialVersionUID = 7304258208356507812L;
	
	/** workflow's flow id */
	private Integer doId;
	
	/** the opeator's id */
	private Integer userId;
	
	/** business's id */
	private Integer busyId;
	
	/** operate memo */
	private String  remark;
	
	/** id of what choose action */
	private Integer actionId;
	
	/** name of what choose action */
	private String actionName;
	
	/** code of what choose action */
	private String actionCode;
}
