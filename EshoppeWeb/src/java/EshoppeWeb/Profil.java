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

/**
 *
 * @author Léa
 */
@WebServlet(name = "Profil", urlPatterns = {"/profil"})
public class Profil extends HttpServlet {
   
   private HttpSession session;
   private String nomUser;
   private int capUser;

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
      
      session = request.getSession();
      nomUser = (String)session.getAttribute( "Nom_Joueur" );
      
      try (PrintWriter out = response.getWriter()) {
         generatePageProfil(out, "");
      }
   }
   
   protected void generatePageProfil(PrintWriter out, String Erreur)
           throws ServletException , IOException{
      
          UtilHtml.enteteHtml(out,"Profil");
          UtilHtml.barreDeMenu(out, session);
          out.println("<form action='profil' method='post'>");
            out.println("<div id='mainProfilDiv'>");
                  out.print("<table id='zeProfil'>");
                        out.println("<tr class='rowHeigh'>");
                           out.println("<td class='zeTitre' colspan='4'>Profil </td>");
                        out.println("</tr>");

                        out.println("<tr>");
                            out.println("<td colspan='2' class='labelRow'>Nom Usager : </td>");
                            out.println("<td colspan='2' class='zeChampTexte'><input type=\"text\" name=\"Username\" value=\"\"/></td>");

                        out.println("</tr>");

                        out.println("<tr>");
                            out.println("<td colspan='2' class='labelRow'>Mot de passe : </td>");
                            out.println("<td colspan='2' class='zeChampTexte'><input type=\"text\" name=\"MotDePasse\" value=\"\"/> </td>");

                        out.println("</tr>");
                        
                        out.println("<tr class='zeOtherRow'>");
                            out.println("<td colspan='4' class='b_modif'><input type=\"submit\" value=\"Modifier\" class=\"b_submit\"/> </td>");
                        out.println("</tr>");

                        out.println("<tr>");
                            out.println("<td colspan='2' class='labelRow'>Nom : </td>");
                            out.println("<td colspan='2' class='zeChampTexte'><input type=\"text\" name=\"Nom\" value=\"\"/> </td>");

                        out.println("</tr>");

                        out.println("<tr>");
                            out.println("<td colspan='2' class='labelRow'>Prénom : </td>");
                            out.println("<td colspan='2' class='zeChampTexte'><input type=\"text\" name=\"Prenom\" value=\"\"/> </td>");

                        out.println("</tr>");
                        
                        out.println("<tr>");
                            out.println("<td colspan='2' class='labelRow'>Capital : </td>");
                            out.println("<td colspan='2' class='zeChampTexte'><input type=\"text\" name=\"Capital\" value=\"\"/> </td>");

                        out.println("</tr>");

                        out.println("<tr>");
                            out.println("<td colspan='4' class='b_modif'><input type=\"submit\" value=\"Modifier\" class=\"b_submit\"/> </td>");
                        out.println("</tr>");

                    out.println("</table>");
                out.println("</div>");
            out.println("</form>");
           
            UtilHtml.piedsDePage(out, session);
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
              response.setContentType("text/html;charset=UTF-8");
        session = request.getSession();
        try (PrintWriter out = response.getWriter()) {
            generatePageProfil(out, "");
        }
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
