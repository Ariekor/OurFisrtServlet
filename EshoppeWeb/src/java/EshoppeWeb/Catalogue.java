/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EshoppeWeb;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author Isabelle
 */
@WebServlet(name = "Catalogue", urlPatterns = {"/catalogue"})
public class Catalogue extends HttpServlet {

    
    /////a récupérer de session Tomcat...
    boolean connecté = false;
    String nomUser;
    HttpSession session;
    
    ///debut du process cummun
    protected void processRequestDebut(HttpServletRequest request, HttpServletResponse response, PrintWriter out)
            throws ServletException, IOException {
        
        // création de la session
        session = request.getSession();// session ne sera jamais null
        nomUser = (String)session.getAttribute( "Nom_Joueur" );
        if(nomUser != null)
        {
            connecté = true;
        }
        
        response.setContentType("text/html;charset=UTF-8");         
        try { 
            UtilHtml.enteteHtml(out, "Catalogue");//serait bien de récupérer le webServlet name            
            UtilHtml.barreDeMenu(out, connecté);            
        }
      //  catch()
        finally
        {
            //on ne ferme pas tout de suite, il faut exécuter process reques fin
       //    out.close();
        } 
    }
    
    //fin du process commun
    ///debut du process cummun
    protected void processRequestFin(HttpServletRequest request, HttpServletResponse response,PrintWriter out)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");  
        try {        
            barreNavigation(out);    
            UtilHtml.piedsDePage(out);
        }
      //  catch()
        finally
        {
           out.close();
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
        PrintWriter out = response.getWriter();
        processRequestDebut(request,response, out);
        
        menuRecherche(out, "Tout le catalogue"); 
        listeItems(out, "Tout le catalogue",""); 
        
        processRequestFin(request, response, out);        
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
        PrintWriter out = response.getWriter();
        
        String genre = request.getParameter("genre");
        String motCle = request.getParameter("nomCle");
        
        processRequestDebut(request,response, out);
        
        menuRecherche(out, genre); 
        listeItems(out, genre, motCle);   
        
        processRequestFin(request, response, out);
        
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

      
    private void menuRecherche(PrintWriter out, String g){
        
        out.print("<div class='entete_catalogue'>"
                        + "<form action='catalogue' method='post'>"
                        + "     Rechercher dans:  ");
        out.println(genrerListeGenres(g));
        
        out.println("Mot clé: <input type=\"text\" name=\"nomCle\" id=\"marge\" />"
                        + "<input type=\"submit\" value=\"Afficher\" class=\"b_submit\" />"
                        + "</form>"
                   + "</div>");
    }
    
    private String genrerListeGenres(String genre){
        String SQLGenre = "Select Distinct Genre From Catalogue";
        String liste ="<select name=\"genre\" />";
        String g;
        try
        {
            ConnectionOracle oradb = new ConnectionOracle();
            oradb.setConnection("kellylea", "oracle2");
            oradb.connecter();  
            Statement stm = oradb.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rst = stm.executeQuery(SQLGenre); 
            
            if(!genre.equals("Tout le catalogue")){
                liste += "<option>"+"Tout le catalogue"+"</option>";
            }
            else{
                liste += "<option selected>"+"Tout le catalogue"+"</option>";
            }            
            while(rst.next()){
                g = rst.getString("genre");
                if(g.equals(genre)){                
                    liste += "<option selected>"+g+"</option>";
                }
                else{
                    liste += "<option>"+g+"</option>";
                }
            }
            liste += "</select>";
        }
        catch (SQLException e){/*faire quelquechose ici*/}        
        return liste;
    }
    
    private void listeItems(PrintWriter out, String genre, String cle ){
        String numitem;
        String nomitem;
        String qte ;
        String prix;
        String poids ;
        String genreItem ;
        
        // connexion à la base de données
        ConnectionOracle oradb = new ConnectionOracle();
        oradb.setConnection("kellylea", "oracle2");
        oradb.connecter();  
        out.println( "<div class='zeCatalogue'><table class='zeCatable'>" );
           //entête
        out.println( "<tr><td class='zeCatEntete'>Nom d'item</td>"
                   + "<td class='zeCatEntete'>En stock</td>"
                   + "<td class='zeCatEntete'>Prix</td>"
                   + "<td class='zeCatEntete'>Poids</td>"
                   + "<td class='zeCatEntete'>Genre</td>"
                   + "<td class='zeCatEntete'>Ajouter au panier</td></tr>" );

        try
        {
            ResultSet rst;
              try (CallableStatement stm = oradb.getConnection().prepareCall("{ ? = call Gestion_Catalogue.recherchecatalogue(?,?)}", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY )) {
                  stm.registerOutParameter(1, OracleTypes.CURSOR);
                  stm.setString( 2, genre );
                  stm.setString(3, cle+"%");
                  stm.execute();
                  rst = (ResultSet)stm.getObject(1);
                  // parcours du ResultSet
                  while( rst.next() )
                  {
                    /*NOMITEM, QUANTITE, PRIX, POIDS, GENRE*/
                    out.println( "<tr class='zeCatalogueRow'>" );
                    numitem = ((Integer)rst.getInt("NUMITEM")).toString();
                    nomitem = rst.getString( "NOMITEM" );
                    qte = ((Integer)rst.getInt("QUANTITE")).toString();
                    prix = ((Integer)rst.getInt("PRIX")).toString();
                    poids = ((Integer)rst.getInt("POIDS")).toString();
                    genreItem = rst.getString( "GENRE" );

                    out.println( "<input type=\"hidden\" name=\"numitem\" value=\"" 
                            + numitem + "\"/><td class='zeCatalogueCell'>" 
                            + nomitem + "</td><td class='zeCatalogueCell'>" 
                            + qte + "</td><td class='zeCatalogueCell'>"
                            + prix + "</td><td class='zeCatalogueCell'>" 
                            + poids + "</td><td class='zeCatalogueCell'>" 
                            + genreItem + "</td><td class='zeCatalogueCell'>"
                            + "<input type=\"text\" name='qte' size='2' >"
                            + "<input type=\"submit\" value=\"Ajouter\" class=\"b_submit\"/></td>" );
                    out.println( "</tr>" );
                }  
            }
            rst.close();    
        }
        catch( SQLException se ) 
        {
           out.println( se.getMessage() );
        }
        out.println( "</table></div>" );
        oradb.deconnecter(); 
    }
    
    
    private void barreNavigation(PrintWriter out){
            //ajouter barre de navigation href vers debut, précédent, suivent et fin
            // exemple de barre de navigation
      //   out.println( "<a href='insertion'>Insertion</a>" + " | " +
      //         "<a href='recherche'>Recherche</a>" );
          
    }
}
    
