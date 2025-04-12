package com.kt.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmployeeDTO {
	@Size(min=2, max=30, message="NAME SIZE VALIDATION EARROR")
	private String name;
	@Size(min=2, max=30, message="ADDRESS SIZE VALIDATION ERROR")
	private String address;
	@Min(value = 1000, message = "SALARY MUST BE 1000 OR ABOVE")
	@Max(value = 100000, message = "SALARY MUST BE 100000 OR BELOW")
	private Integer salary;
}
