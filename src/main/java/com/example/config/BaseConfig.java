package com.example.config;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
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
	
	/**	Processor（性別の文字列を数値に変換する） **/
	@Autowired
	@Qualifier("GenderConvertProcessor")
	protected ItemProcessor<Employee, Employee> genderConvertProcessor;
	
	/**	Processor（id指定でデータの存在をチェックする） **/
	@Autowired
	@Qualifier("ExistsCheckProcessor")
	protected ItemProcessor<Employee, Employee> existsCheckProcessor;
	
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
	
	
	@Bean
	@StepScope
	public FlatFileItemReader<Employee> csvReader(){
		// CSVのカラムに付ける名前
		String[] nameArray = new String[] {"id","name","age","genderString"};
		
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
	
	/**
	 * 複数のProcessor
	 * @return
	 */
	@Bean
	@StepScope
	public ItemProcessor<Employee, Employee> compositeProcessor(){
		
		CompositeItemProcessor<Employee, Employee> compositeProcessor = new CompositeItemProcessor<>();
		
		compositeProcessor.setDelegates(Arrays.asList(validationProcessor(), this.existsCheckProcessor, this.genderConvertProcessor)); // Listに追加した順でProcessorが実行される
		
		return compositeProcessor;
		
	}
	
	/**
	 * BeanValidatingItemProcessorを使用すれば、アノテーションによるバリデーションを実装できる
	 * バリデーションエラーが発生したときの挙動は、setFilter()で設定する
	 * @return
	 */
	@Bean
	@StepScope
	public BeanValidatingItemProcessor<Employee> validationProcessor(){
		BeanValidatingItemProcessor<Employee> validationProcessor = new BeanValidatingItemProcessor<>();
		
		// true: スキップし、バッチ処理を停止せずに次のデータの処理へ進む
		// false: ValidationExeceptionを投げ、途中で停止する
		validationProcessor.setFilter(true);
		
		return validationProcessor;
	}

}
