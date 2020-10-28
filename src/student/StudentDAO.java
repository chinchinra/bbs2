package student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class StudentDAO {

	// 스프링 쓸때는 오토 와이어 쓰자

	private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"; // 드라이버

	private final String DB_URL = "jdbc:mysql://localhost/bbs?serverTimezone=Asia/Seoul&useSSL=false"; // 접속할 DB
	private final String USER_NAME = "kang"; // DB에 접속할 사용자 이름을 상수로 정의
	private final String PASSWORD = "123456"; // 사용자의 비밀번호를 상수로 정의

	public int write(String studentNumber, String studentName, int studentKorean, int studentMath, int studentEnglish) {

		PreparedStatement state = null; // statement === 정적 SQL 문을 실행하고 생성 된 결과를 리턴하는 데 사용되는 오브젝트입니다.
		Connection conn = null;
		if (idCheck(studentNumber) == 1) {
			try {
				Class.forName(JDBC_DRIVER);
				conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
				String sql = "insert into grade values(?,?,?,?,?,?,now())";
				state = conn.prepareStatement(sql);
				state.setString(1, studentNumber);
				state.setString(2, studentName);
				state.setInt(3, studentKorean);
				state.setInt(4, studentMath);
				state.setInt(5, studentEnglish);
				state.setInt(6, 1); // studentAvaliable , 현재 활성화 중인지 아닌지 확인
				// state.executeUpdate();
				return state.executeUpdate(); // 완료
				// either (1) the row count for SQL Data Manipulation Language (DML)
				// statementsor (2) 0 for SQL statements that return nothing
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (idCheck(studentNumber) == 0) {
			return 0; // 학번 중복
		}
		return -1; // 이상생김
	}

	private int idCheck(String studentNumber) {
		Connection conn_st = null;
		// PreparedStatement state = null; // statement === 정적 SQL 문을 실행하고 생성 된 결과를 리턴하는
		// 데 사용되는 오브젝트입니다.

		Statement state_st = null;
		try {
			Class.forName(JDBC_DRIVER);
			conn_st = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
			String sql_st = "select studentNumber from grade";
			state_st = conn_st.createStatement();
			ResultSet rs = state_st.executeQuery(sql_st);
			while (rs.next()) {
				if (rs.getString(1).equals(studentNumber)) { // == rs.getString(1)
					return 0;
				}
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public String getDate() {

		PreparedStatement state = null; // statement === 정적 SQL 문을 실행하고 생성 된 결과를 리턴하는 데 사용되는 오브젝트입니다.
		Connection conn = null;
		ResultSet rs;
		String SQL = "SELECT NOW()";
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
			state = conn.prepareStatement(SQL);
			rs = state.executeQuery();

			if (rs.next()) {
				return rs.getString(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ""; // 데이터베이스 오류
	}

	public int getNext() {
		Connection conn = null;
		PreparedStatement state = null; // statement === 정적 SQL 문을 실행하고 생성 된 결과를 리턴하는 데 사용되는 오브젝트입니다.
		ResultSet rs;
		String SQL = "SELECT count(*) FROM grade";
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
			state = conn.prepareStatement(SQL);

			rs = state.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 0; // 첫 번째 게시물인 경우

		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}

	public ArrayList<Student2> getList(int pageNumber) {
		ResultSet rs;
		String SQL = "SELECT * FROM grade WHERE studentAvailable = 1 order by studentDate desc LIMIT ?,10";
		Connection conn = null;
		PreparedStatement state = null; // statement === 정적 SQL 문을 실행하고 생성 된 결과를 리턴하는 데 사용되는 오브젝트입니다.
		ArrayList<Student2> list = new ArrayList<Student2>();

		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
			state = conn.prepareStatement(SQL);
			state.setInt(1, (pageNumber - 1) * 10); // 11 10 pa 2 14 10 4

			rs = state.executeQuery();

			while (rs.next()) {
				Student2 student2 = new Student2();
				student2.setStudentNumber(rs.getString(1));
				student2.setStudentName(rs.getString(2));
				student2.setStudentKorean(rs.getInt(3));
				student2.setStudentMath(rs.getInt(4));
				student2.setStudentEnglish(rs.getInt(5));
				student2.setStudentAvailable(rs.getInt(6));
				student2.setStudentDate(rs.getString(7));
				list.add(student2);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public boolean nextPage(int pageNumber) {
		Connection conn = null;
		PreparedStatement state = null;
		ResultSet rs = null;
		String SQL = "SELECT * FROM grade WHERE studentAvailable = 1 limit ?,10";

		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
			state = conn.prepareStatement(SQL);
			state.setInt(1, (pageNumber - 1) * 10);
			rs = state.executeQuery();

			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false; // 데이터베이스 오류 ,페이지 가 존재하는지 확인하는 메서드
	}

	public Student2 getStudent2(String studentNumber) {
		String SQL = "SELECT * FROM grade WHERE studentNumber = ?";
		ResultSet rs;
		Connection conn = null;
		PreparedStatement state = null; // statement === 정적 SQL 문을 실행하고 생성 된 결과를 리턴하는 데 사용되는 오브젝트입니다.

		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
			state = conn.prepareStatement(SQL);
			state.setString(1, studentNumber);
			rs = state.executeQuery();
			if (rs.next()) {
				Student2 student2 = new Student2();
				student2.setStudentNumber(rs.getString(1));
				student2.setStudentName(rs.getString(2));
				student2.setStudentKorean(rs.getInt(3));
				student2.setStudentMath(rs.getInt(4));
				student2.setStudentEnglish(rs.getInt(5));
				student2.setStudentAvailable(rs.getInt(6));
				student2.setStudentDate(rs.getString(7));
				return student2;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; // 데이터베이스 오류
	}

	public int delete(String studentNumber) {
		String SQL = "UPDATE grade SET studentAvailable = 0 WHERE studentNumber = ?";
		Connection conn = null;
		PreparedStatement state = null; // statement === 정적 SQL 문을 실행하고 생성 된 결과를 리턴하는 데 사용되는 오브젝트입니다.
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
			state = conn.prepareStatement(SQL);
			state.setString(1, studentNumber);
			return state.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
	public int deleteFinal(String studentNumber) {
		String SQL = "delete from grade WHERE studentNumber = ?";
		Connection conn = null;
		PreparedStatement state = null; // statement === 정적 SQL 문을 실행하고 생성 된 결과를 리턴하는 데 사용되는 오브젝트입니다.
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
			state = conn.prepareStatement(SQL);
			state.setString(1, studentNumber);
			return state.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
public int update(String studentNumber, String studentName,
		int studentKorean,int studentMath,int  studentEnglish) {
	Connection conn = null;
	PreparedStatement state = null; // statement === 정적 SQL 문을 실행하고 생성 된 결과를 리턴하는 데 사용되는 오브젝트입니다.
		String SQL = "UPDATE grade SET studentName = ? ,studentKorean = ? ,studentMath = ? ,studentEnglish = ?, studentDate = now() WHERE studentNumber = ?";
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
			state = conn.prepareStatement(SQL);
			
			state.setString(1,  studentName);
			state.setInt(2,  studentKorean);
			state.setInt(3,  studentMath);
			state.setInt(4,  studentEnglish);
			
			
			state.setString(5,  studentNumber);
			return state.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}

}
