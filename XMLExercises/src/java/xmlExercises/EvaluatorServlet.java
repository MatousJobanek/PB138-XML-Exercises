package xmlExercises;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author slaweet
 */
@WebServlet(name = "EvaluatorServlet",
urlPatterns = {EvaluatorServlet.ACTION_RESULT, EvaluatorServlet.ACTION_TASK})
public class EvaluatorServlet extends HttpServlet {

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
    static final String ACTION_TASK = "/Task";
    static final String ACTION_RESULT = "/Restult";
    static final String ATTRIBUTE_TASK = "task";
    static final String ATTRIBUTE_RESULT = "result";
    static final String JSP_TASK = "/task.jsp";
    static final String JSP_RESULT = "/result.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        if (request.getServletPath().equals(ACTION_TASK)) {
            task(request, response);
        } else if (request.getServletPath().equals(ACTION_RESULT)) {
            result(request, response);
        } else {
            throw new RuntimeException("Unknown operation: " + request.getServletPath());
        }


    }

    private void task(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Task task = Utils.getTask(1, request.getParameter("type"));
        task.replaceTags();
        request.setAttribute(ATTRIBUTE_TASK, task);
        request.getRequestDispatcher(JSP_TASK).forward(request, response);
    }

    private void result(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String userSolution = request.getParameter("userSolution");
        String type = request.getParameter("type");
        int id = Integer.parseInt(request.getParameter("id"));

        Evaluator evaluator = Utils.getEvaluator(type);
        Task task = Utils.getTask(id, type);
        Result result = new Result();
        ServletContext context = getServletContext();
        //String path = context.getContextPath() + "/" + type + "/" + id + "/data.xml";
        String path = id + "/data.xml";

        try {
            result.setCorrectSolution(evaluator.eval(task.getSolution(), path));
            result.setUserSolution(evaluator.eval(userSolution, path));
            result.setIsCorrect(evaluator.compare(result.getCorrectSolution(), result.getUserSolution()));
        } catch (SyntaxErorException ex) {
            result.setUserSolution(ex.getMessage());
            result.setIsCorrect(false);
            Logger.getLogger(EvaluatorServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        result.replaceTags();
        request.setAttribute(ATTRIBUTE_RESULT, result);
        request.getRequestDispatcher(JSP_RESULT).forward(request, response);
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
