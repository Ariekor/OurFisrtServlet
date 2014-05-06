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

/**
 *
 * @author Isabelle
 */
@WebServlet(name = "Catalogue", urlPatterns = {"/catalogue"})
public class Catalogue extends HttpServlet {

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
            enteteHtml(out);
            
            out.println("<body>");
            out.println("<h1>Servlet Catalogue at " + request.getContextPath() + "</h1>");
            enteteCatalogue(out);            
            
            //tableau affichage 
            out.println("     <table>"
                    + "         <!--Le tableau qui contient la liste-- "
                    + "         <tr> for each colonne: <td>titre colonne</td></tr>"
                    + "           for each item"
                    + "         <tr> for each colonne <td>titre colonne</td></tr>"
                    + "     </table>"
                    + "</div>");
            
            
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
        processRequest(request, response);
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
        processRequest(request, response);
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

    
    private void enteteHtml(PrintWriter out){    
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Catalogue É-Shop-pe</title>"); 
        out.println("<meta charset=\"UTF-8\">");
        out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        out.println("<link href=\"testcss.css\" rel=\"stylesheet\" type=\"text/css\"/>");            
        out.println("</head>");
    }
    private void enteteCatalogue(PrintWriter out){
        
        out.println("<div>");//Ligne de menu du haut
        out.println("   <table>");
        out.println("       <tr><td>LOGO</td>");
        out.println("           <td><input type=\"button\" value=\"login ou profil\"/>"
                                    + "<input type=\"text\" id=\"user\" value=\"nom usager\"/>"
                                    + "<input type=\"text\" id=\"motdepasse\" value=\"mot de passe\"/></td>");
        out.println("           <td><input type=\"button\" value=\"s'inscrire ou se déconnecter\"/></td>");
        out.println("           <td><input type=\"button\" value=\"Inventaire\"/></td>");
        out.println("       </tr> ");
        out.println("   </table>"
                + "     <table>"
                + "         <tr>"
                + "             <td>drop down menu genre</td>"
                + "             <td>bouton et champs recherche</td>"
                + "             <td>Bouton ajouter au panier</td>"
                + "         </tr>"
                + "     </table>");
    }
}
