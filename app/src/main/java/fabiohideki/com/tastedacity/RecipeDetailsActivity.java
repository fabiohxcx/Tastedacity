package fabiohideki.com.tastedacity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fabiohideki.com.tastedacity.adapter.StepsAdapter;
import fabiohideki.com.tastedacity.model.Ingredient;
import fabiohideki.com.tastedacity.model.Recipe;
import fabiohideki.com.tastedacity.model.Step;

public class RecipeDetailsActivity extends AppCompatActivity {

    @BindView(R.id.bt_ingredients)
    Button btIngredients;

    @BindView(R.id.recyclerview_steps)
    RecyclerView mRecyclerView;


    private Recipe recipe;
    private List<Step> steps;
    private List<Ingredient> ingredients;
    private StepsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recipe = (Recipe) Parcels.unwrap(getIntent().getParcelableExtra("recipe"));

        if (recipe != null) {
            setTitle(recipe.getName());

            steps = recipe.getSteps();
            ingredients = recipe.getIngredients();
        }

        setupRecycler();

    }

    private void setupRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getDrawable(R.drawable.line));

        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mAdapter = new StepsAdapter(steps, this, recipe.getName());
        mRecyclerView.setAdapter(mAdapter);
    }


    @OnClick(R.id.bt_ingredients)
    public void callIngredientsScreen() {
        //Toast.makeText(this, ingredients.get(0).getIngredient(), Toast.LENGTH_SHORT).show();

        if (ingredients != null) {
            Intent intent = new Intent(this, RecipeIngredientsActivity.class);
            intent.putExtra("ingredients", Parcels.wrap(ingredients));
            intent.putExtra("recipeName", recipe.getName());
            startActivity(intent);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
