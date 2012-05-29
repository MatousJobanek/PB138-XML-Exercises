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
import org.xml.sax.SAXException;


/**
 *
 * @author Alexandr Toptygin
 */
public class SchemaEvaluator implements Evaluator{
    String victoryMessage = "Winrar is You!";
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    TempFileHandler tfh = null;
    
    // Recieves directory name, as tarballing would be unnecessary
    public SchemaEvaluator(String tempDir){
        try{tfh = new TempFileHandler(tempDir, "xsd");}
        catch( IOException iox){
            Logger.getLogger(SchemaEvaluator.class.getName()).log(Level.SEVERE, null, iox);
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
                    CorrectlyInvalid = false;
                    doc = parser.parse(dirName.concat(dirList[i]));                    
                }
           }
        catch (ParserConfigurationException e){
             result = e.getMessage();
           }
        catch (SAXException e){
             CorrectlyInvalid = true;
           }
        catch (IOException e){
             result = "Something's wrong. Call an admin!";
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
                System.out.println("Prochazim soubor "+i+">"+(dirList[i]));
                if((dirList[i].split("_"))[0].equalsIgnoreCase("cor")){
                    System.out.println("Testuju zadani "+i+">"+(dirList[i]));
                    DocumentBuilder parser = factory.newDocumentBuilder();
                    doc = parser.parse(dirName.concat(dirList[i]));
                }
           }
        catch (ParserConfigurationException e){
             result = e.getMessage();
           }
        catch (SAXException e){
             result = ("Parsing XML failed due to a " + e.getClass().getName() + ":\n" +e.getMessage());
           }
        catch (IOException e){
             result = "Something's wrong. Call an admin!";
           }
         }
        return result;
    }
    
    @Override
    public String eval(String expresion, String dirName) throws SyntaxErorException{
  
        String result;
        
        try{String path = tfh.addFile(expresion);
        
        factory.setAttribute(
          "http://java.sun.com/xml/jaxp/properties/schemaSource",
          path);
        Document doc = null;
        
        result = checkCorrect(dirName, doc);        
        if(result.equals(victoryMessage)) result = checkWrong(dirName, doc);
        
        //tfh.deleteFile(path);
        return result;
        }
        catch(Exception e){
            System.out.println("something went horribly wrong> " + e.getMessage());
            Logger.getLogger(SchemaEvaluator.class.getName()).log(Level.SEVERE, null, e);
            return "It did not work!";
        }
        
    }
    
    @Override
    public boolean compare(String result1, String result2){
        return result1.equals(result2);
    }
}
