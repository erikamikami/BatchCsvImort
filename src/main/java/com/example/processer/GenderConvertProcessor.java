package com.example.processer;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.domain.Employee;

import lombok.extern.slf4j.Slf4j;

@Component("GenderConvertProcessor")
@StepScope
@Slf4j
public class GenderConvertProcessor implements ItemProcessor<Employee, Employee>{
	
	/**
	 * 性別の文字列を数値に変換する
	 *
	 */
	@Override
	public Employee process(Employee item) throws Exception {
		try {
			item.convertGenderStringToInt();
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
			return null;
		}
		return item;
	}

}
