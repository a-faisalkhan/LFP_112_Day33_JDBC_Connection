package com.bridglelabz.jdbc.util;

public class Constants {

	/* MySql Connection configuration */
	public static final String JDBC_STR = "jdbc:mysql://localhost:3306/payroll_service";
	public static final String USERNAME = "root";
	public static final String PASSWORD = "root";

	/* SQL Queries */
	public final String EMP_DATA_FETCH = "select * from employee_payroll;";
	public final String EMP_UPDATE_SALARY = "update salary_tbl set basic_pay=?,"
			+ "deduction=?, taxable_pay=?, tax=?, net_pay=? where id=?;";
	public final String EMP_DATA_BASED_ON_JOIN_DATE = "SELECT * FROM employee_payroll where start_date between CAST(? AS DATE) AND CAST(? AS DATE);";
	public final String EMP_SALARY_GROUP_BY_GENDER = "select ep.gender, SUM(s.basic_pay) from employee_payroll ep, salary_tbl s where ep.id=s.emp_id GROUP BY gender;";
	public final String NEW_EMP_ADD = "insert into employee_payroll (name, start_date, gender, phone, address)"
			+ "values (?,?,?,?,?);";
	public final String NEW_SALARY_DETAILS = "insert into salary_tbl (basic_pay,deduction, taxable_pay, tax, net_pay,emp_id)"
			+ "values (?,?,?,?,?,?)";
	public final String EMP_ID_FETCH_BY_NAME = "select id from employee_payroll where name=?;";
}