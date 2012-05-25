
package xmlExercises.xslt;


/**
 * @author Matous Jobanek
 */

public class Result {

    private Assignment assignment;

    private boolean successful;

    private String xsl;

    private String transformed;

    /**
     * @param assignment
     * @param successful
     * @param xsl
     * @param transformed
     */

    public Result(Assignment assignment, boolean successful, String xsl, String transformed) {
        super();
        this.assignment = assignment;
        this.successful = successful;
        this.xsl = xsl;
        this.transformed = transformed;
    }

    /**
     * @return the assignment
     */

    public Assignment getAssignment() {
        return assignment;
    }

    /**
     * @param assignment
     *        the assignment to set
     */

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    /**
     * @return the successful
     */

    public boolean getSuccessful() {
        return successful;
    }

    /**
     * @param successful
     *        the successful to set
     */

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    /**
     * @return the xsl
     */

    public String getXsl() {
        return xsl;
    }

    /**
     * @param xsl
     *        the xsl to set
     */

    public void setXsl(String xsl) {
        this.xsl = xsl;
    }

    /**
     * @return the transformed
     */

    public String getTransformed() {
        return transformed;
    }

    /**
     * @param transformed
     *        the transformed to set
     */

    public void setTransformed(String transformed) {
        this.transformed = transformed;
    }

}
