package net.tang.service;

import net.tang.entity.City;

public interface ICityService {

	/**
	 * 获取城市逻辑：
	 * 		如果缓存存在，从缓存中获取城市信息
	 * 		如果缓存不存在，从 DB 中获取城市信息，然后插入缓存
	 * @param id
	 * @return
	 */
	City findCityById(Integer id);

	/**
	 * 保存城市
	 * @param city
	 * @return
	 */
	boolean saveCity(City city);

	/**
	 * 更新城市逻辑：
	 * 		如果缓存存在，删除
	 * 		如果缓存不存在，不操作
	 * @param city
	 * @return
	 */
	boolean updateCity(City city);

	/**
	 * 删除城市
	 * @param id
	 * @return
	 */
	boolean deleteCity(Integer id);

}