package com.kt.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kt.dto.EmployeeDTO;
import com.kt.entity.EmployeeEntity;
import com.kt.service.MyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/")
public class MyController {
	
	@Autowired
	private MyService service;
	
	@PostMapping("employees")
	public ResponseEntity<?> insert(@Valid @RequestBody EmployeeDTO dto, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			List<String> list = new ArrayList();
			
			for(ObjectError error : bindingResult.getAllErrors()) {
				list.add(error.getDefaultMessage());
			}
			return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
		}else {
			//Service Layer
			EmployeeEntity savedEmployee = service.insert(dto);
			Map<Object, Object> map = new HashMap<>();
			map.put("message", "Employee saved sucessfully");
			map.put("data", savedEmployee);
			return new ResponseEntity<>(map, HttpStatus.CREATED);
		}
	}
	//ALL ID READ
	@GetMapping("employees")
	public ResponseEntity<List<EmployeeEntity>> readAll(){
		List<EmployeeEntity> list = service.readAll();
		return ResponseEntity.ok(list);
	}
	//SINGLE ID READ
	@GetMapping("employees/{id}")
	public ResponseEntity<?> readSingle(@PathVariable int id){
		Optional<EmployeeEntity> data = service.readSingle(id);
		if(data.isPresent()) {
			return ResponseEntity.ok(data.get());
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID DOES NOT EXIST");
		}
	}
	
	//FULL OBJECT UPDATE
	@PutMapping("employees/{id}")
	public ResponseEntity<?> updateAll(@PathVariable int id, @Valid @RequestBody EmployeeDTO employeeDto, BindingResult bindingResult){
		
		if(bindingResult.hasErrors()) {
			List<String> list = new ArrayList<>();
			for(ObjectError objectError : bindingResult.getAllErrors()) {
				list.add(objectError.getDefaultMessage());
			}
			return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
		}else {
			Optional<EmployeeEntity> data = service.readSingle(id);
			if(data.isPresent()) {
				EmployeeEntity employeeEntity = data.get();
				//UPDATE SERVICE
				EmployeeEntity updatedEmployee = service.updateAll(employeeDto, employeeEntity);
				
				Map<Object, Object> map = new HashMap<>();
				map.put("message", "Employee Updated Successfully");
				map.put("data", updatedEmployee);
				return ResponseEntity.ok(map);
				
			}else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID DOES NOT EXIST");
			}
		}
	}
	//PARTIAL UPADATE
	@PatchMapping("employees/{id}")
	public ResponseEntity<?> updatePartial(@PathVariable int id, @RequestBody Map<String, Object> map){
		//1. VALID
		List<String> errorList = service.validation(map);
		if(errorList.isEmpty()) {
			//2. ID EXIST
			
			Optional<EmployeeEntity> data = service.readSingle(id);
			if(data.isPresent()) {
				
				EmployeeEntity employeeEntity = data.get();
				//UPDATE
				EmployeeEntity partialUpdatedEmployee = service.partialUpdate(employeeEntity, map);
				Map<Object, Object> xmap = new HashMap<>();
				xmap.put("message", "Employee partially  Updated Successfully");
				xmap.put("data", partialUpdatedEmployee);
				return ResponseEntity.ok(xmap);
			}else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID DOES NOT EXIST");
			}
			
		}else {
			return new ResponseEntity<>(errorList, HttpStatus.BAD_REQUEST);
		}
	}
	//DELETE SINGLE
	@DeleteMapping("employees/{id}")
	public ResponseEntity<?> deleteSingle(@PathVariable int id){
		Optional<EmployeeEntity> data = service.readSingle(id);
		if(data.isPresent()) {
			//DELETE
			service.delete(id);
			return ResponseEntity.ok("Employee Deleted Successfully");
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID DOES NOT EXIST");
		}
	}
	
}
