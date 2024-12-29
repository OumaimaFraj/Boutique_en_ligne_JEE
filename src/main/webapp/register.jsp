<%@page import="JEE_Project.model.*"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
		
<!DOCTYPE html>
<html lang="fr">
<head>
	<%@include file="/includes/head.jsp"%>
	<title>E-Commerce register</title>
	<style>
		/* Ajout de styles personnalisés pour améliorer l'apparence */
		body {
			background-color: #f8f9fa;
			background-image: url('product-image/login2.jpg'); /* Remplacez par le chemin de votre image */
            background-size: cover; /* L'image couvre toute la page */
            background-position: center; /* L'image est centrée */
            background-repeat: no-repeat; /* L'image ne se répète pas */
		}
		.card {
			box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
			border-radius: 12px;
		}
		.btn-primary {
			background-color: #859393;
			border: none;
		}
		.btn-primary:hover {
			background-color: #859393;
		}
		.register-link {
			color: #007bff;
			text-decoration: none;
		}
		.register-link:hover {
			text-decoration: underline;
		}
	</style>
</head>
<body>
	<%@include file="/includes/navbar.jsp"%>

	<div class="container">
		<div class="d-flex justify-content-center align-items-center" style="min-height: 100vh;">
			<div class="card w-50 p-4">
				
				<div class="card-body">
					<form action="register" method="post">
						<div class="form-group mb-3">
							<label for="login-name">Nom</label>
							<input type="text" name="Register-name" id="login-name" class="form-control" placeholder="Entrez votre nom">
						</div>
						<div class="form-group mb-3">
							<label for="login-email">Adresse email</label>
							<input type="email" name="Register-email" id="login-email" class="form-control" placeholder="Entrez votre email">
						</div>
						<div class="form-group mb-4">
							<label for="login-password">Mot de passe</label>
							<input type="password" name="Register-password" id="Register-password" class="form-control" placeholder="Entrez votre mot de passe">
						</div>
						<div class="text-center">
							<button type="submit" class="btn btn-primary btn-lg w-100">S'inscrir</button>
							
						</div>
					</form>
		</div>
	</div>
	</div>
	</div>

	<%@include file="/includes/footer.jsp"%>
</body>
</html>
