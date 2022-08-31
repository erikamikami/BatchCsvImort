package com.example.config;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.domain.Employee;

@Configuration
public class JpaImportBatchConfig extends BaseConfig{
	
	/**	JPAで必要 **/
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	/**
	 * Writer(JPA)
	 * @return
	 */
	@Bean
	public JpaItemWriter<Employee> jpaWriter(){
		JpaItemWriter<Employee> jpaItemWriter = new JpaItemWriter<>();
		
		jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
		return jpaItemWriter;
	}
	
	/**
	 * Stepの作成（JPA）
	 * @return
	 */
	@Bean
	public Step csvImportJpaStep() {
		return this.stepBuilderFactory.get("CsvImportJpaStep")
										.<Employee, Employee>chunk(10)
										.reader(csvReader()).listener(this.readListener)
										.processor(compositeProcessor()).listener(this.processListener)
										.writer(jpaWriter()).listener(this.writeListener)
										.build();
										
	}
	
	/**
	 * Jobの作成（JPA）
	 * @return
	 */
	@Bean
	public Job csvImportJpaJob() {
		return this.jobBuilderFactory.get("svImportJpaJob")
										.incrementer(new RunIdIncrementer())
										.start(csvImportJpaStep())
										.build();
	}

}
