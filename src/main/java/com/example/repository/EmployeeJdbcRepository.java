package com.example.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeJdbcRepository {
	
	@Autowired
	private JdbcTemplate template;
	
	private static final String EXISTS_SQL = 
			"SELECT EXISTS (SELECT id, name, age, gender FROM employee WHERE id = ?)";
	
	
	/**
	 * 指定されたidがDBに存在するかどうか確認する
	 * @param id
	 * @return boolean
	 */
	public boolean exists(Integer id) {
		boolean result = template.queryForObject(EXISTS_SQL,Boolean.class,id);
		return result;
	}

}
