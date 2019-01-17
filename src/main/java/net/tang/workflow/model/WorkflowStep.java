package net.tang.workflow.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @author service1
 *
 */
@Data
@EqualsAndHashCode(callSuper=true)
@TableName("workflow_step")
public class WorkflowStep extends SystemModel {

	private static final long serialVersionUID = 1L;

	private Integer entity;
	
	@TableField("entity_name")
	private String entityName;
	
	private Integer step;
	
	@TableField("step_name")
	private String stepName;
	
	private String deal;
	
	private Integer flag;
	
	@TableField("deal_arr")
	private String dealArr;
	
	@TableField("deal_name")
	private String dealName;
}
