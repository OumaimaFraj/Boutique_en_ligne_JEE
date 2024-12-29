<%@page import="JEE_Project.connection.DBconnection"%>
<%@page import="JEE_Project.dao.ProductDao"%>
<%@page import="JEE_Project.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.text.DecimalFormat"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
session = request.getSession();
User authUser = (User) request.getSession().getAttribute("auth");
if (authUser == null) {
    response.sendRedirect("login.jsp");
    return;
}
// Récupérer le panier spécifique à l'utilisateur connecté
ArrayList<Cart> cartList = (ArrayList<Cart>) session.getAttribute("cart-list-" + authUser.getId());
List<Cart> cartProduct = null;
BigDecimal total = BigDecimal.ZERO;

if (cartList != null && !cartList.isEmpty()) {
    ProductDao pDao = new ProductDao(DBconnection.getConnection());
    cartProduct = pDao.getCartProducts(cartList);
    total = pDao.getTotalCartPrice(cartList);
}	

DecimalFormat dcf = new DecimalFormat("#,###.00"); // Format pour le prix
%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/includes/head.jsp"%>
<title>E-Commerce panier</title>
<style type="text/css">
/* Styles généraux pour le tableau */
/* Styles généraux pour le tableau */
.table {
    background-color: #fdfdfd;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.05);
    overflow: hidden;
    width: 100%; /* S'assure que la table prend toute la largeur */
}

.table thead {
    background-color: #f5f5f5;
}

.table thead th {
    color: #555;
    font-weight: bold;
    padding: 12px;
}

.table tbody td {
    color: #666;
    padding: 12px;
    vertical-align: middle; /* Aligne verticalement les éléments dans les cellules */
}

/* Aligner correctement les boutons Buy */
.form-inline {
    display: flex;
    align-items: center; /* Aligne les éléments verticalement dans la ligne */
    justify-content: center; /* Centre le contenu horizontalement */
    gap: 10px; /* Ajoute un écart entre les éléments */
}

.form-inline .btn-incre, .form-inline .btn-decre {
    font-size: 20px;
    padding: 0 10px; /* Donne un peu de padding pour l'espace autour des icônes */
    display: inline-block;
}

.form-inline input.form-control {
    width: 50px; /* Définit une largeur fixe pour l'input */
    text-align: center;
    border: 1px solid #ccc;
    border-radius: 5px;
}

/* Boutons Buy et Check Out */
.btn-primary {
    background-color: #6c757d; /* Couleur précédente */
    border-color: #6c757d; /* Couleur précédente */
    color: #fff;
    padding: 8px 60px; /* Ajoute du padding pour un bouton plus grand */
    border-radius: 5px;
    text-align: center;
}

.btn-primary:hover {
    background-color: #5a6268;
    border-color: #5a6268;
}

/* Bouton Remove */
.btn-danger {
    background-color: #bc1823; /* Couleur précédente */
    border-color: #bc1823; /* Couleur précédente */
    color: #fff;
    padding: 8px 16px;
    border-radius: 5px;
    text-align: center;
}

.btn-danger:hover {
    background-color: #a4151f;
    border-color: #a4151f;
}

/* Bouton Check Out */
.btn-checkout {
    background-color: #28a745; /* Couleur précédente */
    color: #fff;
    border: none;
    padding: 8px 16px;
    border-radius: 5px;
}

.btn-checkout:hover {
    background-color: #218838;
}
/* Boutons d'augmentation/diminution */
.btn-incre, .btn-decre {
    color: #7a7a7a;
    font-size: 20px;
    padding: 2px 8px;
}

.btn-incre:hover, .btn-decre:hover {
    color: #555;
}
/* Total Price */
h3 {
    color: #444;
    font-weight: bold;
}

/* Alignement des éléments en haut dans le conteneur */
.d-flex.py-3 {
    align-items: center;
    gap: 15px;
}

/* S'assurer que tout est bien espacé et aligné */
.d-flex {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

</style>
</head>
<body>
	<%@include file="/includes/navbar.jsp"%>

	<div class="container my-3">
	
		<div class="d-flex py-3"><h3>Total Price: $ <%= (total.compareTo(BigDecimal.ZERO) > 0) ? dcf.format(total) : "0" %> </h3> <a class="mx-3 btn btn-primary" href="order-now">Buy all</a></div>
		<table class="table table-light">
			<thead>
				<tr>
					<th scope="col">Nom</th>
					<th scope="col">Description</th>
					<th scope="col">Prix</th>
					<th scope="col">Quantité</th>
					<th scope="col">Cancel</th>
				</tr>
			</thead>
			<tbody>
				<%
				if (cartProduct != null) {
				    for (Cart c : cartProduct) {

				%>
				<tr>
					<td><%=c.getNom() %></td>
					<td><%=c.getDescription() %></td>
					<td><%= dcf.format(c.getPrix())%></td>
					
					<td>
						<form action="order-now" method="post" class="form-inline">
						<input type="hidden" name="id" value="<%= c.getId()%>" class="form-input">
							<div class="form-group d-flex justify-content-between">
								<a class="btn bnt-sm btn-incre" href="quantity-inc-dec?action=inc&id=<%=c.getId()%>"><i class="fas fa-plus-square"></i></a> 
								<input type="text" name="quantity" class="form-control"  value="<%=c.getQuantity()%>" readonly> 
								<a class="btn btn-sm btn-decre" href="quantity-inc-dec?action=dec&id=<%=c.getId()%>"><i class="fas fa-minus-square"></i></a>
							</div>
						</form>
					</td>
					<td><a href="remove-from-cart?id=<%=c.getId() %>" class="btn btn-sm btn-danger">Remove</a></td>
				</tr>

				<%
        }
    } else {
    %>
    <tr>
        <td colspan="5" style="text-align: center;">Le panier est vide.</td>
    </tr>
    <%
    }
    %>
			</tbody>
		</table>
	</div>

	<%@include file="/includes/footer.jsp"%>
</body>
</html>