package xmlExercises;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
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
        String path = type + "/" + id + "/";
        t.setId(id);
        t.setType(type);

        try {
            t.setSolution(readFile(path + "solution.txt"));
            t.setText(readFile(path + "text.txt"));
            t.setData(readFile(path + "data.xml"));
        } catch (IOException ex) {
            t.setSolution("<table></table>");
            t.setText("Napište xquery výraz, který...");
            t.setData("<table>s</table>");
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
}
