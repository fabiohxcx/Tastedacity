package fabiohideki.com.tastedacity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fabiohideki.com.tastedacity.adapter.RecipeAdapter;
import fabiohideki.com.tastedacity.model.Recipe;
import fabiohideki.com.tastedacity.repository.RecipeRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<List<Recipe>> {

    List<Recipe> recipeList;
    RecipeRepository repository;

    @BindView(R.id.recyclerview_list_recipes)
    RecyclerView mRecyclerView;

    private RecipeAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        repository = new RecipeRepository(this);
        repository.getRecipes(this);

        setupRecycler();

    }

    private void setupRecycler() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new RecipeAdapter(recipeList, this);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        repository.getRecipes(this);
    }

    @Override
    public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

        if (response.isSuccessful()) {

            //Toast.makeText(this, "response : " + response.raw().cacheResponse(), Toast.LENGTH_SHORT).show();
            recipeList = response.body();

            if (recipeList != null) {

                setupRecycler();

/*                textViewTest.setText(
                        recipeList.get(0).getName() + " \n " +
                                recipeList.get(0).getImage() + " \n " +
                                recipeList.get(0).getSteps().get(0).getDescription() + " \n " +
                                recipeList.get(0).getIngredients().get(0).getIngredient() + " \n "

                );*/
            }

        } else {
            Toast.makeText(this, "response fail: " + response.raw().cacheResponse(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onFailure(Call<List<Recipe>> call, Throwable t) {
        Toast.makeText(this, "onFailure", Toast.LENGTH_SHORT).show();
    }
}
