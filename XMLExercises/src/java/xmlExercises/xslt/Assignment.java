
package xmlExercises.xslt;

import nu.xom.Document;

/**
 * @author Matous Jobanek
 */

public class Assignment {

    private String name;

    private String level;

    private Document xml;

    private String assignmentText;

    private Document htmlOutput;

    /**
     * @param name
     * @param level
     * @param xml
     * @param assignmentText
     * @param htmlOutput
     */

    public Assignment(String name, String level, Document xml, String assignmentText, Document htmlOutput) {
        super();
        this.name = name;
        this.level = level;
        this.xml = xml;
        this.assignmentText = assignmentText;
        this.htmlOutput = htmlOutput;
    }

    /**
     * @return the name
     */

    public String getName() {
        return name;
    }

    /**
     * @param name
     *        the name to set
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the level
     */

    public String getLevel() {
        return level;
    }

    /**
     * @param level
     *        the level to set
     */

    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * @return the xml
     */

    public Document getXml() {
        return xml;
    }

    /**
     * @param xml
     *        the xml to set
     */

    public void setXml(Document xml) {
        this.xml = xml;
    }

    /**
     * @return the assignmentText
     */

    public String getAssignmentText() {
        return assignmentText;
    }

    /**
     * @param assignmentText
     *        the assignmentText to set
     */

    public void setAssignmentText(String assignmentText) {
        this.assignmentText = assignmentText;
    }

    /**
     * @return the htmlOutput
     */

    public Document getHtmlOutput() {
        return htmlOutput;
    }

    /**
     * @param htmlOutput
     *        the htmlOutput to set
     */

    public void setHtmlOutput(Document htmlOutput) {
        this.htmlOutput = htmlOutput;
    }

}