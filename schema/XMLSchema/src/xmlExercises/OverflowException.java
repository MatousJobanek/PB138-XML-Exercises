/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlExercises;

/**
 *
 * @author Zlej robočlověk
 */
public class OverflowException  extends Exception {

    public OverflowException(String message, Throwable cause) {
        super(message, cause);
    }
    public OverflowException(String message) {
        super(message);
    }
}
