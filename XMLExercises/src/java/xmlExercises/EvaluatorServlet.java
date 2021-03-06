package xmlExercises;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import java.io.IOException;
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

/**
 *
 * @author slaweet
 */
@WebServlet(name = "EvaluatorServlet",
urlPatterns = {EvaluatorServlet.ACTION_RESULT_XQUERY, EvaluatorServlet.ACTION_TASK_XQUERY,
    EvaluatorServlet.ACTION_RESULT_XPATH, EvaluatorServlet.ACTION_TASK_XPATH,
    EvaluatorServlet.ACTION_RESULT_DTD, EvaluatorServlet.ACTION_TASK_DTD,
    EvaluatorServlet.ACTION_RESULT_XMLSCHEMA, EvaluatorServlet.ACTION_TASK_XMLSCHEMA
})
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
    static final String ACTION_TASK = "/task/";
    static final String ACTION_RESULT = "/result/";
    static final String ACTION_TASK_XQUERY = "/task/xquery";
    static final String ACTION_RESULT_XQUERY = "/result/xquery";
    static final String ACTION_TASK_XPATH = "/task/xpath";
    static final String ACTION_RESULT_XPATH = "/result/xpath";
    static final String ACTION_TASK_DTD = "/task/dtd";
    static final String ACTION_RESULT_DTD = "/result/dtd";
    static final String ACTION_TASK_XMLSCHEMA = "/task/xmlschema";
    static final String ACTION_RESULT_XMLSCHEMA = "/result/xmlschema";
    static final String ATTRIBUTE_TASK = "task";
    static final String ATTRIBUTE_RESULT = "results";
    static final String JSP_TASK = "/task.jsp";
    static final String JSP_RESULT = "/result.jsp";
    static final String RESOURCES_DIR = "";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        if (request.getServletPath().contains(ACTION_TASK)) {
            task(request, response);
        } else if (request.getServletPath().contains(ACTION_RESULT)) {
            result(request, response);
        } else {
            throw new RuntimeException("Unknown operation: " + request.getServletPath());
        }


    }

    private void task(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String type = request.getServletPath().replace(ACTION_TASK, "");
        String id = request.getParameter("id");
        if(id == null) {
        List<String> tasks = Utils.scanDirectoryStructure(Utils.getPathTo(type));
        Random randomGenerator = new Random();
        if (tasks != null) {
            id = tasks.get(randomGenerator.nextInt(tasks.size()));
        } 
        }
        Task task = Utils.getTask(id, type);
        task.replaceTags();

        request.setAttribute(ATTRIBUTE_TASK, task);
        request.getRequestDispatcher(JSP_TASK).forward(request, response);
    }

    private void result(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String userSolution = request.getParameter("userSolution");
        String type = request.getServletPath().replace(ACTION_RESULT, "");
        String id = request.getParameter("id");

        Evaluator evaluator = Utils.getEvaluator(type);
        Task task = Utils.getTask(id, type);
        ServletContext context = getServletContext();
        //String path = context.getContextPath() + "/" + type + "/" + id + "/data.xml";
        String path = Utils.getPathTo(type, id);
        List<Result> results = new ArrayList();

        try {
            for (int i = 1; i <= task.getData().size(); i++) {
                Result result = new Result();
                String file = path + "data" + i + ".xml";
                result.setCorrectSolution(Utils.replaceTagsAndIndent(evaluator.eval(task.getSolution(), file), false));
                result.setUserSolution(Utils.replaceTagsAndIndent(evaluator.eval(userSolution, file), false));
                result.setIsCorrect(evaluator.compare(result.getCorrectSolution(), result.getUserSolution()));
                //result.replaceTags();
                results.add(result);
            }
            request.setAttribute(ATTRIBUTE_RESULT, results);
            request.getRequestDispatcher(JSP_RESULT).forward(request, response);

        } catch (SyntaxErorException ex) {

            request.setAttribute(Constants.ATTRIBUTE_ERROR, ex.getMessage());
            request.getRequestDispatcher(Constants.JSP_ERROR).forward(request, response);
        }
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
