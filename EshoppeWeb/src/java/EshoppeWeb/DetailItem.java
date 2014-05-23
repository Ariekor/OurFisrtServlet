/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EshoppeWeb;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.CallableStatement;
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
import oracle.jdbc.OracleTypes;

/**
 *
 * @author Isabelle
 */
@WebServlet(name = "DetailItem", urlPatterns = {"/detailitem"})
public class DetailItem extends HttpServlet {

     /////a récupérer de session Tomcat...
    String nomUser;
    HttpSession session;    
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
        UtilHtml.afficherErreurPage(out, session);
        UtilHtml.barreDeMenu(out, session); 
        
        //contenu page
        out.println("<div id='mainInscriptionDiv'>");
            out.println("<table id='zeInscritpion'>");

                afficherGeneral(out);
                afficherDetails(out);

            out.println("</table>");
        out.println("</div>");
                
        UtilHtml.piedsDePage(out, session);
        out.close();
    }
    
    private void afficherGeneral(PrintWriter out)
    {
        //récupérer nom item et image et autres données non affichées dans catalogue
        String nomitem ="";
        int prix=0;
        int quantite=0;
        int poids=0;
        //String urlImage;//mettre glogal si on n'affiche pas dans l'ordre...
        
        ConnectionOracle connBd = new ConnectionOracle();
        connBd.setConnection("kellylea", "oracle2");
        connBd.connecter();
        try
        { 
            CallableStatement stm = connBd.getConnection().prepareCall("{ ? = call gestion_catalogue.listercatalogue(?) }");
            stm.registerOutParameter(1, OracleTypes.CURSOR);
            stm.setInt(2, numItem);
            stm.execute();
            ResultSet rst = (ResultSet)stm.getObject(1);
                        
            if  (rst.next())
            {
               nomitem = rst.getString("nomitem");
               prix = (rst.getInt("prix"));    
               quantite = (rst.getInt("quantite"));    
               poids = (rst.getInt("poids"));               
            } 
            rst.close();
            stm.close();  
        }
        catch(SQLException e){session.setAttribute("Erreur", e.getMessage());}
        finally{connBd.deconnecter();}
        out.println("<tr class='rowHeigh'>");
            out.println("<td class='zeTitre' colspan='2'>"+genre+ " : " +nomitem+" </td>"); 
        out.println("</tr>");
        //mettre en page les données communes
        attributsCommuns(out, prix, quantite, poids);  
    }
    
    private void attributsCommuns(PrintWriter out, int prix, int quantite, int poids)
    {
        out.println("<tr>");
            out.println("<td  class='zeDetailCellLeft'>PRIX : </td>");
            out.println("<td  class='zeDetailCellRight'>"+prix+"</td>");
        out.println("</tr>");
        out.println("<tr>");
            out.println("<td  class='zeDetailCellLeft'>STOCK : </td>");
            out.println("<td  class='zeDetailCellRight'>"+quantite+"</td>");
        out.println("</tr>");
        out.println("<tr>");
            out.println("<td  class='zeDetailCellLeft'>POIDS : </td>");
            out.println("<td  class='zeDetailCellRight'>"+poids+"</td>");
        out.println("</tr>");        
    }
    private void afficherDetails(PrintWriter out)
    {        
        ConnectionOracle connBd = new ConnectionOracle();
        connBd.setConnection("kellylea", "oracle2");
        connBd.connecter();
        try
        {  
            String leGenre = genre + "s";
            //récupérer les données et metadata des tables genre
            String SQLDetail = "Select * From "+leGenre+" where numitem='"+numItem+"'";
            Statement stmDetail = connBd.getConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rstDetail = stmDetail.executeQuery(SQLDetail);  
            
            //La fonction stockée ne fonctionne pas avec les "méta-datas"
    /*        CallableStatement stmDetail = connBd.getConnection().prepareCall("{ ? = call GESTION_CATALOGUE.listerInfosGenre(?,?) }");
            stmDetail.registerOutParameter(1, OracleTypes.CURSOR);
            stmDetail.setString(2, leGenre);
            stmDetail.setInt(3, numItem);
            
            stmDetail.execute();
            ResultSet rstDetail = (ResultSet)stmDetail.getObject(1);*/
            
            ResultSetMetaData rstMD = rstDetail.getMetaData();
            int nbCol = rstMD.getColumnCount();
             
            while (rstDetail.next())
            {                
                String nomCol;
                String val="";
                int typeCol;
                
                for (int i=2; i <= nbCol; ++i)
                {                    
                    nomCol = rstMD.getColumnName(i);   
                    typeCol = rstMD.getColumnType(i);
                    if(typeCol == 4 || typeCol == 2)
                    {
                        val = "" + rstDetail.getInt(i);
                    }
                    else if ( typeCol == 12 || typeCol == -16 || typeCol == -1)
                    {
                        val = rstDetail.getString(i);
                    }                    
                    out.println("<tr>");
                        out.println("<td  class='zeDetailCellLeft'>"+nomCol+" : </td>");
                        out.println("<td  class='zeDetailCellRight'>"+val+"</td>");
                    out.println("</tr>");
                }                 
            } 
            rstDetail.close();
            stmDetail.close();              
        }
        catch(SQLException e){session.setAttribute("Erreur", e.getMessage()); }
        finally{connBd.deconnecter();}
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
