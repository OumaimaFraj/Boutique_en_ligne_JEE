package JEE_Project.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.math.BigDecimal;
import JEE_Project.model.Product;
import JEE_Project.dao.ProductDao;
import JEE_Project.connection.DBconnection;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.UUID;

@WebServlet("/add-product")
@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
	    maxFileSize = 1024 * 1024 * 10,      // 10MB
	    maxRequestSize = 1024 * 1024 * 50   // 50MB
	)
public class AddProduct extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AddProduct() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Afficher la page d'ajout de produit
        request.getRequestDispatcher("AddProduct.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html;charset=UTF-8");
		//try (PrintWriter out = response.getWriter()) {
        // Récupérer les paramètres du formulaire
        String nom = request.getParameter("nom");
        String description = request.getParameter("description");
        
        //String prixParam = request.getParameter("prix");
       // BigDecimal prix = new BigDecimal(prixParam);

     // Convertir le prix en BigDecimal
        String prixParam = request.getParameter("prix");
        //----------------pour le test -----------------------------------
        System.out.println("Request Parameters:");
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = parameterNames.nextElement();
            String value = request.getParameter(key);
            System.out.println(key + ": " + value);
        }
        //-------------------------------------------------------------------------

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
        // convertir le stock en int 
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
     // Initialiser image comme chaîne vide
        String image = ""; 


        // Traitement du fichier d'image
        Part filePart = request.getPart("image"); // Récupérer le fichier téléchargé
        if (filePart != null) {
            // Générer un nom unique pour l'image pour éviter les conflits
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String uploadDir = getServletContext().getRealPath("/product-image"); // Spécifier le répertoire où enregistrer l'image
            File uploads = new File(uploadDir);
            if (!uploads.exists()) {
                uploads.mkdirs(); // Créer le répertoire si nécessaire
            }

            // Sauvegarder l'image dans le répertoire spécifié
            File file = new File(uploads, fileName);
            filePart.write(file.getAbsolutePath()); // Sauvegarder l'image sur le disque

            image = fileName; // Le chemin relatif de l'image pour l'enregistrer dans la base de données
        }

        // Créer un objet Product et ajouter à la base de données
        Product product = new Product();
        product.setNom(nom);
        product.setDescription(description);
        product.setPrix(prix);
        product.setImage(image); // Enregistrer le chemin de l'image
        product.setStock(stock);

        // Ajouter le produit à la base de données
        try {
            ProductDao productDao = new ProductDao(DBconnection.getConnection());
            productDao.addProduct(product); // Assurez-vous que la méthode addProduct est bien définie

            // Rediriger vers la page d'accueil ou page des produits
            response.sendRedirect("AdminIndex.jsp"); // Page d'accueil ou liste des produits
        } catch (Exception e) {
            e.printStackTrace(); // Afficher l'exception pour débogage
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection error");
        }
    }
}
