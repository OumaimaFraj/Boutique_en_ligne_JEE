<%@page import="JEE_Project.connection.DBconnection"%>
<%@page import="JEE_Project.dao.OrderDao"%>
<%@page import="JEE_Project.model.*"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/includes/head.jsp"%>
<title>Gestion des commandes</title>
</head>
<body>
	<%@include file="/includes/navbarAdmin.jsp"%>
	<div class="container">
		<div class="card-header my-3">Toutes les commandes</div>
		<table class="table table-light">
			<thead>
				<tr>
					<th scope="col">ID commande</th>
					<th scope="col">ID utilisateur</th>
					<th scope="col">Date</th>
					<th scope="col">Statut</th>
					<th scope="col">Modifier status</th>
				    <th scope="col">View details</th>
				</tr>
			</thead>
			<tbody>
			<%
					try {
					    // Connexion à la base de données
						OrderDao orderDao = new OrderDao(DBconnection.getConnection());
						List<Order> commandes = orderDao.getAllOrders();

						// Parcourir les commandes
						
						for (Order order : commandes) {
				%>
				<tr>
					<td><%= order.getId() %></td>
					<td><%= order.getUtilisateurId() %></td>
					<td><%= order.getDate() %></td>
					<td><%= order.getStatus() %></td>
					<td>
						<a href="gestion-commandes?action=ModifierStatus&id=<%= order.getId() %>" class="btn btn-danger btn-sm">Modifier statut</a>
					</td>
					<td>
                    <a href="OrderDetails.jsp?orderId=<%= order.getId() %>" class="btn btn-info btn-sm">View Details</a>
                </td>
				</tr>
				<%
						}
					} catch (Exception e) {
						out.println("<tr><td colspan='5'>Erreur : " + e.getMessage() + "</td></tr>");
					}
				%>
			</tbody>
		</table>
	</div>
	<%@include file="/includes/footer.jsp"%>
</body>
</html>
