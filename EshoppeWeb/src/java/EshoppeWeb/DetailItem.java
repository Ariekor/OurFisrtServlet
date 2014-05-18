/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EshoppeWeb;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Isabelle
 */
@WebServlet(name = "DetailItem", urlPatterns = {"/detailitem"})
public class DetailItem extends HttpServlet {

     /////a récupérer de session Tomcat...
    String nomUser;
    HttpSession session;
    String erreur;
    int numItem;
    String genre;
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
        PrintWriter out = response.getWriter();
        session = request.getSession();// session ne sera jamais null
        nomUser = (String)session.getAttribute( "Nom_Joueur" );
        numItem = Integer.parseInt(request.getParameter("numitem"));
        genre = request.getParameter("genre");

        response.setContentType("text/html;charset=UTF-8");         
        UtilHtml.enteteHtml(out, "Catalogue");//serait bien de récupérer le webServlet name     
        UtilHtml.barreDeMenu(out, session); 
        
        //contenu page
        afficherGeneral(out);
        afficherDetails(out);
        
        UtilHtml.piedsDePage(out, session);
        out.close();
    }
    
    private void afficherGeneral(PrintWriter out)
    {
        //récupérer nom item et image et autres données non affichées dans catalogue
        String nomitem="un genre d'épée";
        int poids;
        String urlImage;//mettre glogal si on n'affiche pas dans l'ordre...
        out.println("<h1>"+genre+ " : " +nomitem+"</h1>");
        //mettre en page les données communes
    }
    private void afficherDetails(PrintWriter out)
    {
        //liste des différents champs possibles:
        int efficacite;     //arme, armure
        String composition; //arme, armure
        int mains;          //arme
        String taille;      //armure
        String description;  //habilete
        String effetattendu; //potion
        int duréeeffet;      //potion
         
        ConnectionOracle connBd = new ConnectionOracle();
        connBd.setConnection("kellylea", "oracle2");
        connBd.connecter();
        try
        {  
            //récupérer les données et metadata des tables genre
            String SQL = "Select * From "+genre+" where numitem='"+numItem+"'";

            Statement stm = connBd.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rst = stm.executeQuery(SQL);
            ResultSetMetaData rstMD = rst.getMetaData();
            while  (rst.next())
            {
               efficacite = (rst.getInt("efficacite"));
               //etc...
            } 
            rst.close();
            stm.close();              
        }
        catch(SQLException e){erreur = e.getMessage();}
        finally{connBd.deconnecter();}
        //générer une page avec les infos.
            //pour chaque ligne: 
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
        processRequest(request,response);
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
