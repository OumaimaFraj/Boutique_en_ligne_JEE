package JEE_Project.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import JEE_Project.connection.DBconnection;
import JEE_Project.dao.UserDao;
import JEE_Project.model.User;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Affichage du formulaire d'inscription
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("register.jsp");
    }

    // Gestion de la soumission du formulaire d'inscription
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Récupération des données du formulaire
        String name = request.getParameter("Register-name");
        String email = request.getParameter("Register-email");
        String password = request.getParameter("Register-password");
        
        // Vérification des entrées
        if (name == null || email == null || password == null || name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            request.setAttribute("errorMessage", "Tous les champs sont obligatoires.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        String role = "utilisateur";  // rôle par défaut

     // Crée l'objet User avec un id par défaut (l'id sera auto-généré par la base de données)
     User newUser = new User(0, name, email, password, role);  // L'ID est à 0 car il sera auto-généré
     
        // Connexion à la base de données et insertion de l'utilisateur
        try {
            UserDao udao = new UserDao(DBconnection.getConnection());
            boolean isUserInserted = udao.userRegister(newUser);
            if (isUserInserted) {
                response.sendRedirect("login.jsp");  // Redirige vers la page de connexion après l'inscription réussie
            } else {
                request.setAttribute("errorMessage", "Erreur lors de l'inscription. Veuillez réessayer.");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Une erreur s'est produite. Veuillez réessayer.");
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}
