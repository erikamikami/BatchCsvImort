package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * employeeテーブルに相当するドメイン
 * 
 * @author mikami
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
	
	private Integer id;
	private String name;
	private Integer age;
	private Integer gender;
	private String genderString;
	
	/**
	 * 性別の文字列を数値に変換
	 * 
	 * 男性：1
	 * 女性：2
	 * 
	 */
	public void convertGenderStringToInt() {
		if(this.genderString.equals("男性")) {
			this.gender = 1;
		}else if(this.genderString.equals("女性")) {
			this.gender = 2;
		}else {
			String errorMessage = "Gender string is invalid:" + this.genderString;
			throw new IllegalStateException(errorMessage);
		}
	}

}
