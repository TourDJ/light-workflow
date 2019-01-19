package net.tang.workflow.business;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.tang.workflow.page.PageModel;
import net.tang.workflow.tools.DatePattern;

/**
 * 流程基础模型类
 * @author service1
 *
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class WorkflowModel extends PageModel {

	private static final long serialVersionUID = -7448458619781743630L;

	/**
	 * 申请时间
	 */
	@TableField("apply_time")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
	private LocalDateTime applyTime;
	
	/**
	 * 审批人
	 */
	@TableField("audit_person")
	private String auditPerson;
	
	/**
	 * 审批时间
	 */
	@TableField("audit_time")
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
	private LocalDateTime auditTime;
	
	/**
	 * 审批状态
	 */
	@TableField("workflow_status")
	private Integer workflowStatus;

	
	// ----------------------------------- 条件查询用 -----------------------------------

	/**
	 * 审批状态(查询用)
	 */
	@TableField(exist=false)
	private Integer astatus;
	
	/**
	 * 审批人(查询用)
	 */
	@TableField(exist=false)
	private Integer processor;
	
	/**
	 * 流程实体id
	 */
	@TableField(exist=false)
	private Integer entityId;
	
	/**
	 * 申请时间字符串格式
	 */
	@TableField(exist=false)
	private String apply;
	/**
	 * 申请时间开始
	 */
	@TableField(exist=false)
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime applyTimeBegin;
	/**
	 * 申请时间结束
	 */
	@TableField(exist=false)
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime applyTimeEnd;
	
	/**
	 * 审批时间字符串格式
	 */
	@TableField(exist=false)
	private String audit;
	/**
	 * 审批时间开始 
	 */
	@TableField(exist=false)
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
	private LocalDateTime auditTimeBegin;
	/**
	 * 审批时间结束
	 */
	@TableField(exist=false)
    @DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
	private LocalDateTime auditTimeEnd;
	
	/**
	 * 
	 */
	private Integer status;
}
