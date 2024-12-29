<%@ page contentType="text/html;charset=UTF-8" language="java" %> 
<%@ page import="JEE_Project.model.Product" %>
<%@ page import="JEE_Project.dao.ProductDao" %>
<%@ page import="JEE_Project.connection.DBconnection" %>
<%@ page import="java.sql.SQLException" %>

<!DOCTYPE html>
<html>
<head>
    <title>Modifier un produit</title>
    <link rel="stylesheet" href="path_to_your_css/bootstrap.min.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        body {
            background-color: #f4f6f9; /* Soft light gray background */
            font-family: 'Helvetica Neue', Arial, sans-serif;
            color: #333;
        }
        .container {
            max-width: 900px;
            margin-top: 50px;
            background-color: #ffffff;
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        }
        h2 {
            text-align: center;
            margin-bottom: 30px;
            color: #6c757d; /* Dark gray for text */
            font-size: 2rem;
        }
        .form-group label {
            font-weight: 600;
            color: #495057;
            margin-bottom: 10px;
        }
        .form-group input, .form-group textarea, .form-group select {
            border-radius: 8px;
            padding: 12px;
            font-size: 16px;
            width: 100%;
            border: 1px solid #ced4da;
            transition: border 0.3s ease;
        }
        .form-group input:focus, .form-group textarea:focus, .form-group select:focus {
            border-color: #5cb85c;
            outline: none;
        }
        .form-group textarea {
            resize: vertical;
            min-height: 150px;
        }
        .form-group img {
            max-width: 200px;
            border: 1px solid #ddd;
            border-radius: 8px;
            margin-bottom: 15px;
        }
        .btn-primary {
            background-color: #6c757d; /* Subtle gray */
            border: none;
            padding: 12px 20px;
            font-size: 16px;
            cursor: pointer;
            width: 100%;
            border-radius: 8px;
            transition: background-color 0.3s ease;
        }
        .btn-primary:hover {
            background-color: #5a6268; /* Slightly darker gray */
        }
        .alert {
            color: #dc3545;
            font-weight: bold;
            text-align: center;
        }
        .form-group input[type="file"] {
            background-color: #f8f9fa;
        }
    </style>
</head>
<body>

    <% 
        // Récupérer l'ID du produit depuis la requête
        String idParam = request.getParameter("id");
        Product product = null;

        if (idParam != null) {
            int productId = Integer.parseInt(idParam);
            ProductDao productDao = new ProductDao(DBconnection.getConnection());
            try {
                // Récupérer les données du produit à partir de la base de données
                product = productDao.getProductById(productId);
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur lors de la récupération du produit");
            }
        }
    %>

    <% if (product != null) { %>
    <div class="container">
        <h2>Modifier le produit</h2>
        <form action="update-product" method="POST" enctype="multipart/form-data">
            <input type="hidden" name="id" value="<%= product.getId() %>">
            <div class="form-group">
                <label for="nom">Nom du produit</label>
                <input type="text" class="form-control" id="nom" name="nom" value="<%= product.getNom() %>" required>
            </div>
            <div class="form-group">
                <label for="description">Description</label>
                <textarea class="form-control" id="description" name="description" required><%= product.getDescription() %></textarea>
            </div>
            <div class="form-group">
                <label for="prix">Prix</label>
                <input type="text" class="form-control" id="prix" name="prix" value="<%= product.getPrix() %>" required>
            </div>
            <div class="form-group">
                <label for="image">Image actuelle</label>
                <div>
                    <img src="<%= request.getContextPath() + "/product-image/" + product.getImage() %>" alt="Image du produit">
                </div>
                
             <div class="form-group">
                <label for="newImage">Télécharger une nouvelle image (si nécessaire)</label>
                <input type="file" class="form-control" id="newImage" name="newImage">
            </div>
            
            <div class="form-group">
                <label for="stock">Quantité en stock</label>
                <input type="number" class="form-control" id="stock" name="stock" value="<%= product.getStock() %>" required>
            </div>
            <button type="submit" class="btn btn-primary">Modifier le produit</button>
        </form>
    </div>
    <% } else { %>
        <p class="alert alert-danger">Produit non trouvé.</p>
    <% } %>
</body>
</html>
