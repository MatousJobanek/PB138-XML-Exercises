/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlExercises;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
    File temp = null;
    String TempPath = "./temp";
    File TempDir = new File(TempPath);
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    
    private void CreateTempXSD(String s){        
        System.out.println(TempPath);
        String[] list = TempDir.list();
        
        int n = list.length;
        
        boolean found = false;
        while(!found){
            System.out.println("seresetotu?" + n);
            
            for (int i = 0; i < n; i++){
                String[] FName = (list[i]).split("\\.");
                System.out.println("sereseto " + FName.length +" whatthe "+list[i]+" wut "+FName[1]);
                if (Integer.parseInt(FName[0]) != i){
                    temp = new File(TempDir,Integer.toString(i-1).concat(".xsd"));
                    System.out.println("Wat" + i + FName[i]);
                    found = !temp.exists();
                    if(found) break;
                }
            }
            if(!found&&(n<9000)){
                temp = new File(TempDir,Integer.toString(n+1).concat(".xsd"));
                found = true;
            }
        }
        
        try {
            temp.createNewFile();
            FileWriter fw = new FileWriter(temp);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(s);
        } catch (IOException ex) {
            Logger.getLogger(SchemaEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("create is done");
    }
    // Recieves directory name, as tarballing would be unnecessary
    public SchemaEvaluator(){
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
                System.out.println("lol"+i+"wat"+(dirList[i].split("_")).length);
                if((dirList[i].split("_"))[0].equalsIgnoreCase("cor")){
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
        CreateTempXSD(expresion);
        
        factory.setAttribute(
          "http://java.sun.com/xml/jaxp/properties/schemaSource",
          temp.getAbsolutePath());
        Document doc = null;
        
        result = checkCorrect(dirName, doc);        
        if(result.equals(victoryMessage)) result = checkWrong(dirName, doc);
        
        temp.delete();
        return result;
    }
    
    @Override
    public boolean compare(String result1, String result2){
        return result1.equals(result2);
    }
}
