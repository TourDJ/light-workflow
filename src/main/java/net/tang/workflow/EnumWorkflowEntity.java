package net.tang.workflow;

/**
 * 流程实体枚举
 * @author service1
 *
 */
public enum EnumWorkflowEntity {
	WORKFLOW_ENTITY_RETURNS("returns"),
	WORKFLOW_ENTITY_CERTIFICATE("certificate");
	
	private final String code;
	
	EnumWorkflowEntity(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
}
