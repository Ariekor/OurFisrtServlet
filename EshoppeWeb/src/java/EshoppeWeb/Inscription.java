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
import java.util.List;
import java.util.ArrayList;
/**
 *
 * @author Isabelle
 */
@WebServlet(name = "Inscription", urlPatterns = {"/inscription"})
public class Inscription extends HttpServlet {

    // callable:  gestion_users.insertion(?,?,?,?,?)
    // avec dans l'ordre: NOMUSAGER, MOTDEPASSE, NOM, PRENOM, CAPITAL
    
    HttpSession session;
    final int departSolde = 1000;
    
    
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
            generatePage(out, "");
        }
       
    }
    
    protected void generatePage(PrintWriter out, String Erreur) throws ServletException , IOException{
       
            out.println("<form action='inscription' method='post'>");
            UtilHtml.enteteHtml(out, "Catalogue");
                out.println("<div id='mainInscriptionDiv'>");
                    out.println("<table id='zeInscritpion'>");

                        out.println("<tr id='zePresentationRowInscritpion'>");
                            out.println("<td id='zeTitreTabInscription' colspan='4'>Inscription </td>");
                        out.println("</tr>");

                        out.println("<tr>");
                            out.println("<td colspan='2' class='inscriptionLabelRow'>Nom Usager : </td>");
                            out.println("<td colspan='2' class='zeChampTexteInsc'><input type=\"text\" name=\"Username\" value=\"\"/></td>");

                        out.println("</tr>");

                        out.println("<tr>");
                            out.println("<td colspan='2' class='inscriptionLabelRow'>Mot de passe : </td>");
                            out.println("<td colspan='2' class='zeChampTexteInsc'><input type=\"text\" name=\"MotDePasse\" value=\"\"/> </td>");

                        out.println("</tr>");

                        out.println("<tr>");
                            out.println("<td colspan='2' class='inscriptionLabelRow'>Nom : </td>");
                            out.println("<td colspan='2' class='zeChampTexteInsc'><input type=\"text\" name=\"Nom\" value=\"\"/> </td>");

                        out.println("</tr>");

                        out.println("<tr>");
                            out.println("<td colspan='2' class='inscriptionLabelRow'>Prénom : </td>");
                            out.println("<td colspan='2' class='zeChampTexteInsc'><input type=\"text\" name=\"Prenom\" value=\"\"/> </td>");

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
                            out.println(Erreur);
                        out.println("</pre>");
                    out.println("</div>");
                out.println("</div>");
            out.println("</form>");
           
            UtilHtml.piedsDePage(out);
        
    }
    
    protected void processRequestDebut(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws ServletException, IOException {
        
        session = request.getSession();// session ne sera jamais null
        //nomUser = (String)session.getAttribute( "Nom_Joueur" );
        
        response.setContentType("text/html;charset=UTF-8");         
        try { 
            UtilHtml.enteteHtml(out, "Catalogue");//serait bien de récupérer le webServlet name            
            UtilHtml.barreDeMenu(out, session);            
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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            generatePage(out, "");
        }
    }
    
    private String accomplirRequete(String Error, List<String> param)
    {
        for ( int i = 0 ; i < param.size(); ++i)
        {
            if (param.get(i).equals("") )
            {
                Error += "\n -Le champs : "+ getNomChamp(i) +" est vide \n";
            }
            if (param.get(i).length() > 20 )
            {
                Error += "\n -Le champs : " + getNomChamp(i) + " ne doit pas comprendre plus de 20 caracteres \n";
            }
            
        }
        if ( Error.equals(""))
        {
            Error = JDBCPart(Error);
        }
        return Error;
    }
    private String JDBCPart(String Error)
    {
        return Error;
    }
    
    private String getNomChamp(int index)
    {
        String nomChamp = "";
        switch(index){
            case 0 : nomChamp = "Nom usager";
                break;
            case 1 : nomChamp = "Mot de passe";
                break;
            case 2 : nomChamp = "Nom";
                break;
            case 3 : nomChamp = "Prenom";
                break;
            default : nomChamp = "";
                break;
        }
        return nomChamp;
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
        response.setContentType("text/html;charset=UTF-8");
        List<String> param = new ArrayList();
        param.add(request.getParameter("Username"));
        param.add(request.getParameter("MotDePasse"));
        param.add(request.getParameter("Nom"));
        param.add(request.getParameter("Prenom"));
        String Error = "";
        boolean Reussi = false;
        Error = accomplirRequete(Error , param);
        Reussi = Error.equals("");
        if(Reussi == false )
        {
            try (PrintWriter out = response.getWriter()) {
                generatePage(out, Error);
            }
        }
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
