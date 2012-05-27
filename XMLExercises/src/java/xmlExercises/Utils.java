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

    static Task getTask(String id, String type) {
        Task t = new Task();
        t.setId(id);
        t.setType(type);

        List<String> data = new ArrayList<String>();
        try {
            String path = getPathTo(type, id);
            t.setSolution(readFile(path + "solution" + getSuffix(type)));
            t.setText(readFile(path + "text.txt"));
            if ((new File(path + "initsolution" + getSuffix(type))).exists()) {
                t.setInitSolution(readFile(path + "initsolution" + getSuffix(type)));
            } else {
                t.setInitSolution("");
            }
            for (int i = 1; (new File(path + "data" + i + ".xml")).exists(); i++) {
                data.add(readFile(path + "data" + i + ".xml"));
            }
        } catch (IOException ex) {
            t.setText("No excercise found.");
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
        t.setData(data);

        return t;
    }

    static Evaluator getEvaluator(String type) {
        if ("xquery".equals(type)) {
            return new XQueryEvaluator();
        }
        if ("xpath".equals(type)) {
            return new XPathEvaluator();
        }
        if ("dtd".equals(type)) {
            return new DTDEvaluator();
        }
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static String readFile(String path) throws IOException {
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

    public static String getPathTo(String type, String id) {
        return getPathTo(type) + File.separator + id + File.separator;
    }

    public static String getPathTo(String type) {
        return System.getProperty("user.home") + File.separator
                + Constants.ASSIGNMENTS_FOLDER_NAME + File.separator + type;
    }

    public static String getSuffix(String type) {
        if ("xquery".equals(type)) {
            return ".xq";
        } else if ("xpath".equals(type)) {
            return ".xpath";
        } else if ("dtd".equals(type)) {
            return ".dtd";
        } else if ("xmlschema".equals(type)) {
            return ".xsd";
        } else {
            return null;
        }
    }

    public static String formatOutputHtml(String toFormat) {
        char lastChar = toFormat.charAt(0);
        int tabsNum = 0;
        boolean lastWasSlash = false;

        for (int i = 1; i < toFormat.length(); i++) {
            char currentChar = toFormat.charAt(i);

            if (!lastWasSlash && currentChar == '<'
                    && toFormat.substring(i + 1, toFormat.indexOf(">", i + 1)).contains("/")) {
                if (tabsNum > 0) {
                    tabsNum--;
                }
                lastWasSlash = true;
            }

            if (currentChar == '>') {
                int nextBracket = toFormat.indexOf(">", i);
                if (!lastWasSlash || (nextBracket > 0 && toFormat.charAt(nextBracket - 1) == '/')) {
                    tabsNum++;
                    lastWasSlash = false;
                } else {
                    lastWasSlash = false;
                }
            }

            if ((lastChar == '>' && (currentChar == ' ' || currentChar == '\t' || currentChar == '\n'))
                    || (currentChar == '\n')) {
                toFormat = toFormat.substring(0, i) + toFormat.substring(i + 1, toFormat.length());
                i--;
            } else if ((lastChar == '>') || (currentChar == '<' && lastChar != '>')) {
                StringBuffer buffer = new StringBuffer("");
                for (int j = 0; j < tabsNum - 1; j++) {
                    buffer.append(Constants.MY_TAB);
                }
                toFormat =
                        toFormat.substring(0, i) + Constants.MY_LINE_SEPARATOR + buffer.toString()
                        + toFormat.substring(i, toFormat.length());
                lastChar = currentChar;
                i += 1 + (tabsNum > 1 ? tabsNum : 0);
            } else {
                lastChar = currentChar;
            }
        }

        String[] splited = toFormat.split(Constants.MY_LINE_SEPARATOR);
        StringBuffer bufferedOutput = new StringBuffer("");
        int pom = 2;
        for (int i = 0; i < splited.length; i++) {

            if (pom == 2) {
                if (splited.length > i + 2 && splited.length > 1) {

                    String first = splited[i].replace("\t", "");
                    if ((first.substring(0, 1) + "/" + first.substring(1)).equals(splited[i + 2].replace("\t", ""))) {
                        pom--;
                    }
                }
            } else if (pom > 0) {
                pom--;
                splited[i] = splited[i].replace("\t", "");
            } else {
                pom = 2;
                splited[i] = splited[i].replace("\t", "");
            }
            bufferedOutput.append(splited[i] + (pom == 2 ? "\\n" : ""));

        }
        return bufferedOutput.toString().replace(Constants.MY_TAB, "\\t");
    }
}
