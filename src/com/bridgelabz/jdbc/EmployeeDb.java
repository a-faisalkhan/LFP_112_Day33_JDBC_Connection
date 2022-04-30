package com.bridgelabz.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.bridgelabz.jdbc.model.EmployeeModel;
import com.bridglelabz.jdbc.util.Util;


public class EmployeeDb {

	public final String EMP_DATA_FETCH = "select * from employee_payroll";
	public final String EMP_UPDATE_SALARY = "update salary_tbl set basic_pay=?,"
			+ "deduction=?, taxable_pay=?, tax=?, net_pay=? where id=?";

	static List<EmployeeModel> employeeList = new ArrayList<>();

	public static void main(String[] args) {
		Connection connection = DBConnector.getConnection();

		EmployeeDb empDb = new EmployeeDb();
		empDb.fetchEmpData(connection);

		empDb.updateSalary(connection);
		
		DBConnector.close();
	}

	private void updateSalary(Connection connection) {

		try {
			PreparedStatement ps = connection
					.prepareStatement(EMP_UPDATE_SALARY);
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

	public void fetchEmpData(Connection connection) {
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(EMP_DATA_FETCH);

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
}
