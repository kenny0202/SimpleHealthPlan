package logofhealth.com.kenny.dao;

/**
 * Created by Kenny on 2/16/2015.
 */
public class ExerciseDAO {

    private int exerciseID;
    private String title;

    public ExerciseDAO() {

    }

    public ExerciseDAO(String t) {
        title = t;
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

    @Override
    public String toString() {
        return title;
    }
}
