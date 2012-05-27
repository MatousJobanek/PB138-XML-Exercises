
package xmlExercises.xslt;

import nu.xom.Document;

/**
 * @author Matous Jobanek
 */

public class Assignment {

    private String name;

    private String level;

    private String type;
    
    private Document xmlDocument;
    
    private String xml;

    private String assignmentText;

    private String htmlOutput;

    private String htmlOutputAsString;

    public Assignment() {
    }

    
    
    public Assignment(String name, String level, Document xmlDocument, String xml, String assignmentText, String htmlOutput, String htmlOutputAsString) {
        this.name = name;
        this.level = level;
        this.xmlDocument = xmlDocument;
        this.xml = xml;
        this.assignmentText = assignmentText;
        this.htmlOutput = htmlOutput;
        this.htmlOutputAsString = htmlOutputAsString;
    }

    public String getAssignmentText() {
        return assignmentText;
    }

    public void setAssignmentText(String assignmentText) {
        this.assignmentText = assignmentText;
    }

    public String getHtmlOutput() {
        return htmlOutput;
    }

    public void setHtmlOutput(String htmlOutput) {
        this.htmlOutput = htmlOutput;
    }

    public String getHtmlOutputAsString() {
        return htmlOutputAsString;
    }

    public void setHtmlOutputAsString(String htmlOutputAsString) {
        this.htmlOutputAsString = htmlOutputAsString;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public Document getXmlDocument() {
        return xmlDocument;
    }

    public void setXmlDocument(Document xmlDocument) {
        this.xmlDocument = xmlDocument;
    }
   
    
}