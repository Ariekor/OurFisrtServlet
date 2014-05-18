package EshoppeWeb;

import java.io.PrintWriter;
import javax.servlet.http.HttpSession;

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
    
    
    static public void enteteHtml(PrintWriter out, String page){    
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>É-Shop-pe : "+page+"</title>"); 
        out.println("<meta charset=\"UTF-8\">");
        out.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");   
        out.println("<link rel=\"stylesheet\" href=\"Eshoppe.css\"  type=\"text/css\"/>");
        out.println("<script type=\"text/javascript\" src=\"./FiltresChamp.js\"></script>");
        out.println("</head>");
        out.println("<body>");
        out.println("<a href='http://localhost:8080/eshoppeweb/catalogue' target='_parent'>"
                + "<img src='bann.png' alt='' />"
                + "</a>"); 
    }
    static protected void afficherErreurPage(PrintWriter out, HttpSession session)
    {   
        String message = (String)session.getAttribute("Erreur");
        if(message != null && !message.equals(""))
        {
            out.println("<span class='erreur'>Attention: " + session.getAttribute("Erreur")+"</span>");
        }
    }
    static protected void barreDeMenu(PrintWriter out, HttpSession session ){
            out.println("<table class='entete_catalogue'>");
                out.println("  <tr><td id='logo'><a href='http://localhost:8080/eshoppeweb/catalogue' target='_parent'>"
                        + "<img src=\"logo.png\" alt=\"\" /></a></td>");
        String nomUser = (String)session.getAttribute("Nom_Joueur");
        if(nomUser != null) //est fonction du cookie session...
        {    enteteJoueur(out, nomUser);}
        else
        {    enteteVisiteur(out);       }
        
        out.println("</tr>");
        out.println("</table>");    
    }
    static protected void enteteJoueur(PrintWriter out, String nomUser){
        //Section menu donnant acces au profil
        out.println("<td id='profil'><form action='profil' method='post' class='zePosRight'>"
                        + nomUser + " " + "<input type=\"submit\" value=\"Profil\" class=\"b_submit\" /></form>"
                 + "</td>");
        
        //Section menu donnant accès au panier.
        out.println("<td id='panier'><form action='panier' method='post' class='zePosLeft'>"
                + "<input type=\"submit\" value=\"Panier\" class=\"b_submit\" /></form>"
                  + "</td>"); 
        
        //Section menu permettant la validation du membre du catalogue
        out.println("<td id='deconnecter'><form action='logout' method='post' class='zePosRight'>"
                + "<input type=\"submit\" value=\"Se déconnecter\" class=\"b_submit\" /></form>"
                  + "</td>");  
    }
    static protected void enteteVisiteur(PrintWriter out){
        
        /*<form action='login' method='post'>*/
        out.println("<td id='login'><form action='login' method='post'>"
                + "Alias: <input type=\"text\" name=\"user\" class='marge' />"
                + " Mot de passe: <input type=\"password\" class='marge' name=\"motdepasse\"/>"
                + "<input type=\"submit\" value=\"Login\" class=\"b_submit\"/>"
                + "</form></td>");
        /*<form action='inscription' method='post'>*/
        out.println("<td id='inscription'><form action='inscription' method='get'>"
                + "<input type=\"submit\" value=\"S'inscrire\" class=\"b_submit\" /></form></td>");
    }
    static protected void piedsDePage(PrintWriter out, HttpSession session){
        session.removeAttribute("Erreur");
        out.println("</body>");
        out.println("</html>");
    }    
}
