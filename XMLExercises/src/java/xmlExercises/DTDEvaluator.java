package xmlExercises;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import java.io.IOException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author vasekhodina
 */

public class DTDEvaluator{
    boolean valid = true;
    int errOnLine = 0;
    public String eval(String expresion, String fileName){

        DocumentBuilder dBuilder = null;
        Document doc = null;
        
        DocumentBuilderFactory docBuildFac = DocumentBuilderFactory.newInstance();
        docBuildFac.setValidating(true);
        
        try {dBuilder = docBuildFac.newDocumentBuilder();}
        catch (ParserConfigurationException pce){valid = false;}
        
        dBuilder.setErrorHandler(new org.xml.sax.ErrorHandler() {
            
            @Override
            public void fatalError(SAXParseException exception)
            throws SAXException {valid = false;errOnLine = exception.getLineNumber();}
            
            @Override
            public void error(SAXParseException exc)
            throws SAXParseException {valid = false;errOnLine = exc.getLineNumber();}
            
            @Override
            public void warning(SAXParseException e)
            throws SAXParseException {valid = false;errOnLine = e.getLineNumber();}
        });
        
        try {doc = dBuilder.parse(fileName);}
        catch (SAXException se){}
        catch(IOException ioe){}
        if (valid == true)
            return "Valid!";
        else
            return "Not Valid! Error on line:" + errOnLine;
    }
    
    public boolean compare(String result1, String result2){
        return true;
    }
    
}
