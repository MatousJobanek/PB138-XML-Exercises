/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xmlExercises;

import java.io.*;


/**
 *
 * @author Zlej robočlověk
 */

public class TempFileHandler{
    
    String tempPath;
    String type;
    File tempDir;
    
    
    /*
     * [in] String tempPathName -   path where the files are to be stored
     * [in] String fileType -       expects "xsd", "dtd" extension the files will use AND subdir
     *                              that will be used to store these files
     * TODO> parsing fileType (neccessary?)
     * 
     * Creates class that opens main directory with subdirectory for
     * specific file type. For each filetype there should be created
     * new instance of class. (To ease further overloading, if the DTD/XSD
     * differences will require it).
     * Throws obvious IOException, when the dir cannot be found or 
     * subdir found/created
     */
    public TempFileHandler(String tempPathName, String fileType) throws IOException{
        File rootTempPath = new File(tempPathName);
        type = fileType;
        
        if(!rootTempPath.isDirectory()){
            throw new IOException("Directory"+rootTempPath.getPath()+" does not exist");
        }
        
        tempPath = tempPathName + File.separator + fileType;
        tempDir =  new File(rootTempPath, fileType);        
        if(!tempDir.isDirectory()){
            tempDir.mkdir();
        }
        if(!rootTempPath.isDirectory()){
            throw new IOException("Directory/"+fileType+" does not exist");
        }
    }
    /*
     * [in] String exercise - DTD plaintext to save
     * [in] File xmlFile    - XML file to save too
     * 
     * return - path to XML file
     * 
     * Method adds temporary file to the folder, while creating a
     * filename thats not taken. Returns path to created file
     * 
     * TODO> better exceptions. Maybe split methods for easier catching? New exception handler?
     */
    public String addDirectory(String exercise, String DTDname, File xmlFile) throws IOException, OverflowException{
        String result = "NOPE";        
        File foundDir;
        File DTDFile;
        
        String rootDir = findDirName(); //temp/dtd/0
        
        foundDir = new File(rootDir); //temp/dtd/0
        File xmlCopy = new File(foundDir, xmlFile.getName());
        DTDFile = new File(rootDir+File.separator+DTDname);
        if(foundDir.mkdir()){
            if(!DTDFile.createNewFile()){
                throw new IOException("Neslo vytvorit DTD");
            }
            FileWriter fw = new FileWriter(DTDFile);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(exercise);
            bw.close();
            fw.close();
            if(xmlCopy.createNewFile()){
                copy(xmlFile, xmlCopy);            
            }else{
                throw new IOException("XML soubor jiz existuje.");
            }
        }else{
            throw new IOException("Adresar jiz existuje.");
        }
        return xmlCopy.getPath();
    }

    /*
     * [in] String exercise - plaintext to save
     *
     * return - path to file
     * 
     * Method adds temporary file to the folder, while creating a
     * filename thats not taken. Returns path to created file
     * 
     * TODO> better exceptions. Maybe split methods for easier catching? New exception handler?
     */
    public String addFile(String exercise) throws IOException, OverflowException{
        String result;        
        File foundDir;
        
        result = findFileName();
        
        foundDir = new File(result);
        if(foundDir.createNewFile()){
            
            FileWriter fw = new FileWriter(foundDir);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(exercise);
            bw.close();
            fw.close();            
        }else{
            throw new IOException("Soubor jiz existuje.");
        }
        return result;       
    }
    
    public void deleteFile(String delPath) throws IOException{
        File delFile = new File("."+File.separator+delPath);
        if(!delFile.delete()) throw new IOException("Failed to delete file: "+delPath);
    }
    
    
    public void deleteDirectory(String delPath) throws IOException{
        File delDir = new File(delPath);
        File[] delist = delDir.listFiles();
        
        for(int i=0; i<delist.length; i++){
            if(!delist[i].delete()) throw new IOException("Failed to delete file in DIR");
        }
        
        if(!delDir.delete()) throw new IOException("Failed to delete DIR");
    }

    
    private String findFileName() throws IOException, OverflowException{
        
        String resPath ="NOPE!";
        String list[] = tempDir.list();        
        boolean found = false;
        
        //check whether name is taken
        
        for(int i = 0; i<list.length; i++){
            boolean taken = false;
            for(int j = 0; j<list.length; j++){
                if(Integer.parseInt(list[j].split("\\.")[0])==i) taken = true;                
            }
            if(!taken){
                found = true;
                resPath = tempPath + File.separator +i+"."+ type;
                break;
            }
        }
        if(!found){
            if(list.length<9000){                
                int n = (list.length);
                resPath = tempPath + File.separator+n+"."+ type;                
            }else{
                throw new OverflowException("Prilis mnoho souboru.");
            }
        }
        if(resPath.equals("NOPE!")) throw new OverflowException("Tohle se vůbec nemělo stát");
        return resPath;
    }
    private String findDirName() throws IOException, OverflowException{
        
        String resPath ="NOPE!";
        String list[] = tempDir.list();        
        boolean found = false;
        
        //check whether name is taken
        
        for(int i = 0; i<list.length; i++){
            boolean taken = false;
            for(int j = 0; j<list.length; j++){
                if(Integer.parseInt(list[j])==i) taken = true;
            }
            if(!taken){
                resPath = tempPath + File.separator +i;
                found = true;

                break;
            }
        }
        if(!found){
            if(list.length<127){                
                int n = (list.length);
                resPath = tempPath + File.separator +n;                
            }else{
                throw new OverflowException("Prilis mnoho adresaru.");
            }
        }
        if(resPath.equals("NOPE!")) throw new OverflowException("Tohle se vůbec nemělo stát");
        return resPath;
    }
    private void copy(File original, File copy) throws IOException{
        InputStream in = new FileInputStream(original);  
        OutputStream out = new FileOutputStream(copy);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0){
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
}
