<%@ page import="com.db.Overlap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("UTF-8");

	Overlap overlap = new Overlap();

	String id = request.getParameter("id");
	String email = request.getParameter("email");
	String returns = "";
	if (id != null) {
		returns = overlap.overlapID(id);
	} else if (email != null) {
		returns = overlap.overlapEmail(email);
	}
	System.out.println(returns);

	// 안드로이드로 전송
	out.println(returns);
%>
