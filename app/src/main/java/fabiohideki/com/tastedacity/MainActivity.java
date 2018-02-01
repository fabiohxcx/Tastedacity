package fabiohideki.com.tastedacity;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    private int mAppWidgetId;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Widget configuration
        Intent intent = getIntent();
        mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

        if (AppWidgetManager.ACTION_APPWIDGET_CONFIGURE.equals(intent.getAction())) {

            Bundle extras = intent.getExtras();
            if (extras != null) {
                mAppWidgetId = extras.getInt(
                        AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);
            }
        }

        progressBar.getIndeterminateDrawable().setColorFilter(getColor(R.color.primary_dark), android.graphics.PorterDuff.Mode.MULTIPLY);
        //setProgressTintList(ColorStateList.valueOf(getColor(R.color.primary_dark)));

        repository = new RecipeRepository(this);
        repository.getRecipes(this);

        setupRecycler();

       /* DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        Toast.makeText(this, "width: " + dpWidth, Toast.LENGTH_SHORT).show();*/

    }

    private void setupRecycler() {

        mAdapter = new RecipeAdapter(recipeList, this, mAppWidgetId);
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
