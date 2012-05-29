/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlExercises;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;


/**
 *
 * @author Alexandr Toptygin
 */
public class SchemaEvaluator implements Evaluator{
    String victoryMessage = "Odpoved prosla vsemi testy! Hura!";
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    TempFileHandler tfh = null;
    ErrorHandler eh = new MyErrorHandler();
    boolean initError;
    
    // Recieves directory name, as tarballing would be unnecessary
    public SchemaEvaluator(String tempDir){
        initError = false;
        try{tfh = new TempFileHandler(tempDir, "xsd");}
        catch( IOException iox){
            Logger.getLogger(SchemaEvaluator.class.getName()).log(Level.SEVERE, null, iox);
            initError = true;
        }
        factory = DocumentBuilderFactory.newInstance();        
        factory.setValidating(true);
        factory.setAttribute(
          "http://java.sun.com/xml/jaxp/properties/schemaLanguage", 
          "http://www.w3.org/2001/XMLSchema");    
    }
    private String checkWrong(String dirName, Document doc){
        File TestDir = new File(dirName);
        String result = victoryMessage;
        boolean CorrectlyInvalid = true;
        String[] dirList = TestDir.list();
        for (int i = 0; i < dirList.length; i++)  {
        try{
                if((dirList[i].split("_"))[0].equalsIgnoreCase("err")){
                    
                    DocumentBuilder parser = factory.newDocumentBuilder();
                    parser.setErrorHandler(eh);
                    CorrectlyInvalid = false;
                    doc = parser.parse(dirName.concat(dirList[i]));
                    if(!CorrectlyInvalid){
                        result = "Chyba> Test " + (dirList[i].split("_"))[1] + " nemel vubec projit.";
                    }
                }
           }
        catch (ParserConfigurationException e){
             result = "Parseru se neco nelibi> "+e.getMessage();
           }
        catch (SAXException e){
             CorrectlyInvalid = true;
           }
        catch (IOException e){
             result = "Something's wrong. Call an admin! Tell him that: " + e.getMessage();
           }
         }
        if(CorrectlyInvalid) return victoryMessage;
        return result;

    }
    
    
    private String checkCorrect(String dirName, Document doc){        
        File TestDir = new File(dirName);
        String result = victoryMessage;
        String[] dirList = TestDir.list();
        for (int i = 0; i < dirList.length; i++)  {
        try{                
                if((dirList[i].split("_"))[0].equalsIgnoreCase("cor")){                    
                    DocumentBuilder parser = factory.newDocumentBuilder();
                    parser.setErrorHandler(eh);
                    doc = parser.parse(dirName.concat(dirList[i]));
                }
           }
        catch (ParserConfigurationException e){
             result = e.getMessage();
             
           }
        catch (SAXException e){
             result = ("Parsing XML failed in "+dirList[i]+" due to a " + e.getClass().getName() + ":\n\t" +e.getMessage());             
           }
        catch (IOException e){
             result = "Something's wrong. Call an admin! Tell him that: " + e.getMessage();
           }
         }        
        return result;
    }
    
    @Override
    public String eval(String expresion, String dirName) throws SyntaxErorException{
        if(initError) return "Kontaktuj admina, docasne uloziste je spatne nastaveno.";
        String result;
        
        try{
        
        String path = tfh.addFile(expresion);
        //tfh.addDirectory("hnus", "solution.dtd", new File("build.xml"));
        factory.setAttribute(
          "http://java.sun.com/xml/jaxp/properties/schemaSource",
          ".."+File.separator+".."+File.separator+path);
        Document doc = null;
        
        result = checkCorrect(dirName, doc);
        if(result.equals(victoryMessage)) result = checkWrong(dirName, doc);
        
        tfh.deleteFile(path);
        return result;
        }
        catch(Exception e){
            Logger.getLogger(SchemaEvaluator.class.getName()).log(Level.SEVERE, null, e);
            return "It did not work!" + e.getMessage();
        }
        
    }
    
    @Override
    public boolean compare(String result1, String result2){
        return result1.equals(result2);
    }
}
