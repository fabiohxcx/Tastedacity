package fabiohideki.com.tastedacity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.parceler.Parcels;

import fabiohideki.com.tastedacity.model.Recipe;

public class RecipeDetailsActivity extends AppCompatActivity {

    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recipe = Parcels.unwrap(getIntent().getParcelableExtra("recipe"));

        if (recipe != null) {
            setTitle(recipe.getName());
        }

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable(DataContract.ARG_ITEM, getIntent().getParcelableExtra("recipe"));
            DetailFragment fragment = new DetailFragment();

            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.details_container, fragment)
                    .commit();
        }


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
