package net.tang.core.tools;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class Wrapper<T> implements Serializable {

	private static final long serialVersionUID = -4911762250330643779L;
	
	private int status;
	private String message;
	private T data;
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public static <T> Wrapper<T> data(T data){
		return new Wrapper<T>(200, "success", data);
	}
	
	/**
	 * 
	 * @param bool
	 * @return
	 */
	public static Wrapper<Boolean> status(Boolean bool){
		return data(bool);
	}
}
