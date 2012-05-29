package xmlExercises;

/**
 *
 * @author vasekhodina
 */

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.xml.sax.InputSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathExpressionException;

public class XPathEvaluator implements Evaluator{
    /*
     * @param String expression - xpath expression to evaluate
     * @param String filename - path to XML file to evaluate XPath on
     * 
     * @return String with node content selected by XPath
     */
    @Override
    public String eval(String expression,String filename){
        
        String result = "";
        FileReader fr = null;
        try {fr = new FileReader(filename);}
        catch(FileNotFoundException fnf){System.out.println(fnf.toString());}
        InputSource input = new InputSource(fr);
        
        XPathFactory xpathFact = XPathFactory.newInstance();
        XPath xpath = xpathFact.newXPath();
        
        
        try {result = xpath.evaluate(expression, input);}
        catch(XPathExpressionException xpe){System.out.println(xpe.toString());}
        
        if (result == null) System.out.println("null");
        
        try {fr.close();}
        catch (IOException ioe){System.out.println(ioe.toString());}
        
        return result;
    }
    
    /*
     * @param String result1 - result from User
     * @param String result2 - result from solution
     * 
     * @return Boolean true if results are the same
     */
    @Override
    public boolean compare(String result1, String result2){
        return result1.equals(result2);
    }
}