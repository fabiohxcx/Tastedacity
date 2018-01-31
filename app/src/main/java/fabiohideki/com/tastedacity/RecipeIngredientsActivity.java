package fabiohideki.com.tastedacity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.parceler.Parcels;

import java.util.List;

import fabiohideki.com.tastedacity.model.Ingredient;

public class RecipeIngredientsActivity extends AppCompatActivity {

    private List<Ingredient> ingredientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_ingredients);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent originIntent = getIntent();
        ingredientList = (List<Ingredient>) Parcels.unwrap(originIntent.getParcelableExtra("ingredients"));

        String recipeName = originIntent.getStringExtra("recipeName");

        if (recipeName != null) {
            setTitle(recipeName);
        }


        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(DataContract.ARG_ITEM, getIntent().getParcelableExtra("ingredients"));
            IngredientsFragment fragment = new IngredientsFragment();

            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.ingredients_container, fragment)
                    .commit();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
