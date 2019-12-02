<%@ page import="com.db.FindPW"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	request.setCharacterEncoding("UTF-8");

	FindPW findPW = FindPW.getInstance();

	String id = request.getParameter("id");

	String returns = findPW.findPW(id);
	System.out.println(returns);

	// 안드로이드로 전송
	out.println(returns);
%>