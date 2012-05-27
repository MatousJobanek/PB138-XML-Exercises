/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlExercises.xslt;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nu.xom.*;
import nu.xom.xslt.XSLException;
import nu.xom.xslt.XSLTransform;
import xmlExercises.*;

/**
 * @author Matous Jobanek
 */
@WebServlet(name = "XSLTServlet", urlPatterns = {Constants.URL_XSLT_TASK, Constants.URL_XSLT_RESULT})
public class XSLTServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(XSLTServlet.class.getPackage().toString());

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        if (request.getServletPath().contains(Constants.TASK)) {
            task(request, response);
        } else if (request.getServletPath().contains(Constants.RESULT)) {
            result(request, response);
        } else {
            throw new RuntimeException("Unknown operation: " + request.getServletPath());
        }

    }

    public void task(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        try {
            Assignment assignment = XSLTUtils.getAssignment();

            //            System.err.println(assignment.getHtmlOutputAsString());
            //            System.err.println(assignment.getAssignmentText());

            request.setAttribute(Assignment.class.getSimpleName(), assignment);
            request.getRequestDispatcher(Constants.JSP_ASSIGNMENT).forward(request, response);
        } catch (SyntaxErorException ex) {
            returnError(request, response, "There has been occured some problem " + ex.getMessage());
        }
    }

    public void result(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String userSolution = request.getParameter("userSolution");
        if (userSolution != null && !"".equals(userSolution)) {
            try {

                ServletContext context = getServletContext();
                String id = request.getParameter("id");
                XSLTResult result;

//                System.err.println(userSolution);

                result = XSLTUtils.evaluate(userSolution, id);

//                System.err.println(result.isIsCorrect());
//                System.err.println(result.getCorrectHTML());
//                System.err.println(result.getUserHTML());


                request.setAttribute(XSLTResult.class.getSimpleName(), result);
                request.getRequestDispatcher(Constants.JSP_XSLT_RESULT).forward(request, response);

            } catch (SyntaxErorException ex) {
                returnError(request, response, "Problem with the syntax in your input \n" + ex.getMessage());
            }

        } else {
            returnError(request, response, "The input is empty");
        }
    }

    private void returnError(HttpServletRequest request, HttpServletResponse response, String message) throws ServletException, IOException {
        request.setAttribute(Constants.ATTRIBUTE_ERROR, message);
        request.getRequestDispatcher(Constants.JSP_ERROR).forward(request, response);
    }
    
        // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
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
