<%@page import="JEE_Project.connection.DBconnection"%>
<%@page import="JEE_Project.dao.UserDao"%>
<%@page import="JEE_Project.model.*"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/includes/head.jsp"%>
<title>Gestion des utilisateurs</title>
</head>
<body>
	<%@include file="/includes/navbarAdmin.jsp"%>
	<div class="container">
		<div class="card-header my-3">All users</div>
		<table class="table table-light">
			<thead>
				<tr>
					<th scope="col">ID</th>
					<th scope="col">Nom</th>
					<th scope="col">Email</th>
					<th scope="col">Role</th>
					<th scope="col">Actions</th>
				</tr>
			</thead>
			<tbody>
			<%
					try {
					    // Connexion à la base de données
						UserDao userDao = new UserDao(DBconnection.getConnection());
						List<User> utilisateurs = userDao.getUtilisateurs();

						// Parcourir les utilisateurs
						for (User user : utilisateurs) {
				%>
				<tr>
					<td><%= user.getId() %></td>
					<td><%= user.getNom() %></td>
					<td><%= user.getEmail() %></td>
					<td><%= user.getRole() %></td>
					<td>
						<a href="gestion-users?action=supprimer&id=<%= user.getId() %>" class="btn btn-danger btn-sm">Supprimer</a>
						<a href="gestion-users?action=bloquer&id=<%= user.getId() %>" class="btn btn-warning btn-sm">Bloquer</a>
						
					</td>
				</tr>
				<%
						}
					} catch (Exception e) {
						out.println("<tr><td colspan='4'>Erreur : " + e.getMessage() + "</td></tr>");
					}
				%>
				
				<!-- Tables des utilisateurs bloqués -->
				
								
			</tbody>
		</table>
	</div>
	<%@include file="/includes/footer.jsp"%>
</body>
</html>