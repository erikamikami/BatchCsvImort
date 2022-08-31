package com.example.listener;



import org.springframework.batch.core.ItemProcessListener;
import org.springframework.stereotype.Component;

import com.example.domain.Employee;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProcesListener implements ItemProcessListener<Employee, Employee>{
	
	@Override
	public void beforeProcess(Employee item) {
		log.info("ProcessListenerのbeforeProcess:{}", item);
		
	}

	@Override
	public void afterProcess(Employee item, Employee result) {
		log.info("afterProcess:item={}, result={}", item, result);
		
	}

	@Override
	public void onProcessError(Employee item, Exception e) {
		log.error("ProcessError:item={}, errorMessage={}", item, e.getMessage());
		
	}

}
