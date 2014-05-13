/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EshoppeWeb;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

/**
 *
 * @author Simon
 */
@WebServlet(name = "AjouterPanier", urlPatterns = {"/ajouterpanier"})
public class AjouterPanier extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AjouterPanier</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AjouterPanier at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("http://localhost:8080/eshoppeweb/catalogue");
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String retourUrl = "http://localhost:8080/eshoppeweb/panier";
        HttpSession session = request.getSession();
        if ( session == null || session.getAttribute("Nom_Joueur") != null)
        {
            session.setAttribute("Erreur", "Impossible d'éffectuer d'ajouter au panier si vous n'êtes pas connecter");
            retourUrl = "http://localhost:8080/eshoppeweb/catalogue";
        }
        else
        {
            try
            {
                int numItem = Integer.parseInt(request.getParameter("numitem"));
                int quantite = Integer.parseInt(request.getParameter("qte"));
            }
            catch(NumberFormatException E){
                    session.setAttribute("Erreur", "Il faut entrer une quantité au bouton ajouter correspondant");
                    retourUrl = "http://localhost:8080/eshoppeweb/catalogue";
            }

        }
        response.sendRedirect(retourUrl);
        
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
