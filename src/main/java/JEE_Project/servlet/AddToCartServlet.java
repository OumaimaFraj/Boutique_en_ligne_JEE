package JEE_Project.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import JEE_Project.connection.DBconnection;


import JEE_Project.model.*;
import JEE_Project.dao.*;




@WebServlet(name = "AddToCartServlet", urlPatterns = "/add-to-cart")
public class AddToCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setContentType("text/html;charset=UTF-8");

	    try (PrintWriter out = response.getWriter()) {
	        
	        // Vérifier si un utilisateur est connecté
	        HttpSession session = request.getSession();
	        User authUser = (User) session.getAttribute("auth");
	        
	        if (authUser == null) {
	            // Si aucun utilisateur n'est connecté, rediriger vers la page de connexion
	            response.sendRedirect("login.jsp");
	            return;
	        }

	        // Récupérer ou créer le panier pour l'utilisateur connecté
	        @SuppressWarnings("unchecked")
	        ArrayList<Cart> cartList = (ArrayList<Cart>) session.getAttribute("cart-list-" + authUser.getId());
	        if (cartList == null) {
	            cartList = new ArrayList<>();
	        }

	        // Récupérer l'ID du produit depuis les paramètres
	        int id = Integer.parseInt(request.getParameter("id"));

	        // Récupérer les détails du produit depuis la base de données
	        ProductDao productDao = new ProductDao(DBconnection.getConnection());
	        Product product = productDao.getProductById(id);

	        if (product == null) {
	            // Si le produit n'existe pas, rediriger ou afficher un message d'erreur
	            response.sendRedirect("error.jsp");
	            return;
	        }

	        // Vérifier si le produit est déjà dans le panier
	        boolean exist = false;
	        for (Cart c : cartList) {
	            if (c.getId() == id) {
	                exist = true;
	                System.out.println("Produit déjà dans le panier");
	                break;
	            }
	        }

	        // Ajouter le produit au panier si non existant
	        if (!exist) {
	            Cart cm = new Cart();
	            cm.setId(product.getId());
	            cm.setNom(product.getNom());
	            cm.setDescription(product.getDescription());
	            cm.setPrix(product.getPrix());
	            cm.setImage(product.getImage());
	            cm.setStock(product.getStock());
	            cm.setQuantity(1); // Par défaut, la quantité est 1
	            cartList.add(cm);
	        }

	        // Associer le panier à l'utilisateur dans la session
	        session.setAttribute("cart-list-" + authUser.getId(), cartList);

	        // Rediriger vers la page d'accueil ou une autre page
	        response.sendRedirect("index.jsp");
	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}