<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="student.StudentDAO" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="student2" class="student.Student2" scope="page" />
<jsp:setProperty name="student2" property="studentNumber"/>
<jsp:setProperty name="student2" property="studentName" />
<jsp:setProperty name="student2" property="studentKorean" />
<jsp:setProperty name="student2" property="studentMath" />
<jsp:setProperty name="student2" property="studentEnglish" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP 게시판 웹 사이트</title>
</head>
<body> 
	<%
		String userID = null;
		if(session.getAttribute("userID") != null)
		{
			userID = (String) session.getAttribute("userID");
		}
		if(userID == null){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('로그인을 하세요.')");
			script.println("location.href = 'login.jsp'");
			script.println("</script>");
		}	
		else
		{
			if(student2.getStudentNumber() == null || student2.getStudentName() == null)
			{
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('입력이 안 된 사항이 있습니다.')");
				script.println("history.back()");
				script.println("</script>");
			}
			else
			{
				StudentDAO studentDAO = new StudentDAO();
				int result = studentDAO.write(student2.getStudentNumber(),student2.getStudentName(),student2.getStudentKorean(),
						student2.getStudentMath(),student2.getStudentEnglish());
				if (result == -1)
				{
					PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("alert('글쓰기에 실패했습니다..')");
					script.println("history.back()");
					script.println("</script>");
				}
				else
				{
					PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("location.href = 'bbs.jsp'");
					script.println("</script>");
				}
			}
		}
	%>
</body>
</html>