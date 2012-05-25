package xmlExercises.xslt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.TransformerConfigurationException;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.Serializer;
import nu.xom.ValidityException;
import nu.xom.xslt.XSLException;
import nu.xom.xslt.XSLTransform;

/**
 * @author Matous Jobanek
 */
public class Main {

    public static final String MY_LINE_SEPARATOR = "\n";
    public static final String MY_TAB = "\t";
    public static final String ASSIGNMENTS_FOLDER_NAME = "resources/";
    private static final Logger LOGGER = Logger.getLogger(Main.class.getPackage().toString());

    /**
     * @param args
     */
    public static void main(String[] args) throws TransformerConfigurationException {
        //        if (false) {

        Assignment assignment = getAssignment("beginner");
        if (assignment != null) {
            //            System.err.println("name: " + assignment.getName() + "\n" + "\n" + "xml:" + "\n"
            //                    + format(assignment.getXml()) + "\n" + "\n" + "html:" + "\n"
            //                    + formatOutput(assignment.getHtmlOutput().toXML().trim()));
        } else {
            System.err.println("Problem");
        }
        //        } else {
        Result result = evaluate(getMyTestXslt(), "beginner", "1beginner");
        if (result == null) {
            System.err.println("Problem");
        } else {
            System.out.println("succesful: " + result.getSuccessful());
            //                System.out.println("your xsl: " + result.getXsl());
            //                System.out.println("your output: " + result.getTransformed());
            //            }
        }
    }

    private static String formatOutput(String toFormat) {
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
                    buffer.append(MY_TAB);
                }
                toFormat =
                        toFormat.substring(0, i) + MY_LINE_SEPARATOR + buffer.toString()
                        + toFormat.substring(i, toFormat.length());
                lastChar = currentChar;
                i += 1 + (tabsNum > 1 ? tabsNum : 0);
            } else {
                lastChar = currentChar;
            }
        }
        return toFormat;
    }

    public static String format(Document document) {

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
     * @param level
     * @param name
     */
    private static Result evaluate(String newXSL, String level, String name) {
        Assignment assignment = scanDirectory(ASSIGNMENTS_FOLDER_NAME + level + File.separator + name);

        try {
            Document transformed = transform(assignment.getXml(), newXSL, false);

            boolean equal =
                    testForEquality(formatOutput(assignment.getHtmlOutput()),
                    formatOutput(transformed.toXML()));

            return new Result(assignment, equal, newXSL, formatOutput(transformed.toXML()));

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
    private static boolean testForEquality(String originalXml, String userXml) {

        String[] originalSplited = originalXml.split(MY_LINE_SEPARATOR);
        String[] userSplited = userXml.split(MY_LINE_SEPARATOR);

        if (originalSplited.length != userSplited.length) {
            return false;
        }
        for (int i = 0; i < userSplited.length; i++) {
            String regex = "[" + MY_TAB + "," + MY_LINE_SEPARATOR + "]";
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

    private static String getMyTestXslt() {
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
    private static Assignment getAssignment(String level) {

        List<Assignment> assignments = scanDirectoryStructure(ASSIGNMENTS_FOLDER_NAME + level);
        if (assignments.size() > 0) {
            Random randomGenerator = new Random();
            return assignments.get(randomGenerator.nextInt(assignments.size()));
        }
        return null;
    }

    private static List<Assignment> scanDirectoryStructure(String path) {

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

    private static Assignment scanDirectory(String dirPath) {

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
    private static Assignment createAssignment(String dirPath,
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
                assignmentBuffer.append(reader.read());
            }

            Document htmlOutput = transform(xmlDocument, xslPath, true);

            if (name != null && !"".equals(name) && xmlDocument != null && !"".equals(xmlDocument.toXML())
                    && assignmentBuffer.toString() != null && !"".equals(assignmentBuffer.toString())
                    && htmlOutput != null && !"".equals(htmlOutput.toXML())) {
                String htmlOuput = formatOutput(htmlOutput.toXML().trim());
                return new Assignment(name,
                        dirPath.substring(dirPath.lastIndexOf(File.separator) + 1),
                        xmlDocument,
                        assignmentBuffer.toString(),
                        htmlOutput.toXML(),
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

    public static Document transform(Document xmlDocument, String XSL, boolean isFile) throws XSLException,
            ParsingException, IOException {
        Builder builder = new Builder();
        Document stylesheet = isFile ? builder.build(XSL) : builder.build(XSL, null);
        XSLTransform transform = new XSLTransform(stylesheet);
        return XSLTransform.toDocument(transform.transform(xmlDocument));
    }
}
