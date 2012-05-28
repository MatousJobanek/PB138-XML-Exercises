/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlExercises;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Zlej robočlověk
 */
public class main {
    
    
    public static void main(String[] args){
        String tempPath = "./temp";
        Evaluator eve = null;
        
        String result = "nicsenestalo";
        File file = new File("./kontakty.xsd");
        String expresion = "chyba";
        
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            
            expresion = Charset.defaultCharset().decode(bb).toString();
            eve = new SchemaEvaluator(tempPath);
        }
        catch (IOException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }        finally {
            try {
                stream.close();
            } catch (IOException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            if(eve != null) result = eve.eval(expresion,"./priklady/oukol/");
        } catch (SyntaxErorException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ie){
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ie);        
        } catch (OverflowException io){
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, io);
        }
        
        System.out.println(result);
    }
    
}
