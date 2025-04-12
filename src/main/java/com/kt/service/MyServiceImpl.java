package com.kt.service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kt.dto.EmployeeDTO;
import com.kt.entity.EmployeeEntity;
import com.kt.repository.EmployeeRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@Service
public class MyServiceImpl implements MyService{
	
	@Autowired
	private EmployeeRepository repository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	private Validator validator;
	
	public MyServiceImpl() {
		ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
		Validator validator = vf.getValidator();
		this.validator = validator;
	}
	
	public EmployeeEntity insert(EmployeeDTO dto) {
		
		EmployeeEntity entity = modelMapper.map(dto, EmployeeEntity.class);
		entity.setDate(LocalDate.now());
		return repository.save(entity);
	}
	
	@Override
	public List<EmployeeEntity> readAll(){
		
		return repository.findAll();
	}
	
	@Override
	public Optional<EmployeeEntity> readSingle(int id){
		
		return repository.findById(id);
	}
	
	@Override
	public EmployeeEntity updateAll(EmployeeDTO employeeDto, EmployeeEntity employeeEntity) {
		
		modelMapper.map(employeeDto, employeeEntity);
		employeeEntity.setDate(LocalDate.now());
		return repository.save(employeeEntity);
	}
	@Override
	public List<String> validation(Map<String, Object> map){
		
		List<String> errorList = new ArrayList<>();
		for(Entry<String, Object> entry: map.entrySet()) {
			
			String fieldName = entry.getKey();
			Object fieldValue = entry.getValue();
			
			try {
				Field field = EmployeeDTO.class.getDeclaredField(fieldName); //salary
				
				field.setAccessible(true);
				
				EmployeeDTO employeeDto = new EmployeeDTO();
				field.set(employeeDto, fieldValue);
				
				Set<ConstraintViolation<EmployeeDTO>> violations = validator.validate(employeeDto);
				
				for(ConstraintViolation<EmployeeDTO> violation : violations) {
					errorList.add(violation.getMessage());
				}
				
			}catch(Exception e){
				errorList.add("KEY IS NOT CORRECT: "+fieldName);
		     }
		}
		return errorList;
		
	}
	
	@Override
	public EmployeeEntity partialUpdate(EmployeeEntity employeeEntity, Map<String, Object> map){
		for(Entry<String, Object> entry: map.entrySet()) {
			
			String fieldName = entry.getKey();
			Object fieldValue = entry.getValue();
			
			try {
				Field field = EmployeeEntity.class.getDeclaredField(fieldName);
				field.setAccessible(true);
				field.set(employeeEntity, fieldValue);
			}catch(Exception e) {
				e.printStackTrace();
			}
			/*
			 * if(fieldName.equals("name")) { employeeEntity.setName((String) fieldValue); }
			 * if(fieldName.equals("address")) { employeeEntity.setAddress((String)
			 * fieldValue); } if(fieldName.equals("salary")) {
			 * employeeEntity.setSalary((Integer) fieldValue); }
			 */
		}
		employeeEntity.setDate(LocalDate.now());
		
		return repository.save(employeeEntity);
	}
	
	@Override
	public void delete(int id){
		repository.deleteById(id);
	}
}	
