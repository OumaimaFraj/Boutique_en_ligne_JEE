<%@page import="JEE_Project.connection.DBconnection"%>
<%@page import="JEE_Project.dao.ProductDao"%>
<%@page import="JEE_Project.model.*"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%
User auth = (User) request.getSession().getAttribute("auth");
if (auth != null) {
    request.setAttribute("person", auth);
}
ProductDao pd = new ProductDao(DBconnection.getConnection());
List<Product> products = pd.getAllProducts();


%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/includes/head.jsp"%>
<title>Page Admin</title>
<!-- ******************************************************CODE CSS *******************************************-->
<style>
body {
    background: #f8f9fa; /* Fond clair */
    font-family: 'Arial', sans-serif;
    color: #333;
}
        /* Style pour les boutons */
.btn {
    background-color: #6c757d; /* Gris clair */
    color: #fff;
    border: none;
    padding: 8px 20px; /* Réduire le padding pour rendre le bouton plus petit */
    border-radius: 20px; /* Ajuster le border-radius pour rendre les bords un peu moins arrondis */
    font-size: 14px; /* Réduire la taille de la police */
    transition: background-color 0.3s ease;
}

/* Effet au survol */
.btn:hover {
    background-color: #5a6268;
    cursor: pointer;
}
.btn-danger {
    background-color: #dc3545; /* Rouge */
}
.btn-danger:hover {
    background-color: #c82333; /* Rouge plus foncé au survol */    
}

        
.card-img-top {
    width: 100%;
    height: 250px;
    object-fit: cover;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
}

       
.card {
    min-height: 470px; /* Vous pouvez ajuster cette valeur selon la hauteur que vous voulez */
    display: flex;
    flex-direction: column;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Ombre subtile */
    border-radius: 10px; /* Coins arrondis */
    transition: box-shadow 0.3s ease;
}

/* Effet au survol */
.card:hover {
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2); /* Ombre plus marquée au survol */
}
.card-title {
    font-size: 1.2rem;
    font-weight: bold;
    color: #333;
    margin-bottom: 15px;
}

.price {
    font-weight: bold;
}

.category {
    font-size: 0.9rem;
    color: #666; /* Gris clair pour la description */
    margin-bottom: 20px;
}
.row {
    display: flex;
    flex-wrap: wrap;
    gap: 10px; /* Réduire l'espacement entre les cartes */
    justify-content: flex-start; /* Alignement des cartes avec un peu d'espace */
}

.col-md-3 {
    flex: 1 1 calc(25% - 10px); /* Ajuste la largeur des colonnes à 25% et réduit l'espacement */
    max-width: calc(25% - 10px); /* Assure une largeur égale avec moins d'espace entre les cartes */
}

     
        
    </style>
</head>
<body>
	<%@include file="/includes/navbarAdmin.jsp"%>
	

	<div class="container">
		<div class="card-header my-3">All Products</div>
		<div class="row">
			<%
			if (!products.isEmpty()) {
				for (Product p : products) {
			%>
			<div class="col-md-3 my-3">
				<div class="card w-100">
					<img class="card-img-top" src="product-image/<%=p.getImage() %>"alt="Card image cap">
					<div class="card-body">
						<h5 class="card-title"><%=p.getNom() %></h5>
						<h6 class="price">Price: $<%=p.getPrix() %></h6>
						<h6 class="category">Description: <%=p.getDescription() %></h6>
						<div class="mt-3 d-flex justify-content-between">
						<!-- Bsh temshi lel url mtaa servlet w thot l id mtaa l produit p eli nzel aalih -->
							<a class="btn btn-dark" href="update-product?id=<%=p.getId()%>">Modifier</a>
						    <a class="btn btn-danger" href="delete-product?id=<%=p.getId()%>">Supprimer</a>
						</div>
					</div>
				</div>
			</div>
			<%
			}
			} else {
			out.println("There is no proucts");
			}
			%>

		</div>
	</div>


	<%@include file="/includes/footer.jsp"%>
</body>
</html>