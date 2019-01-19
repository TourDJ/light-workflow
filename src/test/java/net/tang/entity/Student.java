package net.tang.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class Student implements Serializable {

	private static final long serialVersionUID = 367755847177069762L;

	private String name;
	
	private int age;
	
	private int score;
	
	private String remark;
}
