<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
<%@include file="/includes/head.jsp"%>
    <meta charset="UTF-8">
    <title>Ajouter un produit</title>
    <link rel="stylesheet" href="#">
  <style>
   /* Ajout de styles personnalisés pour améliorer l'apparence */
		body {
			background-color: #f8f9fa;
		    background-image: url('product-image/login1.jpg');
			
            background-size: cover; /* L'image couvre toute la page */
            background-position: center; /* L'image est centrée */
            background-repeat: no-repeat; /* L'image ne se répète pas */
		}
		.card {
			box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
			border-radius: 12px;
			width: 100%; /* Make the card take up the full width of the container */
			
		}
		.container {
           max-width: 1500px; /* Adjust the maximum width as needed */
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
	    <%@include file="/includes/navbarAdmin.jsp"%>

	<div class="container">
		<div class="d-flex justify-content-center align-items-center" style="min-height: 100vh;">
			<div class="card w-50 p-4">
			<h2>Ajouter un produit</h2>
				<div class="card-body">
					<form action="add-product" method="POST" enctype="multipart/form-data">
						<div class="form-group mb-3">
							<label for="nom">Nom du produit</label>
                            <input type="text" id="nom" name="nom" required class="form-control" placeholder="Entrez le nom de produit">
						</div>
						<div class="form-group mb-3">
							<label for="description">Description du produit</label>
                            <textarea id="description" name="description" required class="form-control" placeholder="Entrez la descriptionl"></textarea>
						</div>
						<div class="form-group mb-3">
							<label for="prix">Prix</label>
                            <input type="number" step="0.01" id="prix" name="prix" required class="form-control" placeholder="Entrez le prix">                            
                                                    
						</div>
						<div class="form-group mb-3">
							<label for="image">Télécharger une image</label>
                            <input type="file" id="image" name="image" required class="form-control" placeholder="Entrez l'image">
						</div>
						<div class="form-group mb-3">
							<label for="stock">Quantité en stock</label>
                            <input type="number" id="stock" name="stock" required class="form-control" placeholder="Entrez stock">
						</div>
						
						<div class="text-center">
							<button type="submit" class="btn btn-primary btn-lg w-100">Ajouter un produit</button>
							
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>

	<%@include file="/includes/footer.jsp"%>

</body>
</html>
