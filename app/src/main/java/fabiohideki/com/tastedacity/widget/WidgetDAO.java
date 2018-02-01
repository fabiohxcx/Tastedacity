package fabiohideki.com.tastedacity.widget;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import fabiohideki.com.tastedacity.model.Ingredient;
import fabiohideki.com.tastedacity.model.Recipe;

/**
 * Created by fabio.lagoa on 01/02/2018.
 */

public class WidgetDAO {

    private static final String WIDGET_SP_NAME = "widgets.info";

    private static final String RECIPE_NAME_SUFFIX = ":R";
    private static final String INGREDIENTS_SUFFIX = ":I";

    private static final String SEPARATOR_ITEM = "#::#";
    private static final String SEPARATOR_VALUE = "&::&";

    // Persist data as Shared preference for each widget
    public static void saveIngredients(Context context, int widgetId, Recipe recipe) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(WIDGET_SP_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor sharedEditor = sharedPreferences.edit();

        StringBuilder sb = new StringBuilder();

        // Persist as string, using separator for each item and for each ingredient field
        for (Ingredient ingredient : recipe.getIngredients()) {
            sb.append(formatToString(ingredient));
            sb.append(SEPARATOR_ITEM);
        }

        sharedEditor.putString(widgetId + RECIPE_NAME_SUFFIX, recipe.getName());
        sharedEditor.putString(widgetId + INGREDIENTS_SUFFIX, sb.toString());

        sharedEditor.apply();

    }

    // Restore data from Shared preference
    public static List<Ingredient> restoreIngredients(Context context, int widgetId) {
        ArrayList<Ingredient> ingredients = null;

        SharedPreferences sharedPreferences =
                context.getSharedPreferences(WIDGET_SP_NAME, Context.MODE_PRIVATE);

        String savedIngredients = sharedPreferences.getString(widgetId + INGREDIENTS_SUFFIX, null);

        // Split and convert to object
        if (savedIngredients != null) {
            String[] sIngredients = savedIngredients.split(SEPARATOR_ITEM);

            ingredients = new ArrayList<>(sIngredients.length);

            for (String sIngredient : sIngredients) {
                ingredients.add(getFromString(sIngredient));
            }
        }

        return ingredients != null ? ingredients : null;

    }

    public static String restoreRecipeName(Context context, int widgetId) {

        SharedPreferences sharedPreferences =
                context.getSharedPreferences(WIDGET_SP_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getString(widgetId + RECIPE_NAME_SUFFIX, null);

    }

    // Convert Ingredient object to String with separator
    private static String formatToString(Ingredient ingredient) {
        return ingredient.getQuantity()
                + SEPARATOR_VALUE
                + ingredient.getMeasure()
                + SEPARATOR_VALUE
                + ingredient.getIngredient();
    }

    // Convert String to Ingredient object
    private static Ingredient getFromString(String ingredientStr) {
        String[] sValues = ingredientStr.split(SEPARATOR_VALUE);

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setIngredient(sValues[2]);
        ingredient1.setMeasure(sValues[1]);
        ingredient1.setQuantity(Float.parseFloat(sValues[0]));

        return ingredient1;
    }

}
