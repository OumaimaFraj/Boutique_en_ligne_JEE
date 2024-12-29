package JEE_Project.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import JEE_Project.connection.DBconnection;
import JEE_Project.dao.UserDao;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/gestion-users")
public class GestionUsersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        int id = Integer.parseInt(request.getParameter("id"));

        try {
            UserDao userDao = new UserDao(DBconnection.getConnection());

            switch (action) {
                case "supprimer":
                    if (userDao.supprimerUtilisateur(id)) {
                        response.sendRedirect("GestionUsers.jsp?message=Utilisateur supprimé avec succès.");
                    } else {
                        response.sendRedirect("GestionUsers.jsp?error=Erreur lors de la suppression.");
                        System.out.println("ID reçu pour suppression : " + id);

                    }
                    break;
                case "bloquer":
                    if (userDao.bloquerUtilisateur(id)) {
                        response.sendRedirect("GestionUsers.jsp?message=Utilisateur bloqué avec succès.");
                    } else {
                        response.sendRedirect("GestionUsers.jsp?error=Erreur lors du blocage.");
                    }
                    break;
                default:
                   response.sendRedirect("GestionUsers.jsp?error=Action inconnue.");
            }
        } catch (Exception e) {
            response.sendRedirect("GestionUsers.jsp?error=" + e.getMessage());
        }
    }
}