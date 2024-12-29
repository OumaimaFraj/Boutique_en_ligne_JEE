<%@page import="java.text.DecimalFormat"%>
<%@page import="JEE_Project.dao.OrderDao"%>
<%@page import="JEE_Project.connection.DBconnection"%>
<%@page import="JEE_Project.dao.ProductDao"%>
<%@page import="JEE_Project.model.*"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%
	// Création d'un DecimalFormat
	DecimalFormat dcf = new DecimalFormat("#.##");
	request.setAttribute("dcf", dcf);

	// Récupération de l'objet 'auth' de la session
	User auth = (User) request.getSession().getAttribute("auth");

	// Si 'auth' est null, rediriger vers la page de login
	if (auth == null) {
	    response.sendRedirect("login.jsp");
	    return;  // Arrêter l'exécution du code JSP après la redirection
	}

	// Si l'utilisateur est authentifié, on passe à la récupération des commandes
	List<Order> orders = null;
	OrderDao orderDao = new OrderDao(DBconnection.getConnection());
	orders = orderDao.getOrdersByUserId(auth.getId()); // Récupère les commandes de l'utilisateur
	request.setAttribute("orders", orders);

	%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/includes/head.jsp"%>
<title>E-Commerce Cart</title>
</head>
<body>
	<%@include file="/includes/navbar.jsp"%>
	<div class="container">
		<div class="card-header my-3">All Orders</div>
		<table class="table table-light">
			<thead>
				<tr>
					<th scope="col">Date</th>
					<th scope="col">ID</th>
					<th scope="col">Status</th>
					<th scope="col">View Details</th>
				</tr>
			</thead>
			<tbody>
			<% if (orders != null && !orders.isEmpty()) { %>
        <% for (Order order : orders) { %>
            <tr>
                <td><%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(order.getDate()) %></td>
                <td><%= order.getId() %></td>
                <td><%= order.getStatus() %></td>
                <td>
                    <a href="OrderDetails.jsp?orderId=<%= order.getId() %>" class="btn btn-info btn-sm">View Details</a>
                </td>
            </tr>
        <% } %>
    <% } else { %>
        <tr>
            <td colspan="4">No orders found.</td>
        </tr>
    <% } %>
			
			</tbody>
		</table>
	</div>
	<%@include file="/includes/footer.jsp"%>
</body>
</html>
