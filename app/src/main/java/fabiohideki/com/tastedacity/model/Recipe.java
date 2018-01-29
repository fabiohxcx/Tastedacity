package fabiohideki.com.tastedacity.model;

import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by hidek on 28/01/2018.
 */

public class Recipe {

    private int id;
    private String name;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Step> steps;
    private String image;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!TextUtils.isEmpty(name)) {
            this.name = name;
        } else
            this.name = "Some Recipe";
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {

        if (ingredients != null && !ingredients.isEmpty())
            this.ingredients = ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        if (steps != null && !steps.isEmpty())
            this.steps = steps;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        if (!TextUtils.isEmpty(image))
            this.image = image;

    }
}
