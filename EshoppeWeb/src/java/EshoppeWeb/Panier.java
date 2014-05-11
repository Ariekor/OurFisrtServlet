package EshoppeWeb;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author Isabelle
 */
@WebServlet(urlPatterns = {"/panier"})
public class Panier extends HttpServlet {

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
            UtilHtml.enteteHtml(out,"Panier");
            UtilHtml.barreDeMenu(out, true);
            
            
            out.println("<h1>Panier</h1>");
            
            
            listeItems(out, "zazer");
            //si clic bouton envoyer, doit retourner à catalogue avec  
            // les champs de login user rempli mais pas mot depasse.  Pas encore logué.
            out.println("<form action='catalogue' method='post'>");
            
            
            out.println("</form>");
            
            UtilHtml.piedsDePage(out);
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

    private void listeItems(PrintWriter out, String nomUser ){
        String numitem;
        String nomitem;
        int qte ;
        int prixUnitaire;
        int prixCalcule ;        
        
        // connexion à la base de données
        ConnectionOracle oradb = new ConnectionOracle();
        oradb.setConnection("kellylea", "oracle2");
        oradb.connecter();  
        out.println( "<div id='zeCatalogueDiv'><table id='zeCatable'>" );
           //entête panier
        out.println( "<tr><td class='zeCatEntete'>Nom d'item</td>"
                   + "<td class='zeCatEntete'>Qté au panier</td>"
                   + "<td class='zeCatEntete'>Prix unitaire</td>"
                    + "<td class='zeCatEntete'>Prix calculé</td>"
                   + "<td class='zeCatEntete'>Retirer du panier</td>");

        try
        {
            ResultSet rst;
              try (CallableStatement stm = oradb.getConnection().prepareCall("{ ? = call Gestion_Panier.lister(?)}", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY )) {
                  stm.registerOutParameter(1, OracleTypes.CURSOR);
                  stm.setString( 2, nomUser );                  
                  stm.execute();
                  rst = (ResultSet)stm.getObject(1);
                  // parcours du ResultSet
                  while( rst.next() )
                  {
                    //récupérer les valeurs de chaque ligne
                    out.println( "<tr class='zeCatalogueRow'>" );
                    numitem = ((Integer)rst.getInt("NUMITEM")).toString();
                    nomitem = rst.getString( "NOMITEM" );
                    qte = (Integer)rst.getInt("QUANTITEITEM");
                    prixUnitaire = (Integer)rst.getInt("PRIX");
                    prixCalcule =  ((Integer)rst.getInt("QUANTITEITEM"))*((Integer)rst.getInt("PRIX"));                    
                    //chaqueligne est un form qui permet de retirer un objet du panier
                    out.println( "<input type=\"hidden\" name=\"numitem\" value=\"" 
                            + numitem + "\"/><td class='zeCatalogueCell'>" 
                            + nomitem + "</td><td class='zeCatalogueCell'>" 
                            + "<input type=\"text\" name='qte' value =\""+qte+"\" size='3'></td><td class='zeCatalogueCell'>"
                            + prixUnitaire + "</td><td class='zeCatalogueCell'>" 
                            + prixCalcule + "</td><td class='zeCatalogueCell'>" 
                            + "<input type=\"buton\" value=\"X\" class=\"b_submit\"/></td>" );
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
}
