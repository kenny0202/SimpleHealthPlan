package logofhealth.com.kenny.dao;

/**
 * Created by Kenny on 2/16/2015.
 */
public class RecipeDAO {

    private int recipeID;
    private String title;
    private String ingredients;
    private String instructions;

    public RecipeDAO() {

    }

    public RecipeDAO(String t, String i, String in) {
        title = t;
        ingredients = i;
        instructions = in;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @Override
    public String toString() {
        return title;
    }

}
