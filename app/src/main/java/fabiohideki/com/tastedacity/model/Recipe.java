package fabiohideki.com.tastedacity.model;

import android.text.TextUtils;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by hidek on 28/01/2018.
 */
@Parcel
public class Recipe {

    int id;
    String name;
    ArrayList<Ingredient> ingredients;
    ArrayList<Step> steps;
    String image;
    String servings;

    public Recipe() {
    }

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

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }
}
