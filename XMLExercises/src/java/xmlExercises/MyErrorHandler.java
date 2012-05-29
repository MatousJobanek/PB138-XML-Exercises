/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlExercises;

import java.io.PrintStream;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author Zlej robočlověk
 */
public class MyErrorHandler implements ErrorHandler {
    private PrintStream out;

    
    public MyErrorHandler() {
        
    }

    private String getParseExceptionInfo(SAXParseException spe) {
        String systemId = spe.getSystemId();

        if (systemId == null) {
            systemId = "null";
        }

        String info = "URI=" + systemId + " Line=" 
            + spe.getLineNumber() + ": " + spe.getMessage();

        return info;
    }

    @Override
    public void warning(SAXParseException spe) throws SAXException {
         String message = ("Warning: " + getParseExceptionInfo(spe));
         throw new SAXException(message);
    }
        
    @Override
    public void error(SAXParseException spe) throws SAXException {
        String message = "Error: " + getParseExceptionInfo(spe);
        throw new SAXException(message);
    }

    @Override
    public void fatalError(SAXParseException spe) throws SAXException {
        String message = "Fatal Error: " + getParseExceptionInfo(spe);
        throw new SAXException(message);
    }
}
