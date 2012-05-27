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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nu.xom.*;
import nu.xom.xslt.XSLException;
import nu.xom.xslt.XSLTransform;
import xmlExercises.Constants;
import xmlExercises.SyntaxErorException;
import xmlExercises.Utils;

/**
 *
 * @author jobas
 */
public class XSLTUtils {
    
    private static final Logger LOGGER = Logger.getLogger(XSLTUtils.class.getPackage().toString());

    /**
     * @param newXSL
     * @param name
     */
    public static XSLTResult evaluate(String newXSL, String name) throws SyntaxErorException {
        Assignment assignment = scanDirectory(Utils.getPathTo("xslt", name));
        
        try {
            Document transformed = transform(assignment.getXmlDocument(), newXSL, false);
            
            boolean equal =
                    testForEquality(assignment.getHtmlOutputAsString(),
                    Utils.formatOutputHtml(transformed.toXML()));

//            System.err.println(formatOutput(transformed.toXML()));
//            System.err.println(assignment.getHtmlOutputAsString());

            
            return new XSLTResult(equal, transformed.toXML(), assignment.getHtmlOutput(), Utils.formatOutputHtml(transformed.toXML()), assignment.getHtmlOutputAsString());
            
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
    public static boolean testForEquality(String originalXml, String userXml) {
        
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
    
    public static String getMyTestXslt() {
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
    public static Assignment getAssignment() throws SyntaxErorException {
        
        try {
            
            List<Assignment> assignments = scanDirectoryStructure(Utils.getPathTo("xslt"));
            Assignment assignment = null;
            if (assignments.size() > 0) {
                Random randomGenerator = new Random();
                assignment = assignments.get(randomGenerator.nextInt(assignments.size()));
            }
            if (assignment == null) {
                assignment = new Assignment();
                assignment.setAssignmentText("No excercise found.");
            }
            String pathToInitSol = System.getProperty("user.home")
                    + File.separator + Constants.ASSIGNMENTS_FOLDER_NAME + File.separator
                    + "xslt" + File.separator + "initSolution";
            if ((new File(pathToInitSol)).exists()) {
                assignment.setInitSolution(Utils.readFile(pathToInitSol).replace("\"", "\\\"").replace("\n", "\\n"));
            } else {
                assignment.setInitSolution("");
            }
            
            return assignment;
            
        } catch (IOException ex) {
            Logger.getLogger(XSLTUtils.class.getName()).log(Level.SEVERE, null, ex);
            throw new SyntaxErorException("There is a problem with filesystem " + ex.getMessage());
        }
    }
    
    public static List<Assignment> scanDirectoryStructure(String path) throws SyntaxErorException {
        
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
    
    public static Assignment scanDirectory(String dirPath) throws SyntaxErorException {
        
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
    public static Assignment createAssignment(String dirPath,
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
            throw new SyntaxErorException(e.getMessage());
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Problem", e);
            throw new SyntaxErorException(e.getMessage());
            
        } catch (XSLException e) {
            LOGGER.log(Level.SEVERE, "Problem", e);
            throw new SyntaxErorException(e.getMessage());
            
        }
        return null;
    }
    
    public static String getBodyContent(String html) {
//        System.out.println("html: " +  html);
        int indexOfBody = html.toLowerCase().indexOf("<body>");
        return html.substring(html.indexOf(">", indexOfBody) + 1, html.toLowerCase().indexOf("</body>")).replace("\n", "\\n").replace("\"", "\\\"");
    }
    
    public static Document transform(Document xmlDocument, String XSL, boolean isFile) throws XSLException,
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
}
