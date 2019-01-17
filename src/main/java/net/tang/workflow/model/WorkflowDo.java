package net.tang.workflow.model;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.tang.tools.DatePattern;

/**
 * 流程操作实体
 * @author service1
 *
 */
@Data
@EqualsAndHashCode(callSuper=true)
@TableName("workflow_do")
public class WorkflowDo extends SystemModel {

	private static final long serialVersionUID = 1L;

	@TableField("entity_id")
	private Integer entityId;
	
	private Integer step;
	
	@TableField("step_name")
	private String stepName;
	
	private Integer deal;
	
	@TableField("deal_time")
	@DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
	@JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
	private LocalDateTime dealTime;
	
	private String remark;
	
	@TableField("busy_id")
	private Integer busyId;

	private Integer action;
	
	@TableField("action_name")
	private String actionName;
	
	@TableField("detail_id")
	private Integer detailId;
	
	private Integer sid;

}
