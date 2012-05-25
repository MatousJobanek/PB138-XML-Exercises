package xmlExercises;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author slaweet
 */
public class Utils {

    public static String replaceTags(String s) {
        s = s.replace("<", "&lt;");
        s = s.replace(">", "&gt;");
        return s;
    }

    static Task getTask(int id, String type) {
        Task t = new Task();
        t.setId(id);
        t.setType(type);

        try {
            String path = Utils.getPathTo(type, id);
            t.setSolution(readFile(path + "solution.xq"));
            t.setText(readFile(path + "text.txt"));
            List<String> data = new ArrayList<String>();
            for (int i = 1; i < 5; i++) {
                data.add(readFile(path + "data" + i + ".xml"));
            }
            t.setData(data);
        } catch (IOException ex) {
            //t.setSolution("<table></table>");
            t.setText("Not implemented yet");
            //t.setData("<table>s</table>");
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return t;
    }

    static Evaluator getEvaluator(String type) {
        if ("xquery".equals(type)) {
            return new XQueryEvaluator();
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

    static String readFile(String path) throws IOException {
        FileInputStream stream = new FileInputStream(new File(path));
        try {
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            /*
             * Instead of using default, pass in a decoder.
             */
            return Charset.defaultCharset().decode(bb).toString();
        } finally {
            stream.close();
        }
    }
    
    static List<String> scanDirectoryStructure(String path) {

        File dir = new File(path);
        FileFilter filter = new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return !pathname.isFile();
            }

        };
        File[] dirs = dir.listFiles(filter);
        if (dirs == null || dirs.length == 0) {
            //LOGGER.log(Level.SEVERE, "There is no assignments on this path: " + path);
            return null;
        }

        ArrayList<String> assignments = new ArrayList<String>(dirs.length);
        for (int i = 0; i < dirs.length; i++) {
            assignments.add(dirs[i].getName());
        }

        return assignments;
    }
    
    public static String getPathTo(String type, int id) {
         return Utils.getPathTo(type) + File.separator + id + File.separator;
    }
    
    public static String getPathTo(String type, String id) {
         return Utils.getPathTo(type) + File.separator + id + File.separator;
    }
    
    public static String getPathTo(String type) {
         return System.getProperty("user.home") + File.separator + 
                 Constants.ASSIGNMENTS_FOLDER_NAME + File.separator + type;
    }
}
