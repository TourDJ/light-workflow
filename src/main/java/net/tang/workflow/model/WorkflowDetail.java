package net.tang.workflow.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.tang.workflow.page.PageModel;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("workflow_detail")
public class WorkflowDetail extends PageModel {

	private static final long serialVersionUID = 1L;
	
	@TableField("entity_id")
	private Integer entityId;
	
	private Integer step;
	
	@TableField("step_name")
	private String stepName;
	
	@TableField("workflow_status")
	private Integer workflowStatus;
	
	@TableField("busy_id")
	private Integer busyId;
	
    @TableField("create_user")
    private Integer createUser;
}
