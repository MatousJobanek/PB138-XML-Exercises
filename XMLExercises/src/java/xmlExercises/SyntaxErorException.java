package xmlExercises;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author slaweet
 */
public class SyntaxErorException extends Exception {

    public SyntaxErorException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public SyntaxErorException(String message) {
        super(message);
    }
}
