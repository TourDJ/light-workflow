package net.tang.workflow.model;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.tang.workflow.page.PageModel;

@Data
@EqualsAndHashCode(callSuper=true)
@TableName("workflow_entity")
public class WorkflowEntity extends PageModel {

	private static final long serialVersionUID = 1L;
	
	private String code;
	
	private String name;
}
