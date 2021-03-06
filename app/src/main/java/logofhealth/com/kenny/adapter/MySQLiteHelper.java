package logofhealth.com.kenny.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import logofhealth.com.kenny.dao.ExerciseDAO;
import logofhealth.com.kenny.dao.RecipeDAO;

/**
 * Created by Kenny on 2/16/2015.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "HealthDB";

    //columns for exercise
    private static final String TABLE_EXERCISE = "exercise";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String[] COLUMNS = {KEY_ID, KEY_TITLE};

    //columns for recipe
    private static final String TABLE_RECIPE = "recipe";
    private static final String KEY_RID = "id";
    private static final String KEY_RTITLE = "title";
    private static final String KEY_RINGREDIENTS = "ingredients";
    private static final String KEY_RINSTRUCTIONS = "instructions";
    private static final String[] RCOLUMNS = {KEY_RID, KEY_RTITLE, KEY_RINGREDIENTS, KEY_RINSTRUCTIONS};

    //columns for mealplan
    private static final String TABLE_MEALPLAN = "mealplan";
    private static final String KEY_MID = "id";
    private static final String KEY_MTITLE = "title";

    private static final String KEY_MINGREDIENTS = "ingredients";
    private static final String[] MCOLUMNS = {KEY_MID, KEY_MTITLE, KEY_MINGREDIENTS};

    //columns for program
    private static final String TABLE_PROGRAM = "program";
    private static final String KEY_PID = "id";
    private static final String KEY_PTITLE = "title";
    private static final String[] PCOLUMNS = {KEY_PID, KEY_PTITLE};

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_RECIPE_TABLE = "CREATE TABLE recipe ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "ingredients TEXT, " +
                "instructions TEXT, " +
                " UNIQUE (title))";

        String CREATE_EXERCISE_TABLE = "CREATE TABLE exercise ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                " UNIQUE (title))";

        String CREATE_MEALPLAN_TABLE = "CREATE TABLE mealplan ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "ingredients TEXT)";

        String CREATE_PROGRAM_TABLE = "CREATE TABLE program ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "description TEXT)";

        db.execSQL(CREATE_RECIPE_TABLE);
        db.execSQL(CREATE_EXERCISE_TABLE);
        db.execSQL(CREATE_MEALPLAN_TABLE);
        db.execSQL(CREATE_PROGRAM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS recipe");
        db.execSQL("DROP TABLE IF EXISTS exercise");
        db.execSQL("DROP TABLE IF EXISTS mealplan");
        db.execSQL("DROP TABLE IF EXISTS program");
        // create fresh books table
        this.onCreate(db);
    }

    //START OF CRUD OPERATION FOR EXERCISE
    public void addExercise(ExerciseDAO e) {
        Log.d("addExercise", e.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, e.getTitle());
        db.insert(TABLE_EXERCISE, null, values);
        db.close();
    }

    public ExerciseDAO getExercise(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE_EXERCISE, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[]{String.valueOf(id)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null);
        if (cursor != null)
            cursor.moveToFirst();

        ExerciseDAO e = new ExerciseDAO();
        e.setExerciseID(Integer.parseInt(cursor.getString(0)));
        e.setTitle(cursor.getString(1));
        db.close();
        return e;
    }

    public List<ExerciseDAO> getAllExercise() {
        List<ExerciseDAO> exercise = new ArrayList<ExerciseDAO>();

        String query = "SELECT  * FROM " + TABLE_EXERCISE + " ORDER BY " + KEY_TITLE + " ASC ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ExerciseDAO e = null;
        if (cursor.moveToFirst()) {
            do {
                e = new ExerciseDAO();
                e.setExerciseID(Integer.parseInt(cursor.getString(0)));
                e.setTitle(cursor.getString(1));
                exercise.add(e);
            } while (cursor.moveToNext());
        }
        db.close();
        return exercise;
    }

    public int updateExercise(ExerciseDAO e) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", e.getTitle());

        int i = db.update(TABLE_EXERCISE, //table
                values, // column/value
                KEY_ID + " = ?", // selections
                new String[]{String.valueOf(e.getExerciseID())});
        db.close();
        return i;
    }

    public void deleteAllExercise() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXERCISE, null, null);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_EXERCISE + "'");
        db.close();
    }

    public void deleteExercise(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXERCISE, KEY_ID + " = ?", new String[]{Integer.toString(id)});
        db.close();
    }
    //END OF CRUD FOR EXERCISE

    //START OF CRUD FOR RECIPE
    public void addRecipe(RecipeDAO r) {
        Log.d("addRecipe", r.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_RTITLE, r.getTitle());
        values.put(KEY_RINGREDIENTS, r.getIngredients());
        values.put(KEY_RINSTRUCTIONS, r.getInstructions());
        db.insert(TABLE_RECIPE, null, values);
        Log.d("Data Added", r.toString());
        db.close();
    }

    public RecipeDAO getRecipe(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE_RECIPE, // a. table
                        RCOLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[]{String.valueOf(id)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null);
        if (cursor != null)
            cursor.moveToFirst();

        RecipeDAO r = new RecipeDAO();
        r.setRecipeID(Integer.parseInt(cursor.getString(0)));
        r.setTitle(cursor.getString(1));
        r.setIngredients(cursor.getString(2));
        r.setInstructions(cursor.getString(3));
        db.close();
        return r;
    }

    public List<RecipeDAO> getAllRecipe() {
        List<RecipeDAO> recipe = new ArrayList<RecipeDAO>();

        String query = "SELECT  * FROM " + TABLE_RECIPE + " ORDER BY " + KEY_RTITLE + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        RecipeDAO r = null;
        if (cursor.moveToFirst()) {
            do {
                r = new RecipeDAO();
                r.setRecipeID(Integer.parseInt(cursor.getString(0)));
                r.setTitle(cursor.getString(1));
                r.setIngredients(cursor.getString(2));
                r.setInstructions(cursor.getString(3));

                recipe.add(r);
            } while (cursor.moveToNext());
        }
        db.close();
        return recipe;
    }

    public void updateRecipe(RecipeDAO r) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("title", r.getTitle());
        values.put("ingredients", r.getIngredients());
        values.put("instructions", r.getInstructions());

        db.update(TABLE_RECIPE, //table
                values, // column/value
                KEY_RID + " = ?", // selections
                new String[]{String.valueOf(r.getRecipeID())});

        db.close();
    }

    public void deleteAllRecipe() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECIPE, null, null);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_RECIPE + "'");

        db.close();
    }

    public void deleteRecipe(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECIPE, KEY_RID + " = ?", new String[]{Integer.toString(id)});
        db.close();
    }
    //END OF CRUD FOR RECIPE

    //BEGIN CRUD FOR MEALPLAN
    public void addMeal(RecipeDAO r) {
        Log.d("addRecipe", r.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_MTITLE, r.getTitle());
        values.put(KEY_MINGREDIENTS, r.getIngredients());
        db.insert(TABLE_MEALPLAN, null, values);
        Log.d("Data Added", r.toString());
        db.close();
    }

    public RecipeDAO getMeal(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE_MEALPLAN, // a. table
                        MCOLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[]{String.valueOf(id)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null);
        if (cursor != null)
            cursor.moveToFirst();

        RecipeDAO r = new RecipeDAO();
        r.setRecipeID(Integer.parseInt(cursor.getString(0)));
        r.setTitle(cursor.getString(1));
        r.setIngredients(cursor.getString(2));
        db.close();
        return r;
    }

    public List<RecipeDAO> getAllMeal() {
        List<RecipeDAO> recipe = new ArrayList<RecipeDAO>();

        String query = "SELECT  * FROM " + TABLE_MEALPLAN;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        RecipeDAO r = null;
        if (cursor.moveToFirst()) {
            do {
                r = new RecipeDAO();
                r.setRecipeID(Integer.parseInt(cursor.getString(0)));
                r.setTitle(cursor.getString(1));
                r.setIngredients(cursor.getString(2));

                recipe.add(r);
            } while (cursor.moveToNext());
        }
        db.close();
        return recipe;
    }

    public void updateMealList(List<RecipeDAO> r) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEALPLAN);
        String CREATE_MEALPLAN_TABLE = "CREATE TABLE mealplan ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "description TEXT)";
        db.execSQL(CREATE_MEALPLAN_TABLE);

        for (int i = 0; i < r.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(KEY_MTITLE, r.get(i).getTitle());
            db.insert(TABLE_MEALPLAN, null, values);
        }
        db.close();
    }

    public void deleteAllMeal() {
        RecipeDAO r = new RecipeDAO();
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEALPLAN, null, null);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_MEALPLAN + "'");
        db.close();
    }

    public void deleteMeal(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEALPLAN, KEY_MID + " = ?", new String[]{Integer.toString(id)});
        db.close();
    }
    //END CRUD FOR MEALPLAN

    //BEGIN CRUD PROGRAM
    public void addProgram(ExerciseDAO r) {
        Log.d("addRecipe", r.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PTITLE, r.getTitle());
        db.insert(TABLE_PROGRAM, null, values);
        db.close();
        db.close();
    }

    public ExerciseDAO getProgram(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE_PROGRAM, // a. table
                        PCOLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[]{String.valueOf(id)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null);
        if (cursor != null)
            cursor.moveToFirst();

        ExerciseDAO e = new ExerciseDAO();
        e.setExerciseID(Integer.parseInt(cursor.getString(0)));
        e.setTitle(cursor.getString(1));
        db.close();
        return e;
    }

    public List<ExerciseDAO> getAllProgram() {
        List<ExerciseDAO> exercise = new ArrayList<ExerciseDAO>();

        String query = "SELECT  * FROM " + TABLE_PROGRAM;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ExerciseDAO r = null;
        if (cursor.moveToFirst()) {
            do {
                r = new ExerciseDAO();
                r.setExerciseID(Integer.parseInt(cursor.getString(0)));
                r.setTitle(cursor.getString(1));
                exercise.add(r);
            } while (cursor.moveToNext());
        }
        db.close();
        return exercise;
    }

    public void updateProgram(List<ExerciseDAO> r) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRAM);
        String CREATE_PROGRAM_TABLE = "CREATE TABLE program ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT)";
        db.execSQL(CREATE_PROGRAM_TABLE);

        for (int i = 0; i < r.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(KEY_PTITLE, r.get(i).getTitle());
            db.insert(TABLE_PROGRAM, null, values);
        }
        db.close();
    }

    public void deleteAllProgram() {
        ExerciseDAO r = new ExerciseDAO();
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROGRAM, null, null);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + TABLE_PROGRAM + "'");
        db.close();
    }

    public void deleteProgram(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROGRAM, KEY_PID + " = ?", new String[]{Integer.toString(id)});
        db.close();
    }
    //END CRUD FOR PROGRAM
}