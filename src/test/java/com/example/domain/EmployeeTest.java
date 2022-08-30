package com.example.domain;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class EmployeeTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@DisplayName("性別の文字列を数値に変換できているか【正常】")
	void convertGenderStringToIntNormalTest() {
		// 期待値
		Employee expected = new Employee(1, "佐藤", 20, 1, "男性");
		
		// 実際
		Employee actual = new Employee(1, "佐藤", 20, null, "男性");
		actual.convertGenderStringToInt();
		
		// 結果
		assertEquals(expected.getGender(), actual.getGender());
	}
	
	@Test
	@DisplayName("性別の文字列を数値に変換できず例外が投げられるか【異常】")
	void convertGenderStringToIntAbnrmalTest() {
		Employee actual = new Employee(1, "佐藤", 20, null, "男");
		
		// 結果
		assertThrows(IllegalStateException.class, () -> actual.convertGenderStringToInt());
	}

}
