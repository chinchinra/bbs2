package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public UserDAO() {
		try {
//			String dbURL = "jdbc:mysql://localhost:3306/BBS";
			String dbURL = "jdbc:mysql://localhost/bbs?serverTimezone=Asia/Seoul&useSSL=false";

//			String dbID = "root";
			String dbID = "kang";
	//		String dbPassword = "root";
			String dbPassword = "123456";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public int login(String userID, String userPassword) 
	{
		String SQL = "SELECT upw FROM bbsuser WHERE uid = ?";
		
		
		try 
		{
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1,  userID);
			rs = pstmt.executeQuery();
			if(rs.next()) 
			{
				if(rs.getString("upw").contentEquals(userPassword))
					return 1; // 로그인 성공
				else
					return 0; // 비밀번호 불일치
			}
			return -1; // 아이디가 없음
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return -2; // 데이터 베이스 오류
	}
	
	public int join(User user)
	{
		String SQL = "INSERT INTO bbsuser VALUES (?, ?, ?, ?, ?)";
		
		if(idCheck(user.getUserID()) == -1) {
			return -1;
		}
		
		
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getUserGender());
			pstmt.setString(5, user.getUserEmail());
			return pstmt.executeUpdate();
		}
		catch(Exception e) {
			e.printStackTrace();
		
		}
		return -2; //데이터 베이스 오류
	}
	
	public int idCheck(String userId) {
		String SQL = "SELECT uid FROM bbsuser WHERE uid = ?";
		
		try 
		{
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1,  userId);
			rs = pstmt.executeQuery();
			if(rs.next()) 
			{
					return -1; // 아이디 존재		
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return 1;
		
	}
	
}
