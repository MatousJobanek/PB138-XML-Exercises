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
    String error = "";
    
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
        catch (ParserConfigurationException pce){}
        
        dBuilder.setErrorHandler(new org.xml.sax.ErrorHandler() {
            
            @Override
            public void fatalError(SAXParseException e)
            throws SAXException {valid = false;errOnLine = e.getLineNumber();error=e.getMessage();}
            
            @Override
            public void error(SAXParseException e)
            throws SAXParseException {valid = false;errOnLine = e.getLineNumber();error=e.getMessage();}
            
            @Override
            public void warning(SAXParseException e)
            throws SAXParseException {valid = false;errOnLine = e.getLineNumber();error=e.getMessage();}
        });
        
        
        
        try {doc = dBuilder.parse(pathToXml);}
        catch (SAXException se){}
        catch(IOException ioe){}
        try {tempFileHandler.deleteDirectory(new File(pathToXml).getParent());}
        catch(IOException uhh){}
        if (valid == true)
            return "Valid!";
        else
            
            return "Not Valid! Error on line: " + errOnLine + "\n" + getFormated(error) ;
    }
    
    private String getFormated(String string){
        String[] splited = string.split(" ");
        StringBuffer buffered = new StringBuffer("");
        int count = 0;
        for (String s : splited){
            if (count < 5){
                
                count++;
            } else {
                count = 0;
                buffered.append("\n");
            }
            buffered.append(s + " ");
        }
        return buffered.toString();    }
    
    public boolean compare(String result1, String result2){
        return result1.equals(result2);
    }
    
}
