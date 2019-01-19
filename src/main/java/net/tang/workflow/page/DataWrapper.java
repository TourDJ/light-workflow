package net.tang.workflow.page;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class DataWrapper<T> implements Serializable {

	private static final long serialVersionUID = -4911762250330643779L;
	
	private int status;
	
	private String message;
	
	private T data;
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public static <T> DataWrapper<T> data(T data) {
		return new DataWrapper<T>(200, "success", data);
	}
	
	/**
	 * 
	 * @param bool
	 * @return
	 */
	public static DataWrapper<Boolean> status(Boolean bool){
		return data(bool);
	}
}
