<%@page import="student.StudentDAO"%>
<%@page import="student.Student2"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>
<%@ page import="java.util.ArrayList" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width initial-scale=1">

<link rel="stylesheet" href="css/bootstrap.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="css/custom.css">
<title>JSP 게시판 웹 사이트</title>
<style type="text/css">
	a, a:hover{
	color : #000000;
	text-decoration: none;
	}
	#contents{
		text-align: center;
	}
</style>
</head>
<body> 
	<%	
		String userID = null;
		if(session.getAttribute("userID") != null){
			userID = (String) session.getAttribute("userID");
		}
		int pageNumber = 1;
		if(request.getParameter("pageNumber") != null){
			pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
		}		//리퀘스트가 있다면 페이지 넘버 변경 해주네 , page넘버는 url에서 넘어오고 있는거 같음
	%>
	
	<nav class="navbar navbar-default">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
			data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
			aria-expanded="false">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="main.jsp">JSP 게시판 웹 사이트</a>
		</div>
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li><a href="main.jsp">메인</a></li>
				<li class="active"><a href="bbs.jsp">게시판</a></li>
			</ul>
			
			<%
				if(userID == null){

			%>
			
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false">접속하기<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="login.jsp">로그인</a></li>
						<li><a href="join.jsp">회원가입</a></li>
					</ul>	
				</li>
			</ul>
			
			<%
			}
			else
			{
				
			%>
			
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown">
					<a href="#" class="dropdown-toggle"
					data-toggle="dropdown" role="button" aria-haspopup="true"
					aria-expanded="false">회원관리<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="logoutAction.jsp">로그아웃</a></li>
					</ul>	
				</li>
			</ul>
			
			<%
			}
			%>
		</div>
	</nav>
	
	<div class="container">
		<div class="row">
			<table class="table table-striped" style="text-align:enter; border : 1px solid #dddddd;">
				<thead> <!-- 테이블의 헤더 부분 -->
					<tr>
						<th style="background-color: #eeeeee; text-align:center;">학번</th>
						<th style="background-color: #eeeeee; text-align:center;">이름</th>
						<th style="background-color: #eeeeee; text-align:center;">작성일</th>
					</tr>
				</thead>
				<tbody>
				<%
					StudentDAO studentDAO = new StudentDAO();
					ArrayList<Student2> list = studentDAO.getList(pageNumber);
			
					for(int  i = 0; i < list.size(); i++)
					{
				%>
					<tr id = "contents">
						<td><%=list.get(i).getStudentNumber()%></td>
						<td><a href="view.jsp?studentNumber=<%= list.get(i).getStudentNumber()%>"><%=list.get(i).getStudentName().replaceAll(" ", "&nbsp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>")%></a></td>
						<td><%= list.get(i).getStudentDate().substring(0, 11) + list.get(i).getStudentDate().substring(11, 13) + 
						" 시 " + list.get(i).getStudentDate().substring(14, 16) +" 분"%></td>
					</tr>
				<%
					}
				%>
				</tbody>
			</table>
			<%
				if (pageNumber != 1){
			%>
				<a href="bbs.jsp?pageNumber=<%=pageNumber - 1 %>" class="btn btn-success btn-arrow-left">이전</a>
			<%
				} if(studentDAO.nextPage(pageNumber + 1)){
			%>
				<a href="bbs.jsp?pageNumber=<%=pageNumber + 1%>" class="btn btn-success btn-arrow-left">다음</a><!-- pagenumber가 아예 변루로써 넘어가네 ,? 뒤에 변수 넣고 값 저장 하고 -->
				<!-- 이러면 똑같은 페이지인데 변수가 달라지니 위에 함수에 의해서 다른 페이지 처럼 보이는 거네 -->
			<% 																								
				}
			%>
			<a href="write.jsp" class="btn btn-primary pull-right">글쓰기</a>
		</div>
	</div>
	
	<script src="https://code.jquery.com/jquery-3.1.1.min.js">
	</script>
	<script src="js/bootstrap.js"></script>
</body>
</html>