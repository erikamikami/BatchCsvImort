package com.example.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.domain.Employee;

@Configuration
public class MybatisImportBatchConfig extends BaseConfig{
	
	/**	Mybatisで必要になる **/
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	/**
	 * Writer（MyBatis用）
	 * @return
	 */
	@Bean
	public MyBatisBatchItemWriter<Employee> mybatisWriter(){
		return new MyBatisBatchItemWriterBuilder<Employee>()
												.sqlSessionFactory(sqlSessionFactory)
												.statementId("com.example.repository.EmployeeMapper.insertOne")
												.build();
	}
	
	/**
	 * Stepの作成（MyBatis用）
	 * @return
	 */
	@Bean
	public Step csvImportMybatisStep() {
		return this.stepBuilderFactory.get("CsvImportMybatisStep")
										.<Employee, Employee>chunk(10)
										.reader(csvReader()).listener(this.readListener)
										.processor(compositeProcessor()).listener(this.processListener)
										.writer(mybatisWriter()).listener(this.writeListener)
										.build();
				
	}
	
	
	/**
	 * Jobの作成（MyBatis用）
	 * @return
	 */
	@Bean
	public Job csvImportMybatisJob() {
		return this.jobBuilderFactory.get("CsvImportMybatisJob")
										.incrementer(new RunIdIncrementer())
										.start(csvImportMybatisStep())
										.build();
										
	}
	
}
