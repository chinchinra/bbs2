<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="student.*" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>
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
		
		String studentNumber = null;
		if(request.getParameter("studentNumber") != null)
		{
			studentNumber = request.getParameter("studentNumber");
		}
		if( studentNumber == null)
		{
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('유효하지 않은 글입니다.')");
			script.println("location.href = 'bbs.jsp'");
			script.println("</script>");
		}
		
		Student2 student2 = new StudentDAO().getStudent2(studentNumber);
		/*if(!userID.equals(student2.getUserID())){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('권한이 없습니다..')");
			script.println("location.href = 'bbs.jsp'");
			script.println("</script>");
		}
		else
		{ */
			if(request.getParameter("studentName") == null || request.getParameter("studentKorean")  == null ||
					request.getParameter("studentEnglish").equals("") || request.getParameter("studentMath").equals(""))
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
				int result = studentDAO.update(studentNumber, request.getParameter("studentName"), Integer.parseInt(request.getParameter("studentKorean")),Integer.parseInt(request.getParameter("studentMath")),
						Integer.parseInt(request.getParameter("studentEnglish"))); 
				if (result == -1)
				{
					PrintWriter script = response.getWriter();
					script.println("<script>");
					script.println("alert('글 수정에 실패했습니다..')");
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
		//}
	%>
</body>
</html>