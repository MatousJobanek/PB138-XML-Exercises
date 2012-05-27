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

    public XSLTResult(boolean isCorrect, String userHTML, String correctHTML) {
        this.isCorrect = isCorrect;
        this.userHTML = userHTML;
        this.correctHTML = correctHTML;
    }

    public boolean isIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

   

    public String getCorrectHTML() {
        return correctHTML;
    }

    public void setCorrectHTML(String correctHTML) {
        this.correctHTML = correctHTML;
    }

    public String getUserHTML() {
        return userHTML;
    }

    public void setUserHTML(String userHTML) {
        this.userHTML = userHTML;
    }
    
    
    
}
