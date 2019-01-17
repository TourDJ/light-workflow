package net.tang.workflow.model;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
@TableName("workflow_entity")
public class WorkflowEntity extends SystemModel {

	private static final long serialVersionUID = 1L;
	
	private String code;
	
	private String name;
}
