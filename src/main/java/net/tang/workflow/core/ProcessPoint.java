package net.tang.workflow.core;

import lombok.Data;

/**
 * 业务流程中的每一步实体.
 * @author tang
 *
 */
@Data
public class ProcessPoint {

	/**
	 * 步骤id
	 */
	private int step;
	
	/**
	 * 步骤名称
	 */
	private String stepName;
	
	/**
	 * 是否处理
	 */
	private boolean process;
}
