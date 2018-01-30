package fabiohideki.com.tastedacity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
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

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private RecipeAdapter mAdapter;


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        progressBar.getIndeterminateDrawable().setColorFilter(getColor(R.color.primary_dark), android.graphics.PorterDuff.Mode.MULTIPLY);
        //setProgressTintList(ColorStateList.valueOf(getColor(R.color.primary_dark)));

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
            progressBar.setVisibility(View.GONE);

            if (recipeList != null) {
                setupRecycler();
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
