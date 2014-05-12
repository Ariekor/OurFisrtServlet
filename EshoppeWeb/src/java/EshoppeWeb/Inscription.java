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
@WebServlet(name = "Inscription", urlPatterns = {"/inscription"})
public class Inscription extends HttpServlet {

    // callable:  gestion_users.insertion(?,?,?,?,?)
    // avec dans l'ordre: NOMUSAGER, MOTDEPASSE, NOM, PRENOM, CAPITAL
    
    
    
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
            out.println("<form action='inscription' method='post'>");
            UtilHtml.enteteHtml(out, "Catalogue");
                out.println("<div id='mainInscriptionDiv'>");
                    out.println("<table id='zeInscritpion'>");

                        out.println("<tr id='zePresentationRowInscritpion'>");
                            out.println("<td id='zeTitreTabInscription' colspan='4'>Inscription </td>");
                        out.println("</tr>");

                        out.println("<tr>");
                            out.println("<td colspan='2' class='inscriptionLabelRow'>Nom Usager : </td>");
                            out.println("<td colspan='2' class='zeChampTexteInsc'><input type=\"text\" name=\"Username\" value=\"votre pseudo\"/></td>");

                        out.println("</tr>");

                        out.println("<tr>");
                            out.println("<td colspan='2' class='inscriptionLabelRow'>Mot de passe : </td>");
                            out.println("<td colspan='2' class='zeChampTexteInsc'><input type=\"text\" name=\"MotDePasse\" value=\"votre mot de passe\"/> </td>");

                        out.println("</tr>");

                        out.println("<tr>");
                            out.println("<td colspan='2' class='inscriptionLabelRow'>Nom : </td>");
                            out.println("<td colspan='2' class='zeChampTexteInsc'><input type=\"text\" name=\"Nom\" value=\"votre nom\"/> </td>");

                        out.println("</tr>");

                        out.println("<tr>");
                            out.println("<td colspan='2' class='inscriptionLabelRow'>Prénom : </td>");
                            out.println("<td colspan='2' class='zeChampTexteInsc'><input type=\"text\" name=\"Prenom\" value=\"votre prénom\"/> </td>");

                        out.println("</tr>");

                        out.println("<tr class='zeOtherRow'>");
                            out.println("<td > </td>");
                            out.println("<td > </td>");
                            out.println("<td> </td>");
                            out.println("<td class='zeChampTexteInsc'><input type=\"submit\" value=\"S'inscrire\" class=\"b_submit\"/> </td>");
                        out.println("</tr>");

                    out.println("</table>");
                    out.println("<div id='zePreContenant>");
                        out.println("<pre id='zeError'>");
                        out.println("</pre>");
                    out.println("</div>");
                out.println("</div>");
            out.println("</form>");
            
            
            
            UtilHtml.piedsDePage(out);
        }
       
    }
    
    protected void processRequestDebut(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");         
        try { 
            UtilHtml.enteteHtml(out, "Catalogue");//serait bien de récupérer le webServlet name            
            UtilHtml.barreDeMenu(out, false);            
        }
      //  catch()
        finally
        {
            //on ne ferme pas tout de suite, il faut exécuter process reques fin
       //    out.close();
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

}
