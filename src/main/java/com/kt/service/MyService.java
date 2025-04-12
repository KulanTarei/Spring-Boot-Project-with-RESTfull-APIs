package com.kt.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.kt.dto.EmployeeDTO;
import com.kt.entity.EmployeeEntity;

public interface MyService {
	
	EmployeeEntity insert(EmployeeDTO dto);
	List<EmployeeEntity> readAll();
	Optional<EmployeeEntity> readSingle(int id);
	EmployeeEntity updateAll(EmployeeDTO employeeDto, EmployeeEntity employeeEntity);
	List<String> validation(Map<String, Object> map);
	EmployeeEntity partialUpdate(EmployeeEntity employeeEntity, Map<String, Object> map);
	void delete(int id);
	
}
