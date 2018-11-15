package net.tang.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("t_city")
public class City implements Serializable {

	private static final long serialVersionUID = -1L;

	@TableField
	private Integer id; //城市编号

	@TableField("province_id")
	private Integer provinceId; //省份编号

	@TableField("city_name")
	private String cityName; //城市名称

	private String description; //描述

	@Override
	public String toString() {

		return "City{" +
				"id=" + id +
				", provinceId=" + provinceId +
				", cityName='" + cityName + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}