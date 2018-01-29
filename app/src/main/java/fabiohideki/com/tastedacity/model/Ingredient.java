package fabiohideki.com.tastedacity.model;

import org.parceler.Parcel;

/**
 * Created by hidek on 28/01/2018.
 */

@Parcel
public class Ingredient {

    String ingredient;
    float quantity;
    String measure;

    public Ingredient() {
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }


    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }


}
