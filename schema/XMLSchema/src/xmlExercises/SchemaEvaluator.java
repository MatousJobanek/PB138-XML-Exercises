/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlExercises;


/**
 *
 * @author Zlej robočlověk
 */
public class SchemaEvaluator implements Evaluator{
    @Override
    // Recieves directory name, as tarballing would be unnecessary
    public String eval(String expresion, String dirName) throws SyntaxErorException{
        
        return "y u do dis?";
    }
    
    @Override
    public boolean compare(String result1, String result2){
        return result1.equals(result2);
    }
}
