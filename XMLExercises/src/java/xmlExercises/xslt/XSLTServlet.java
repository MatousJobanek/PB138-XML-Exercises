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

    private void task(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
         
        try {
            Assignment assignment = getAssignment();
            
            if (assignment == null){
                assignment = new Assignment();
                assignment.setAssignmentText("No excercise found.");
            }
//            System.err.println(assignment.getHtmlOutputAsString());
//            System.err.println(assignment.getAssignmentText());
            
            
            request.setAttribute(Assignment.class.getSimpleName(), assignment);
            request.getRequestDispatcher(Constants.JSP_ASSIGNMENT).forward(request, response);
        } catch (SyntaxErorException ex) {
            returnError(request, response, "There has been occured some problem " + ex.getMessage());
        }        
    }

    private void result(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String userSolution = request.getParameter("userSolution");
        if (userSolution != null && !"".equals(userSolution)) {
            try {

                ServletContext context = getServletContext();
                String id = request.getParameter("id");
                XSLTResult result;

//                System.err.println(userSolution);
                
                result = evaluate(userSolution, id);
                
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

    

    public String format(Document document) {

        Serializer serializer;
        try {
            serializer = new Serializer(System.out, "UTF-8");
            serializer.setIndent(4);
            serializer.setMaxLength(64);
            serializer.write(document);
            return document.toXML();
        } catch (UnsupportedEncodingException e) {
            // TODO:logging
            e.printStackTrace();
        } catch (IOException e) {
            // TODO:logging
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param newXSL
     * @param name
     */
    private XSLTResult evaluate(String newXSL, String name) throws SyntaxErorException {
        Assignment assignment = scanDirectory(Utils.getPathTo("xslt", name));

        try {
            Document transformed = transform(assignment.getXmlDocument(), newXSL, false);

            boolean equal =
                    testForEquality(assignment.getHtmlOutputAsString(),
                    Utils.formatOutputHtml(transformed.toXML()));

//            System.err.println(formatOutput(transformed.toXML()));
//            System.err.println(assignment.getHtmlOutputAsString());

            
            return new XSLTResult(equal, transformed.toXML(),  assignment.getHtmlOutput(), Utils.formatOutputHtml(transformed.toXML()), assignment.getHtmlOutputAsString());

        } catch (XSLException e) {
            LOGGER.log(Level.SEVERE, "Problem", e);
            e.printStackTrace();

        } catch (ParsingException e) {
            LOGGER.log(Level.SEVERE, "Problem", e);
            e.printStackTrace();

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Problem", e);
            e.printStackTrace();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Problem", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param xml
     * @param xml2
     */
    private boolean testForEquality(String originalXml, String userXml) {

        String[] originalSplited = originalXml.split(Constants.MY_LINE_SEPARATOR);
        String[] userSplited = userXml.split(Constants.MY_LINE_SEPARATOR);

        if (originalSplited.length != userSplited.length) {
            return false;
        }
        for (int i = 0; i < userSplited.length; i++) {
            String regex = "[" + Constants.MY_TAB + "," + Constants.MY_LINE_SEPARATOR + "]";
            String originalLine = originalSplited[i].replaceAll(regex, "");
            String userLine = userSplited[i].replaceAll(regex, "");
            if (!originalLine.equals(userLine)) {
//                System.err.println("original: " + originalSplited[i]);
//                System.err.println("user: " + userSplited[i]);
                return false;
            }
        }
        return true;
    }

    private String getMyTestXslt() {
        Builder builder = new Builder();
        try {
            return builder.build(new File("resources/beginner/1beginner/address-book.xslt")).toXML();
        } catch (ValidityException e) {
            LOGGER.log(Level.SEVERE, "Problem", e);
            e.printStackTrace();

        } catch (ParsingException e) {
            LOGGER.log(Level.SEVERE, "Problem", e);
            e.printStackTrace();

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Problem", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param string
     */
    private Assignment getAssignment() throws SyntaxErorException {

        String homeFolder = System.getProperty("user.home");
        List<Assignment> assignments = scanDirectoryStructure(Utils.getPathTo("xslt"));
        if (assignments.size() > 0) {
            Random randomGenerator = new Random();
            return assignments.get(randomGenerator.nextInt(assignments.size()));
        }
        return null;
    }

    private List<Assignment> scanDirectoryStructure(String path) throws SyntaxErorException {

        File dir = new File(path);
        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return !pathname.isFile();
            }
        };
        File[] dirs = dir.listFiles(filter);
        if (dirs == null || dirs.length == 0) {
            LOGGER.log(Level.SEVERE, "There is no assignments on this path: " + path);
            return null;
        }

        ArrayList<Assignment> assignments = new ArrayList<Assignment>(dirs.length);
        for (int i = 0; i < dirs.length; i++) {
            Assignment assignment = scanDirectory(dirs[i].getPath());
            if (assignment != null) {
                assignments.add(assignment);
            }
        }

        return assignments;
    }

    private Assignment scanDirectory(String dirPath) throws SyntaxErorException {

        File dir = new File(dirPath);
        File[] files = dir.listFiles();

        if (files == null || files.length == 0) {
            LOGGER.log(Level.INFO, "There is no assignments on this path: " + dirPath);
            return null;
        }

        String xmlDoc = null;
        String assignmentText = null;
        String xsl = null;
        for (int i = 0; i < files.length; i++) {
            if (files[i].getPath().toLowerCase().endsWith("xml")) {
                xmlDoc = files[i].getPath();
            } else if (files[i].getPath().toLowerCase().endsWith("txt")) {
                assignmentText = files[i].getPath();
            } else if (files[i].getPath().toLowerCase().endsWith("xsl")) {
                xsl = files[i].getPath();
            }
        }

        if (xmlDoc == null || assignmentText == null || xsl == null) {
            return null;
        }

        return createAssignment(dirPath, xmlDoc, assignmentText, xsl);

    }

    /**
     * @param dirPath
     * @param xmlDocPath
     * @param assignmentTextPath
     * @param xslPath
     * @return
     */
    private Assignment createAssignment(String dirPath,
            String xmlDocPath,
            String assignmentTextPath,
            String xslPath) throws SyntaxErorException {
        try {
            String name = dirPath.substring(dirPath.lastIndexOf(File.separator) + 1);

            Builder builder = new Builder();
            Document xmlDocument = builder.build(new File(xmlDocPath));

            BufferedReader reader = new BufferedReader(new FileReader(assignmentTextPath));

            StringBuffer assignmentBuffer = new StringBuffer("");
            while (reader.ready()) {
                assignmentBuffer.append(reader.readLine()).append("\n");
            }

            Document htmlOutput = transform(xmlDocument, xslPath, true);

            if (name != null && xmlDocument != null && !"".equals(xmlDocument.toXML())
                    && assignmentBuffer.toString() != null && !"".equals(assignmentBuffer.toString())
                    && htmlOutput != null && !"".equals(htmlOutput.toXML())) {
                String htmlOuputAsString = Utils.formatOutputHtml(htmlOutput.toXML().trim());
//                System.err.println(assignmentBuffer.toString());
//                System.err.println("htmlOutput: " + htmlOutput.toXML());
                return new Assignment(name,
                        dirPath.substring(dirPath.lastIndexOf(File.separator) + 1),
                        xmlDocument,
                        xmlDocument.toXML().replace("\n", "\\n"),
                        assignmentBuffer.toString().replace("\n", "\\n"),
                        getBodyContent(htmlOutput.toXML()).replace("\\n", ""),
                        htmlOuputAsString);
            }

        } catch (ValidityException e) {
            Logger.getLogger(XSLTServlet.class.getName()).log(Level.SEVERE, null, e);
            throw new SyntaxErorException("the input is not valid \n" + e.getMessage());

        } catch (ParsingException e) {
            LOGGER.log(Level.SEVERE, "Problem", e);
            e.printStackTrace();

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Problem", e);
            e.printStackTrace();

        } catch (XSLException e) {
            LOGGER.log(Level.SEVERE, "Problem", e);
            e.printStackTrace();

        }
        return null;
    }

    private String getBodyContent(String html) {
//        System.out.println("html: " +  html);
        int indexOfBody = html.toLowerCase().indexOf("<body>");
        return html.substring(html.indexOf(">", indexOfBody)+1, html.toLowerCase().indexOf("</body>")).replace("\n", "\\n").replace("\"", "\\\"");
    }

    public Document transform(Document xmlDocument, String XSL, boolean isFile) throws XSLException,
            ParsingException, IOException, SyntaxErorException {
        try {
            
            Builder builder = new Builder();
            Document stylesheet = isFile ? builder.build(XSL) : builder.build(new ByteArrayInputStream(XSL.getBytes()));
            XSLTransform transform = new XSLTransform(stylesheet);

            return XSLTransform.toDocument(transform.transform(xmlDocument));

        } catch (XMLException e) {
            Logger.getLogger(XSLTServlet.class.getName()).log(Level.SEVERE, null, e);
            throw new SyntaxErorException(e.getMessage());
            
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
