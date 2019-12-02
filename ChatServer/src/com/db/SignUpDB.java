package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUpDB {
	private static SignUpDB instance = new SignUpDB();

	public static SignUpDB getInstance() {
		return instance;
	}

	public SignUpDB() {
	}

	// oracle ����
	String jdbcUrl = "jdbc:oracle:thin:@192.168.15.116:1521:orcl";
	String userId = "uganda";
	String userPw = "uganda";

	Connection conn = null;
	PreparedStatement pstmt = null;
	PreparedStatement pstmt2 = null;
	ResultSet rs = null;

	String sql = "";
	String sql2 = "";
	String returns = " ";

	public synchronized String connectionDB(String id, String pwd, String name, String email) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(jdbcUrl, userId, userPw);

			sql = "SELECT id FROM member WHERE id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				returns = "�̹� �����ϴ� ���̵� �Դϴ�.";
			} else {
				sql2 = "INSERT INTO member VALUES(?,sys.pkg_crypto.encrypt(?),?,?)";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, id);
				System.out.println(id);
				pstmt2.setString(2, pwd);
				System.out.println(pwd);
				pstmt2.setString(3, name);
				System.out.println(name);
				pstmt2.setString(4, email);
				System.out.println(email);
				int member = pstmt2.executeUpdate();
				if (member == 1) {
					returns = "ȸ�� ���� ����!";
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			returns = "ȸ������ ����";
		} finally {
			if (pstmt2 != null)
				try {
					pstmt2.close();
				} catch (SQLException ex) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
		}
		return returns;
	}
}
