package fabiohideki.com.tastedacity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.parceler.Parcels;

import butterknife.ButterKnife;
import fabiohideki.com.tastedacity.model.Recipe;

public class RecipeDetailsActivity extends AppCompatActivity {

    private Recipe recipe;

    private boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recipe = Parcels.unwrap(getIntent().getParcelableExtra("recipe"));

        if (recipe != null) {
            setTitle(recipe.getName());
        }

        if (findViewById(R.id.step_container) != null) {
            mTwoPane = true;
        }

        Bundle arguments = new Bundle();
        arguments.putParcelable(DataContract.ARG_ITEM, getIntent().getParcelableExtra("recipe"));
        arguments.putBoolean(DataContract.ARG_BOOL, mTwoPane);
        DetailFragment fragment = new DetailFragment();

        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.details_container, fragment)
                .commit();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
