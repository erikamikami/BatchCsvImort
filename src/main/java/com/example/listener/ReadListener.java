package com.example.listener;

import org.springframework.batch.core.ItemReadListener;
import org.springframework.stereotype.Component;

import com.example.domain.Employee;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ReadListener implements ItemReadListener<Employee>{
	
	@Override
	public void beforeRead() {
		log.info("ReadListenerのBeforeRead");
		
	}

	@Override
	public void afterRead(Employee item) {
		log.debug("AfterRead:{}", item);
		
	}

	@Override
	public void onReadError(Exception ex) {
		log.error("ReadError: errorMessage:", ex.getMessage(), ex);
		
	}

}
