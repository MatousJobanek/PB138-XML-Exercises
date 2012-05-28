package xmlExercises;

import java.io.File;
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

public class DTDEvaluator implements Evaluator{
    boolean valid = true;
    int errOnLine = 0;
    
    public String eval(String expresion, String fileName){

        DocumentBuilder dBuilder = null;
        Document doc = null;
        String pathToXml = null;
        TempFileHandler tempFileHandler = null;
        try {tempFileHandler = new TempFileHandler(System.getProperty("user.home") + File.separator + "xmlExercisesFiles"
        + File.separator + "temp","dtd");
        
        pathToXml = tempFileHandler.addDirectory(expresion, "solution.dtd", new File(fileName));
        }
        catch(Exception ex){ 
            System.out.println(ex.getMessage());
        }
        
        System.out.println(pathToXml);
        
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
        
        
        
        try {doc = dBuilder.parse(pathToXml);}
        catch (SAXException se){}
        catch(IOException ioe){}
        try {tempFileHandler.deleteDirectory(new File(pathToXml).getParent());}
        catch(IOException uhh){}
        if (valid == true)
            return "Valid!";
        else
            return "Not Valid! Error on line:" + errOnLine;
    }
    
    public boolean compare(String result1, String result2){
        return result1.equals(result2);
    }
    
}
