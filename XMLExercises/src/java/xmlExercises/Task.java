package xmlExercises;

import java.util.ArrayList;
import java.util.List;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author slaweet
 */
public class Task {

    private String id;
    private List<String> data;
    private String solution;
    private String text;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Task() {
    }

    public void replaceTags() {
        //data = Utils.replaceTags(data);
        List<String> data = new ArrayList<String>();
        for (String file : this.data) {
            file = file.replace("\n", "\\n");
            data.add(file);
        }
        this.data = data;
        text = text.replace("\n", "\\n");
    }
}
