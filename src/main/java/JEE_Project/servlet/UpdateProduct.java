package JEE_Project.servlet;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import JEE_Project.model.Product;
import JEE_Project.dao.ProductDao;
import JEE_Project.connection.DBconnection;

@WebServlet("/update-product")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50   // 50MB
)
public class UpdateProduct extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UpdateProduct() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Afficher la page de modification du produit
        request.getRequestDispatcher("UpdateProduct.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        // Récupérer l'ID du produit à modifier
        String idParam = request.getParameter("id");
        int productId = 0;
        if (idParam != null && !idParam.isEmpty()) {
            try {
                productId = Integer.parseInt(idParam);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID du produit invalide");
                return;
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID du produit requis");
            return;
        }

        // Récupérer les nouveaux paramètres du produit
        String nom = request.getParameter("nom");
        String description = request.getParameter("description");

        // Traitement du prix
        String prixParam = request.getParameter("prix");
        BigDecimal prix = null;
        if (prixParam != null && !prixParam.isEmpty()) {
            try {
                prix = new BigDecimal(prixParam);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Prix invalide");
                return;
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Le prix est requis");
            return;
        }

        // Traitement du stock
        String stockParam = request.getParameter("stock");
        int stock = 0; // Valeur par défaut
        if (stockParam != null && !stockParam.isEmpty()) {
            try {
                stock = Integer.parseInt(stockParam);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Quantité invalide");
                return;
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Le stock est requis");
            return;
        }

        // Traitement de l'image
        String image = null; // Image actuelle
        Part filePart = request.getPart("newImage"); // Récupérer le fichier téléchargé
        if (filePart != null && filePart.getSize() > 0) {
            // Si une nouvelle image est téléchargée, on la sauvegarde
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String uploadDir = getServletContext().getRealPath("/product-image");
            File uploads = new File(uploadDir);
            if (!uploads.exists()) {
                uploads.mkdirs(); // Créer le répertoire si nécessaire
            }

            // Sauvegarder l'image dans le répertoire
            File file = new File(uploads, fileName);
            filePart.write(file.getAbsolutePath());

            image = fileName; // Mettre à jour l'image avec le chemin relatif
        }

        // Récupérer le produit existant depuis la base de données
        try {
            ProductDao productDao = new ProductDao(DBconnection.getConnection());
            Product product = productDao.getProductById(productId);
            if (product == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Produit non trouvé");
                return;
            }

            // Mettre à jour les données du produit
            product.setNom(nom);
            product.setDescription(description);
            product.setPrix(prix);
            product.setStock(stock);
            if (image != null) {
                product.setImage(image); // Si une nouvelle image a été téléchargée, mettre à jour l'image
            }

            // Mettre à jour le produit dans la base de données
            productDao.updateProduct(product);

            // Rediriger vers la page des produits
            response.sendRedirect("AdminIndex.jsp"); // Page d'accueil ou liste des produits
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur lors de la mise à jour du produit");
        }
    }
}
