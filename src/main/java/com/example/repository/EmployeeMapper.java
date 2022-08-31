package com.example.repository;

import org.apache.ibatis.annotations.Mapper;

import com.example.domain.Employee;

@Mapper
public interface EmployeeMapper {
	
	public void insertOne(Employee employee);

}
