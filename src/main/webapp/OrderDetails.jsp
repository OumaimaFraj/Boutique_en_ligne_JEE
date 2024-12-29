<%@page import="java.util.*"%>
<%@page import="JEE_Project.model.*"%>
<%@page import="JEE_Project.dao.*"%>
<%@page import="JEE_Project.connection.DBconnection"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%
   
    int orderId = Integer.parseInt(request.getParameter("orderId"));
    OrderDao orderDao = new OrderDao(DBconnection.getConnection());
    List<OrderDetails> orderDetails = orderDao.getOrderDetailsByOrderId(orderId); // Méthode pour récupérer les détails de la commande
    request.setAttribute("orderDetails", orderDetails);
%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/includes/head.jsp"%>
<title>Order Details</title>
<style>
/* Fond de la page */

h3 {
    color: #6c757d; /* Couleur gris foncé élégante */
    font-family: 'Helvetica', sans-serif; /* Changer la police du titre */
    font-size: 26px; /* Taille plus grande pour le titre */
    margin-bottom: 30px; /* Espacement en bas pour un meilleur visuel */
}

</style>
</head>
<body>
    <div class="container">
    <br>
    <br>
        <table class="table table-striped">
        
         <h3>Order Details ID: <%= orderId %></h3>
         <br>
        
            <thead>
                <tr>
                     <th>ID de commande</th>
                    <th>ID de produit</th>
                    <th>Nom de produit</th>
                    <th>Quantité</th>
                    <th>Prix total</th>
                    
                </tr>
            </thead>
            <tbody>
                <% if (orderDetails != null && !orderDetails.isEmpty()) { %>
                    <% for (OrderDetails detail : orderDetails) { %>
                        <tr>
                            <td><%= detail.getCommandeId() %></td>
                            <td><%= detail.getProduitId() %></td>
                            <td><%= detail.getProduitNom() %></td>
                            <td><%= detail.getQuantite() %></td>
                            <td><%= detail.getPrixTotal() %></td>
                        </tr>
                    <% } %>
                <% } else { %>
                    <tr>
                        <td colspan="4">No details found for this order.</td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>
    <%@include file="/includes/footer.jsp"%>
</body>
</html>
