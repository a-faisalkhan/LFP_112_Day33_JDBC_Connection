package com.bridgelabz.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.bridgelabz.jdbc.model.EmployeeModel;

import com.bridglelabz.jdbc.util.Constants;

import com.bridglelabz.jdbc.util.Util;

import com.mysql.cj.protocol.Resultset;

public class EmployeeDb {

	List<EmployeeModel> employeeList = new ArrayList<>();

	Constants constants;

	static PayrollDbService payrollDbService;

	Connection connection;

	public EmployeeDb() {
		constants = new Constants();
		payrollDbService = PayrollDbService.init();
		connection = payrollDbService.getConnection();
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		EmployeeDb empDb = new EmployeeDb();

//		empDb.getEmpData("2015-01-01", "2020-12-31");
//		empDb.getSalaryByGender();

//		empDb.updateSalary(connection);
		empDb.addNewEmp();

		try {
			payrollDbService.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getEmpData(String startDate,
			String endDate) {
		try {
			PreparedStatement ps = connection
					.prepareStatement(
							constants.EMP_DATA_BASED_ON_JOIN_DATE);
			ps.setString(1, startDate);
			ps.setString(2, endDate);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				EmployeeModel model = new EmployeeModel();

				model.setId(rs.getInt("id"));
				model.setName(rs.getString("name"));
				model.setGender(rs.getString("gender"));
				model.setPhone(rs.getString("phone"));
				model.setAddress(rs.getString("address"));
				model.setStart_date(
						rs.getDate("start_date"));
				System.out.println(model);
				System.out.println(
						"--------------------------------------");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void updateSalary(Connection connection) {

		try {
			PreparedStatement ps = connection
					.prepareStatement(
							constants.EMP_UPDATE_SALARY);
			double basic_pay = 3000000;
			double deduction = basic_pay * 0.1;
			double taxable_pay = basic_pay - deduction;
			double tax = taxable_pay * 0.2;
			double net_pay = taxable_pay - tax;

			ps.setDouble(1,
					Util.formatDoubleValue(basic_pay));
			ps.setDouble(2,
					Util.formatDoubleValue(deduction));
			ps.setDouble(3,
					Util.formatDoubleValue(taxable_pay));
			ps.setDouble(4, Util.formatDoubleValue(tax));
			ps.setDouble(5,
					Util.formatDoubleValue(net_pay));
			ps.setInt(6, 8);

			int updateStatus = ps.executeUpdate();

			if (updateStatus > 0) {
				System.out.println(
						"Salary tbl of DB is updated.");
			} else {
				System.out.println(
						"There is some error during updation");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void fetchEmpData() {
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st
					.executeQuery(constants.EMP_DATA_FETCH);

			while (rs.next()) {

				EmployeeModel model = new EmployeeModel();

				model.setId(rs.getInt("id"));
				model.setName(rs.getString("name"));
				model.setGender(rs.getString("gender"));
				model.setPhone(rs.getString("phone"));
				model.setAddress(rs.getString("address"));
				model.setStart_date(
						rs.getDate("start_date"));
				employeeList.add(model);
			}

			employeeList.forEach(e -> {
				System.out.println("Id : " + e.getId());
				System.out.println("Name : " + e.getName());
				String gender = e.getGender().equals("M")
						? "Male"
						: "Female";
				System.out.println("Gender : " + gender);
				System.out
						.println("Phone : " + e.getPhone());
				System.out.println(
						"Address : " + e.getAddress());
				System.out.println("Joining Date : "
						+ e.getStart_date());
				System.out.println(
						"------------------------------------------");
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getSalaryByGender() {
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(
					constants.EMP_SALARY_GROUP_BY_GENDER);

			while (rs.next()) {
				System.out.println("Gender : "
						+ rs.getString("gender"));
				System.out.println("Sum of salary : "
						+ rs.getDouble("SUM(s.basic_pay)"));
				System.out.println(
						"--------------------------------------");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addNewEmp() {
		Scanner sc = new Scanner(System.in);
		try {
			PreparedStatement ps = connection
					.prepareStatement(constants.NEW_EMP_ADD,
							Statement.RETURN_GENERATED_KEYS);
			System.out
					.println("Enter the Employee name : ");
			String name = sc.nextLine();
			ps.setString(1, name);
			System.out.println(
					"Enter the joining date (yyyy-MM-dd) : ");
			Date joinDate = Date.valueOf(sc.nextLine());
			ps.setDate(2, joinDate);
			System.out.println("Enter the gender (M/F) : ");
			ps.setString(3, sc.nextLine());
			System.out.println("Enter the Phone number : ");
			ps.setString(4, sc.nextLine());
			System.out.println("Enter the Adress : ");
			ps.setString(5, sc.nextLine());

			int insertStatus = ps.executeUpdate();

			System.out.println("EMP PAYROLL ADD STATUS : "
					+ insertStatus);
			if (insertStatus > 0) {
				int id = 0;
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) {
					System.out.println(rs);
					id = rs.getInt(1);
				}
				System.out
						.println("Enter the basic pay : ");
				double basic_pay = sc.nextDouble();
				addSalaryDetails(basic_pay, id);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		sc.close();
	}

	private void addSalaryDetails(double basic_pay,
			int id) {
		try {

			PreparedStatement psInsert = connection
					.prepareStatement(
							constants.NEW_SALARY_DETAILS);

			double deduction = Util
					.formatDoubleValue(basic_pay * 0.1);
			double taxable_pay = Util.formatDoubleValue(
					basic_pay - deduction);
			double tax = Util
					.formatDoubleValue(taxable_pay * 0.2);
			double net_pay = Util
					.formatDoubleValue(taxable_pay - tax);

			psInsert.setDouble(1, basic_pay);
			psInsert.setDouble(2, deduction);
			psInsert.setDouble(3, taxable_pay);
			psInsert.setDouble(4, tax);
			psInsert.setDouble(5, net_pay);
			psInsert.setInt(6, id);

			int status = psInsert.executeUpdate();

			if (status > 0) {
				System.out.println(
						"Salary details are added");
				connection.commit();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
