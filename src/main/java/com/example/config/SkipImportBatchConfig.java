package com.example.config;

import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.domain.Employee;


@Configuration
public class SkipImportBatchConfig extends BaseConfig{
	
	/** SkipListener **/
	@Autowired
	private SkipListener<Employee, Employee> employeeSkipListener;
	
	@Autowired
	private MyBatisBatchItemWriter<Employee> myBatisWriter;
	
	/** stepの生成 **/
	@Bean
	public Step csvImportSkipStep() {
		return stepBuilderFactory.get("CsvImportSkipStep")
								.<Employee, Employee>chunk(10)
								.reader(csvReader()).listener(readListener)
								.processor(genderConvertProcessor).listener(processListener)
								.writer(myBatisWriter)
								.faultTolerant() // ★Step作成時にfaultToleranメソッドを呼び出すことで、その後にスキップとリトライを設定できる
								.skipLimit(Integer.MAX_VALUE) //スキップの最大数を指定する
								.skip(RuntimeException.class) // スキップ対象の例外クラスを指定する
								.listener(employeeSkipListener) // SkipListenerを指定する
								.build();
						
	}
	
	@Bean("CsvImportSkipJob")
	public Job csvImportSkipJob() {
		return jobBuilderFactory.get("CsvImportSkipJob")
								.incrementer(new RunIdIncrementer())
								.start(csvImportSkipStep())
								.build();
	}
	
	
	
}
