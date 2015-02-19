package logofhealth.com.logofhealth.dao;

/**
 * Created by Kenny on 2/16/2015.
 */
public class ExerciseDAO {

    private int exerciseID;
    private String title;
    private String description;

    public ExerciseDAO() {

    }

    public ExerciseDAO(String t) {
        title = t;
    }

    public ExerciseDAO(String t, String d) {
        title = t;
        description = d;
    }

    public int getExerciseID() {
        return exerciseID;
    }

    public void setExerciseID(int exerciseID) {
        this.exerciseID = exerciseID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return title;
    }
}
