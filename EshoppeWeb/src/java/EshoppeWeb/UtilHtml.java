package EshoppeWeb;

import java.io.PrintWriter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Isabelle
 */
public class UtilHtml {
    //méthodes statiques
    
    
    static public void enteteHtml(PrintWriter out){    
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Catalogue É-Shop-pe</title>"); 
        out.println("<meta charset=\"UTF-8\">");
        out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        out.println("<link href=\"EshoppeCSS.css\" rel=\"stylesheet\" type=\"text/css\"/>");            
        out.println("</head>");
    }
    static protected void barreDeMenu(PrintWriter out, boolean connecté ){
        out.println("<div>");//Ligne de menu du haut
        out.println("   <table>");
        out.println("       <tr><td>LOGO</td>");
        if(connecté)
            enteteJoueur(out);
        else
            enteteVisiteur(out);
        out.println("       </tr> ");
        out.println("   </table>");
    
    }
    static protected void enteteJoueur(PrintWriter out){
        out.println("           <td><input type=\"submit\" value=\"Profil\"/>"
                                 + "Alias du joueur connecté</td>");////récupérer la valeur réelle
        out.println("           <td><input type=\"submit\" value=\"Se déconnecter\"/></td>");
        out.println("           <td><input type=\"submit\" value=\"Inventaire\"/></td>");     
        out.println("           <td><input type=\"submit\" value=\"Panier\"/></td>");   
    }
    static protected void enteteVisiteur(PrintWriter out){
        out.println("           <td><input type=\"submit\" value=\"Login\"/>"
                                    + " Alias: <input type=\"text\" name=\"user\" value=\"nom usager\"/>"
                                    + " Mot de passe: <input type=\"password\" name=\"motdepasse\"/></td>");
        
        out.println("           <td><input type=\"submit\" value=\"S'inscrire\"/></td>");
    }
    static protected void piedsDePage(PrintWriter out){
        out.println("</body>");
        out.println("</html>");
    }
    
}
