package xmlExercises;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author slaweet
 */
public class Result {
    private String userSolution;
    private String correctSolution;
    private boolean isCorrect;

    public String getCorrectSolution() {
        return correctSolution;
    }

    public void setCorrectSolution(String correctSolution) {
        this.correctSolution = correctSolution;
    }

    public boolean isIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    public String getUserSolution() {
        return userSolution;
    }

    public void setUserSolution(String userSolution) {
        this.userSolution = userSolution;
    }

    public Result() {
    }
    
    public void replaceTags() {
        userSolution = Utils.replaceTags(userSolution);
        correctSolution = Utils.replaceTags(correctSolution);
    }
}
