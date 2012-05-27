/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlExercises.xslt;

/**
 *
 * @author Matous Jobanek
 */
public class XSLTResult {
    

    private boolean isCorrect;
    
    private String userHTML;

    private String correctHTML;

    private String userHTMLAsString;

    private String correctHTMLAsString;

    public XSLTResult(boolean isCorrect, String userHTML, String correctHTML, String userHTMLAsString, String correctHTMLAsString) {
        this.isCorrect = isCorrect;
        this.userHTML = userHTML;
        this.correctHTML = correctHTML;
        this.userHTMLAsString = userHTMLAsString;
        this.correctHTMLAsString = correctHTMLAsString;
    }

    public String getCorrectHTML() {
        return correctHTML;
    }

    public void setCorrectHTML(String correctHTML) {
        this.correctHTML = correctHTML;
    }

    public String getCorrectHTMLAsString() {
        return correctHTMLAsString;
    }

    public void setCorrectHTMLAsString(String correctHTMLAsString) {
        this.correctHTMLAsString = correctHTMLAsString;
    }

    public boolean isIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public String getUserHTML() {
        return userHTML;
    }

    public void setUserHTML(String userHTML) {
        this.userHTML = userHTML;
    }

    public String getUserHTMLAsString() {
        return userHTMLAsString;
    }

    public void setUserHTMLAsString(String userHTMLAsString) {
        this.userHTMLAsString = userHTMLAsString;
    }
    
    
    
}
