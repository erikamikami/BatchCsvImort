package com.example.config;

import java.nio.charset.StandardCharsets;

import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;

import com.example.domain.Employee;

@EnableBatchProcessing
public abstract class BaseConfig {
	
	/**	jobBuilderFactory **/
	@Autowired
	protected JobBuilderFactory jobBuilderFactory;
	
	/**	stepBuilderFactory **/
	@Autowired
	protected StepBuilderFactory stepBuilderFactory;
	
	/**	Processor **/
	@Autowired
	@Qualifier("GenderConvertProcessor")
	protected ItemProcessor<Employee, Employee> genderConvertProcessor;
	
	/**	ReadListener **/
	@Autowired
	protected ItemReadListener<Employee> readListener;
	
	/**	ProcessListener **/
	@Autowired
	protected ItemProcessListener<Employee, Employee> processListener;
	
	/**	WriteListener **/
	@Autowired
	protected ItemWriteListener<Employee> writeListener;
	
	/**	sampleProperty **/
	@Autowired
	protected SampleProperty sampleProperty;
	
	
	public FlatFileItemReader<Employee> csvReader(){
		// CSVのカラムに付ける名前
		String[] nameArray = new String[] {"id","name","age","gender"};
		
		//ファイルの読み込み設定
		return new FlatFileItemReaderBuilder<Employee>() // Builderをまず取得する
											.name("employeeCsvReader") // Readerに名前を付ける
											.resource(new ClassPathResource(sampleProperty.getCsvPath())) // ファイルを読み取るための方法を指定する。今回はクラスパスから読み取る。任意のパス（C:¥hogeなど）からファイルを読み取るバイは、FileSystemResouceクラスを使用。
											.linesToSkip(1) // ヘッダー行の読み取りをスキップする際に使用
											.encoding(StandardCharsets.UTF_8.name()) // 読み込むファイルの文字コードを指定
											.delimited() // DelimitedBuilderを取得する。これでファイルから読み取った値をJavaクラスにマッピングする
											.names(nameArray) // カラムに付ける名前を指定。
											.fieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() { // CSVとJavaとをマッピングする。CSVのカラムにつけた名前とJavaクラスのフィールド名が一致すると値がマッピングされる
												{
													setTargetType(Employee.class);
												}
											})
											.build();
		
	}

}
