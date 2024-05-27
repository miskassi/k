package it.unisa.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.unisa.model.ProdottoBean;
import it.unisa.model.ProdottoDao;

/**
 * Servlet implementation class HomeServlet
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Definisci una whitelist di pagine consentite
    private static final List<String> ALLOWED_PAGES = Arrays.asList("Home.jsp", "Account.jsp", "Carrello.jsp", "Catalogo.jsp","Checkout.jsp","ComposizioneOrdine.jsp","Dettagli.jsp","Login.jsp","MieiOrdini.jsp","Ps4.jsp","Ps5.jsp","Registrazione.jsp","Switch.jsp","XboxOne.jsp","XboxSeries.jsp");

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        ProdottoDao dao = new ProdottoDao();
        
        ArrayList<ArrayList<ProdottoBean>> categorie = new ArrayList<>();
        String redirectedPage = request.getParameter("page");

        // Validazione del parametro 'page'
        if (redirectedPage == null || !ALLOWED_PAGES.contains(redirectedPage)) {
            // Se la pagina non è valida, reindirizza a una pagina di errore o una pagina predefinita
            redirectedPage = "Home.jsp"; 
        }
        
        else if (redirectedPage.contains("WEB-INF") || redirectedPage.contains("META-INF")) {
            // Se la pagina richiama una directory sensibile, reindirizza a una pagina di errore o una pagina predefinita
            redirectedPage = "Home.jsp"; // Oppure "error.jsp"
        }

        try {
            ArrayList<ProdottoBean> PS5 = dao.doRetrieveByPiattaforma("PlayStation 5");
            ArrayList<ProdottoBean> XboxSeries = dao.doRetrieveByPiattaforma("Xbox Series");
            ArrayList<ProdottoBean> Switch = dao.doRetrieveByPiattaforma("Nintendo Switch");
            ArrayList<ProdottoBean> PS4 = dao.doRetrieveByPiattaforma("PlayStation 4");
            ArrayList<ProdottoBean> Xone = dao.doRetrieveByPiattaforma("Xbox One");
            
            categorie.add(PS5);
            categorie.add(XboxSeries);
            categorie.add(Switch);
            categorie.add(PS4);
            categorie.add(Xone);

            request.getSession().setAttribute("categorie", categorie);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/" + redirectedPage);
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}