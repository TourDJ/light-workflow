package net.tang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.tang.entity.City;
import net.tang.service.ICityService;
import net.tang.workflow.page.DataWrapper;

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
	public DataWrapper detail(City city) {
		Integer id = city.getId();
		City result = cityService.findCityById(id);
		return DataWrapper.data(result);
	}
	
	/**
	 * 
	 * @param city
	 * @return
	 */
	@PostMapping("/save")
	public DataWrapper save(@RequestBody City city) {
		return DataWrapper.status(cityService.saveCity(city));
	}
	
	/**
	 * 
	 * @param city
	 * @return
	 */
	@PostMapping("/update")
	public DataWrapper update(@RequestBody City city) {
		return DataWrapper.status(cityService.updateCity(city));
	}
	
	/**
	 * 
	 * @param city
	 * @return
	 */
	@PostMapping("/delete")
	public DataWrapper delete(@RequestBody City city) {
		Integer id = city.getId();
		return DataWrapper.status(cityService.deleteCity(id));
	}
	
}
