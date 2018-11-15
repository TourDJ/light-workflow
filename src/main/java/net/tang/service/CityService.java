package net.tang.service;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import net.tang.entity.City;
import net.tang.mapper.CityMapper;

@Service
public class CityService extends ServiceImpl<CityMapper, City> implements ICityService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CityService.class);

	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 获取城市逻辑：
	 * 		如果缓存存在，从缓存中获取城市信息
	 * 		如果缓存不存在，从 DB 中获取城市信息，然后插入缓存
	 * @param id
	 * @return
	 */
	@Override
	public City findCityById(Integer id) {
		// 从缓存中获取城市信息
		String key = "city_" + id;
		ValueOperations<String, City> operations = redisTemplate.opsForValue();
		// 缓存存在
		boolean hasKey = redisTemplate.hasKey(key);

		if (hasKey) {
			City city = operations.get(key);
			LOGGER.info("CityServiceImpl.findCityById() : 从缓存中获取了城市 >> " + city.toString());
			return city;
		}

		// 从 DB 中获取城市信息
		City city = this.getById(id);
		if(city == null)
			return null;
		// 插入缓存
		operations.set(key, city, 1000, TimeUnit.SECONDS);
		LOGGER.info("城市插入缓存 >> key: " + key + ", city: " + city.toString());
		return city;
	}

	/**
	 * 保存城市
	 * @param city
	 * @return
	 */
	@Override
	@Transactional
	public boolean saveCity(City city) {
		LOGGER.info("新增城市");
		return this.save(city);
	}

	/**
	 * 更新城市逻辑：
	 * 		如果缓存存在，删除
	 * 		如果缓存不存在，不操作
	 * @param city
	 * @return
	 */
	@Override
	@Transactional
	public boolean updateCity(City city) {

		boolean ret = this.updateById(city);

		// 缓存存在，删除缓存
		String key = "city_" + city.getId();
		boolean hasKey = redisTemplate.hasKey(key);

		if (hasKey) {
			redisTemplate.delete(key);
			LOGGER.info("从缓存中删除城市update() >> key: " + key + ", city: "  + city.toString());
		}

		return ret;
	}

	/**
	 * 删除城市
	 * 
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public boolean deleteCity(Integer id) {
		boolean ret = this.deleteCity(id);

		// 缓存存在，删除缓存
		String key = "city_" + id;
		boolean hasKey = redisTemplate.hasKey(key);

		if (hasKey) {
			redisTemplate.delete(key);
			LOGGER.info("从缓存中删除城市(delete) >>  key: " + key + ", city: " + id);
		}

		return ret;
	}
}
