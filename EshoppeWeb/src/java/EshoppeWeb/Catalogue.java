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

    /////a récupérer de session Tomcat...
    boolean connecté = false;
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
            UtilHtml.enteteHtml(out);
            
            out.println("<body>");
            out.println("<h1>The super duper Catalogue!!! </h1>");
            UtilHtml.barreDeMenu(out, connecté);
            enteteCatalogue(out);            
            
            //tableau affichage mettre dans une méthode genererListeItems() prend 0,1 ou 2 params.
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

    
    
    private void enteteCatalogue(PrintWriter out){
                
        out.println("<form ><table>"
                + "         <tr>"
                + "             <td>Rechercher dans: <select name=\"genre\" /> "////remplir par méthode
                + "                     <option selected>Tout le catalogue</option>" 
                + "                     <option>Armes</option>"
                + "                     <option>Armures</option>"                 
                + "                     <option>Habiletés</option>" 
                + "                     <option>Potions</option>" 
                + "             </select> "
                + "             </td>"
                + "             <td>Mot clé: <input type=\"text\" id=\"nomCle\" />"
                + "                 <input type=\"submit\" value=\"Afficher\"/></td>"
                + "         </tr>"
                + "</table></form>");
    }
}
    
