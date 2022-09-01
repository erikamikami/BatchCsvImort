package com.example.listener;

import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

import com.example.domain.Employee;

import lombok.extern.slf4j.Slf4j;

/**
 * スキップ用のListener
 * 
 * @author mikami
 *
 */

@Component
@StepScope
@Slf4j
public class EmployeeSkipListener implements SkipListener<Employee, Employee>{
	
	@Override
	public void onSkipInRead(Throwable t) {
		log.warn("Read時にエラーが発生したので、スキップしました:errorMessage={}", t.getMessage());
		
	}

	@Override
	public void onSkipInWrite(Employee item, Throwable t) {
		log.warn("Write時にエラーが発生したので、スキップしました:errorMessage={}", t.getMessage());
		
	}

	@Override
	public void onSkipInProcess(Employee item, Throwable t) {
		log.warn("Process時にエラーが発生したので、スキップしました:errorMessage={}", t.getMessage());
		
	}

}
