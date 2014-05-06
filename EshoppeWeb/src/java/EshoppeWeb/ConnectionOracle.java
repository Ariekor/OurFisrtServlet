/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package EshoppeWeb;
import java.sql.*;
import oracle.jdbc.pool.*;

/**
 *
 * @author Isabelle
 */
public class ConnectionOracle {
    
    private String nomUsager;
    private String motdePasse;
    String url="jdbc:oracle:thin:@205.237.244.251:1521:orcl";
       /**
        * @param args the command line arguments
        */

    private Connection conn;
    public void setConnection( String u, String m)
    {
       nomUsager = u;
       motdePasse = m;
    }

    // La Méthode getConnection retourne un objet conn de type Connection
    public  Connection getConnection()
    {
       return conn;
    }

    //La méthode connecter qui sera appeler au moment de la connection
    // par le bouton  Se Connecter de l'interface (Classe Identifier)
    public void connecter()
    {
       try
       {
          OracleDataSource ods = new OracleDataSource();
          ods.setURL(url);
          ods.setUser(nomUsager);
          ods.setPassword(motdePasse);
          conn=ods.getConnection();
          System.out.println("vous êtes connectés");
       }
          catch(SQLException se)
          {
          System.out.println(se);       
          conn = null;
       }
    }

    public void deconnecter()
    {
          try 
          { 
               conn.close();
               System.out.println("connexion fermée");
          }
          catch(SQLException se)
          {
             conn = null;  
          }
    }
}
