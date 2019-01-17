package net.tang.workflow;

public class WorkflowException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WorkflowException() {
		
	}
	
	public WorkflowException(String error) {
		super(error);
	}
}
