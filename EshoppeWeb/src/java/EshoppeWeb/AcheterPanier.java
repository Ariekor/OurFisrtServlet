/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EshoppeWeb;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
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
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;

/**
 *
 * @author Isabelle
 */
@WebServlet(name = "AcheterPanier", urlPatterns = {"/acheterpanier"})
public class AcheterPanier extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
   HttpSession session;
   String erreur ="";
   String nomUsager;
   String URL = "http://localhost:8080/eshoppeweb/catalogue";
   ResultSet rstPanier;
   int capUser;
   int facture;
   int depense;

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
        session = request.getSession();
        nomUsager = (String)session.getAttribute("Nom_Joueur");
        capUser = Integer.parseInt( request.getParameter("cap"));
        facture = Integer.parseInt( request.getParameter("total"));
        acheterPanier();
        if ( erreur.equals(""))
        {
            erreur = "Achat réussi votre # de confirmation est :"+  (int)(Math.random() * (20000000 - 10000000));
        }
        UtilHtml.gererErreurs(session, erreur);
        response.sendRedirect(URL);
    }
    
    private void acheterPanier()
    {
        if (facture > capUser)
        {
            erreur = "Vous n'avez pas assez d'écus\n";
            URL = "http://localhost:8080/eshoppeweb/panier";
        }
        else
        {
            // connexion à la base de données
            ConnectionOracle oradb = new ConnectionOracle();
            oradb.setConnection("kellylea", "oracle2");
            oradb.connecter();
            try
            {   
                CallableStatement stmP = oradb.getConnection().prepareCall("{ ? = call Gestion_Panier.lister(?)}" );
                stmP.registerOutParameter(1, OracleTypes.CURSOR);
                stmP.setString( 2, nomUsager );
                stmP.execute();
                rstPanier = (ResultSet)stmP.getObject(1);  
            
                // pour chaque ligne:
                while ( rstPanier.next() )
                {
                    //récupérer valeurs utiles
                    int numitem = rstPanier.getInt("NUMITEM");
                    String nomitem = rstPanier.getString( "NOMITEM" );
                    int qte = (Integer)rstPanier.getInt("QUANTITEITEM");
                    int prixUnitaire = (Integer)rstPanier.getInt("PRIX");
                    int prixCalcule =  prixUnitaire*qte;
                    int stock = (Integer)rstPanier.getInt("STOCK");
                    
                    if (stock >= qte && qte != 0)
                    {
                        //ajouter à inventaire
                        erreur += ajouterInventaire(nomUsager,numitem,qte);
                        //update qte du catalogue
                        mettreAJourCatalogue(numitem,qte,stock);
                        //retirer du panier
                        retirerPanier(nomUsager,numitem);
                        //augment la dépense += prix*qte
                        if (erreur.equals(""))
                        {
                            depense += prixCalcule;                            
                        }                        
                    }
                    //sinon
                    else
                    {
                        erreur += "La quantité n'est pas valide\n";
                        URL = "http://localhost:8080/eshoppeweb/panier";
                    }
                }
            }
            catch( SQLException sqe )
            {
                erreur += sqe.getMessage()+"\n";
            }
            finally { oradb.deconnecter(); }
            //update capital -= depense
            ajusterCapital(nomUsager, capUser - depense);
        }
    }
    
    private void ajusterCapital(String nom , int cap)
    {
        ConnectionOracle odc = new ConnectionOracle();
        odc.setConnection("kellylea", "oracle2");
        odc.connecter();
        try
        {
            CallableStatement stm = odc.getConnection().prepareCall("{call GESTION_USERS.MODIFIERCAPITAL( ? , ? )}");
            stm.setString(1, nom);
            stm.setInt(2, cap);
            int modifie = stm.executeUpdate();
            if (modifie == 0)
            {
                session.setAttribute("Erreur", "Solde non ajustée");
            }            
            stm.close();            
        }
        catch(SQLException s){session.setAttribute("Erreur", s.getMessage()+ "\n");}
        finally{odc.deconnecter();}
    }
    private void  mettreAJourCatalogue(int numItem,int qte, int stock)
    {
         ConnectionOracle odc = new ConnectionOracle();
        odc.setConnection("kellylea", "oracle2");
        odc.connecter();
        try
        {
            CallableStatement stm = odc.getConnection().prepareCall("{call GESTION_CATALOGUE.MODIFIERQUANTITE( ? , ? )}");
            stm.setInt(1, numItem);
            stm.setInt(2, stock - qte );
            int modifie = stm.executeUpdate();
            if (modifie == 0)
            {
                session.setAttribute("Erreur", "Quantité non ajustée.");
            }            
            stm.close();            
        }
        catch(SQLException s){session.setAttribute("Erreur", s.getMessage()+ "\n");}
        finally{odc.deconnecter();}
    }
    
    private void retirerPanier(String nomUser, int numItem)
    {
        //GESTION_PANIER.SUPPRIMER(numItem,nomUsage)
        ConnectionOracle oradbRetirer = new ConnectionOracle();
            oradbRetirer.setConnection("kellylea", "oracle2");
            oradbRetirer.connecter();
            try{
                CallableStatement stm = oradbRetirer.getConnection().prepareCall("{call GESTION_PANIER.SUPPRIMER( ? , ? )}");
                stm.setInt(1, numItem);
                stm.setString(2, nomUser);
                               
                int retrait = stm.executeUpdate();
                if (retrait == 0)
                {
                    erreur += "\n L'item n'a pas été retiré...\n ";
                }
                stm.close();                
            }
            catch(SQLException sqe){session.setAttribute("Erreur", sqe.getMessage()+ "\n");}
            finally{oradbRetirer.deconnecter();}     
        
    }
    
    
    private String ajouterInventaire(String nomUser, int numItem, int quantite)
    {
        String err = "";
        if(estDejaDansInventaire(nomUser,numItem))
        {
            modifierQte(nomUser,numItem,quantite);
        }
        else
        {
            ConnectionOracle odc = new ConnectionOracle();
            odc.setConnection("kellylea", "oracle2");
            odc.connecter();
            
            try{
                CallableStatement stm = odc.getConnection().prepareCall("{call GESTION_INVENTAIREJOUEUR.INSERTION( ? , ? , ? )}");
                stm.setString(1, nomUser);
                stm.setInt(2, numItem);
                stm.setInt(3, quantite);
                int ajout = stm.executeUpdate();
                if (ajout == 0)
                {
                    err += "\n L'item n'est pas ajouté...\n ";
                }
                stm.close();                
            }
            catch(SQLException sqe){ erreur += sqe.getMessage()+ "\n";}
            finally{odc.deconnecter();}
        }        
        return err;
    }
    private boolean estDejaDansInventaire(String nom , int numItem)
    {
        boolean valide = false;
        String sqlLogin = "select nomusager from inventairejoueur where nomusager = '"+nom+"' and numitem = '"+numItem+"'";
        ConnectionOracle oradb = new ConnectionOracle();
        oradb.setConnection("kellylea", "oracle2");
        oradb.connecter(); 
        try
        {   
            Statement stm = oradb.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rst = stm.executeQuery(sqlLogin);
            if(rst.first())
            {
                valide = true;
            }
            rst.close();
            stm.close();            
        }
        catch (SQLException e){erreur += e.getMessage()+ "\n";} 
        finally {oradb.deconnecter();}
        return valide;
    }
    
    
    
    private int obtenirQtePanier (String nom , int numItem)
    {
        int qte = 0;
        String Sql= "Select QUANTITEITEM FROM inventairejoueur where nomusager='"+nom+"' and numitem='"+numItem+"'";
        ConnectionOracle oradbPanier = new ConnectionOracle();
        oradbPanier.setConnection("kellylea", "oracle2");
        oradbPanier.connecter();
        try
        {
            Statement stm = oradbPanier.getConnection().createStatement();
            ResultSet rst = stm.executeQuery(Sql);
            if (rst.next())
            {
                qte = rst.getInt(1);
            }
            rst.close();
            stm.close();
        }
        catch(SQLException s){erreur += s.getMessage() + "\n";}
        finally{oradbPanier.deconnecter();}
        return qte;
    }
    
    private void modifierQte(String nom , int numItem , int quantite)
    {
        ConnectionOracle odc = new ConnectionOracle();
        odc.setConnection("kellylea", "oracle2");
        odc.connecter();
        try{
            CallableStatement stm = odc.getConnection().prepareCall("{call GESTION_INVENTAIREJOUEUR.MODIFIERQUANTITE( ? , ? , ? )}");
            stm.setInt(1, numItem);
            stm.setString(2,nom);
            stm.setInt(3, quantite + obtenirQtePanier(nom,numItem) );
            int modifie = stm.executeUpdate();
            if (modifie == 0)
            {
                erreur += "Quantité non ajustée.\n";
            }            
            stm.close();            
        }
        catch(SQLException s){erreur += s.getMessage()+ "\n";}
        finally{odc.deconnecter();}
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
