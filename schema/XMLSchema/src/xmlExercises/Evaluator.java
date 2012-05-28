package xmlExercises;

import java.io.IOException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author slaweet
 */
public interface Evaluator {

    
    public String eval(String expresion, String dirName) throws SyntaxErorException, IOException, OverflowException;
    public boolean compare(String result1, String result2);


}
