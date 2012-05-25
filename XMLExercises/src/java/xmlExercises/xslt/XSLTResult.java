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
    

    private boolean correct;
    
    private String userHTML;

    private String correctHTML;

    public XSLTResult(boolean correct, String userHTML, String correctHTML) {
        this.correct = correct;
        this.userHTML = userHTML;
        this.correctHTML = correctHTML;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
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
