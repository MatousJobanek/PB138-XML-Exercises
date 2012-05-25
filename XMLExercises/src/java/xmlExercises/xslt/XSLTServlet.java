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
import javax.xml.transform.TransformerConfigurationException;
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
        Assignment assignment = getAssignment();
//        System.err.println();
        request.setAttribute(Assignment.class.getSimpleName(), assignment);
        request.getRequestDispatcher(Constants.JSP_ASSIGNMENT).forward(request, response);
    }

    private void result(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String userSolution = request.getParameter("userSolution");
        ServletContext context = getServletContext();
        String id = request.getParameter("id");
        XSLTResult result = evaluate(userSolution, id);

        request.setAttribute(Constants.RESULT, result);
        request.getRequestDispatcher(Constants.JSP_XSLT_RESULT).forward(request, response);

//        } catch (SyntaxErorException ex) {
//
//            request.setAttribute(ATTRIBUTE_ERROR, ex.getMessage());
//            request.getRequestDispatcher(JSP_ERROR).forward(request, response);
//        }
    }

    private String formatOutput(String toFormat) {
        char lastChar = toFormat.charAt(0);
        int tabsNum = 0;
        boolean lastWasSlash = false;

        for (int i = 1; i < toFormat.length(); i++) {
            char currentChar = toFormat.charAt(i);

            if (!lastWasSlash && currentChar == '<'
                    && toFormat.substring(i + 1, toFormat.indexOf(">", i + 1)).contains("/")) {
                if (tabsNum > 0) {
                    tabsNum--;
                }
                lastWasSlash = true;
            }

            if (currentChar == '>') {
                int nextBracket = toFormat.indexOf(">", i);
                if (!lastWasSlash || (nextBracket > 0 && toFormat.charAt(nextBracket - 1) == '/')) {
                    tabsNum++;
                    lastWasSlash = false;
                } else {
                    lastWasSlash = false;
                }
            }

            if ((lastChar == '>' && (currentChar == ' ' || currentChar == '\t' || currentChar == '\n'))
                    || (currentChar == '\n')) {
                toFormat = toFormat.substring(0, i) + toFormat.substring(i + 1, toFormat.length());
                i--;
            } else if ((lastChar == '>') || (currentChar == '<' && lastChar != '>')) {
                StringBuffer buffer = new StringBuffer("");
                for (int j = 0; j < tabsNum - 1; j++) {
                    buffer.append(Constants.MY_TAB);
                }
                toFormat =
                        toFormat.substring(0, i) + Constants.MY_LINE_SEPARATOR + buffer.toString()
                        + toFormat.substring(i, toFormat.length());
                lastChar = currentChar;
                i += 1 + (tabsNum > 1 ? tabsNum : 0);
            } else {
                lastChar = currentChar;
            }
        }
        return toFormat;
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
    private XSLTResult evaluate(String newXSL, String name) {
        Assignment assignment = scanDirectory(Utils.getPathTo("xslt", name));

        try {
            Document transformed = transform(assignment.getXmlDocument(), newXSL, false);

            boolean equal =
                    testForEquality(formatOutput(assignment.getHtmlOutput()),
                    formatOutput(transformed.toXML()));

            System.err.println(transformed.toXML());
            System.err.println(assignment.getHtmlOutputAsString());
            
            return new XSLTResult(equal, formatOutput(transformed.toXML()), formatOutput(assignment.getHtmlOutputAsString()));

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
                System.err.println("original: " + originalSplited[i]);
                System.err.println("user: " + userSplited[i]);
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
    private Assignment getAssignment() {

        String homeFolder = System.getProperty("user.home");
        List<Assignment> assignments = scanDirectoryStructure(Utils.getPathTo("xslt"));
        if (assignments.size() > 0) {
            Random randomGenerator = new Random();
            return assignments.get(randomGenerator.nextInt(assignments.size()));
        }
        return null;
    }

    private List<Assignment> scanDirectoryStructure(String path) {

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

    private Assignment scanDirectory(String dirPath) {

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
            String xslPath) {
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
                String htmlOuput = formatOutput(htmlOutput.toXML().trim());
                System.err.println(assignmentBuffer.toString());
                System.err.println(assignmentTextPath);
                return new Assignment(name,
                        dirPath.substring(dirPath.lastIndexOf(File.separator) + 1),
                        xmlDocument,
                        xmlDocument.toXML().replace("\n", "\\n"),
                        assignmentBuffer.toString().replace("\n", "\\n"),
                        getBodyContent(htmlOutput.toXML()),
                        htmlOuput.replace("\n", "\\n"));
            }

        } catch (ValidityException e) {
            LOGGER.log(Level.SEVERE, "Problem", e);
            e.printStackTrace();

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
        return html.substring(html.indexOf("<body>") + 6, html.indexOf("</body>")).replace("\n", "\\n").replace("\"", "\\\"");
    }

    public Document transform(Document xmlDocument, String XSL, boolean isFile) throws XSLException,
            ParsingException, IOException {
        Builder builder = new Builder();
        Document stylesheet = isFile ? builder.build(XSL) : builder.build(XSL, null);
        XSLTransform transform = new XSLTransform(stylesheet);
        return XSLTransform.toDocument(transform.transform(xmlDocument));
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
