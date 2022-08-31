package com.example.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.domain.Employee;

@Configuration
public class JdbcImportBatchConfig extends BaseConfig{
	
	@Autowired
	private DataSource dataSource;
	
	/**	insert文 **/
	private static final String INSERT_EMPLOYEE_SQL = 
			"INSERT INTO employee(id, name, age, gender) VALUES (:id, :name, :age, :gender)";
	
	/**
	 * Writer
	 * JdbcBatchItemWriterを使用すれば、DBへ書き込みができる
	 * @return
	 */
	@Bean
	@StepScope
	public JdbcBatchItemWriter<Employee> jdbcWriter(){
		
		// providerの作成
		BeanPropertyItemSqlParameterSourceProvider<Employee> provider = new BeanPropertyItemSqlParameterSourceProvider<>();
		
		// Writerの設定
		// JdbcBatchItemWriterBuilderの使用が必要
		return new JdbcBatchItemWriterBuilder<Employee>() // まずBuilderを生成する
									.itemSqlParameterSourceProvider(provider) // itemSqlParameterSourceProviderを使うと、SQLに渡すパラメータクラスを指定できる
									.sql(INSERT_EMPLOYEE_SQL) // SQLを指定
									.dataSource(this.dataSource) 
									.build();
	}
	
	/**
	 * Stepの作成
	 * @return
	 */
	@Bean
	public Step csvImportJdbcStep() {
		return this.stepBuilderFactory.get("CsvImportJdbcStep")
								.<Employee, Employee>chunk(10)
								.reader(csvReader()).listener(this.readListener)
								.processor(compositeProcessor()).listener(this.processListener)
								.writer(jdbcWriter()).listener(this.writeListener)
								.build();
		
	}
	
	/**
	 * Jobの作成
	 * @return
	 */
	@Bean("JdbcJob")
	public Job csvImportJdbcJob() {
		return this.jobBuilderFactory.get("CsvImportJdbcJob")
								.incrementer(new RunIdIncrementer())
								.start(csvImportJdbcStep())
								.build();
	}

}
