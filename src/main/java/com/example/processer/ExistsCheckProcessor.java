package com.example.processer;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.domain.Employee;
import com.example.repository.EmployeeJdbcRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 読み取ったCSVがDBに存在するかチェックする
 * 既に登録済みのデータはスキップする
 * @author mikami
 *
 */
@Component("ExistsCheckProcessor")
@StepScope
@Slf4j
public class ExistsCheckProcessor implements ItemProcessor<Employee, Employee>{
	
	@Autowired
	private EmployeeJdbcRepository employeeJdbcRepository;

	/**
	 * 従業員が存在するかチェックする
	 * @return
	 * 存在した場合：null(スキップする)
	 * 存在しなかった場合：employee
	 */
	@Override
	public Employee process(Employee item) throws Exception {
		Boolean result = employeeJdbcRepository.exists(item.getId());
		
		if(result) {
			log.info("Skip because exists:{}", item);
			return null; // Processorの戻り値をnullにするとそのデータはWriterに渡されない（つまりスキップされる）
		}
		
		return item;
	}

}
