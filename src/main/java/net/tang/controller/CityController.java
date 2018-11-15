package net.tang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.tang.core.tools.Wrapper;
import net.tang.entity.City;
import net.tang.service.ICityService;

@RestController
@RequestMapping("/city")
public class CityController {

	@Autowired
	private ICityService cityService;
	
	/**
	 * 
	 * @param city
	 * @return
	 */
	@GetMapping("/detail")
	public Wrapper detail(City city) {
		Integer id = city.getId();
		City result = cityService.findCityById(id);
		return Wrapper.data(result);
	}
	
	/**
	 * 
	 * @param city
	 * @return
	 */
	@PostMapping("/save")
	public Wrapper save(@RequestBody City city) {
		return Wrapper.status(cityService.saveCity(city));
	}
	
	/**
	 * 
	 * @param city
	 * @return
	 */
	@PostMapping("/update")
	public Wrapper update(@RequestBody City city) {
		return Wrapper.status(cityService.updateCity(city));
	}
	
	/**
	 * 
	 * @param city
	 * @return
	 */
	@PostMapping("/delete")
	public Wrapper delete(@RequestBody City city) {
		Integer id = city.getId();
		return Wrapper.status(cityService.deleteCity(id));
	}
	
}
