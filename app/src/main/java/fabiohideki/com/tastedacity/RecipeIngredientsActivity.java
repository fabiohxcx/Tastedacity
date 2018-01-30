package fabiohideki.com.tastedacity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fabiohideki.com.tastedacity.model.Ingredient;

public class RecipeIngredientsActivity extends AppCompatActivity {

    @BindView(R.id.tv_ingredients)
    TextView tvIngredients;

    private List<Ingredient> ingredientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_ingredients);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);


        Intent originIntent = getIntent();
        ingredientList = (List<Ingredient>) Parcels.unwrap(originIntent.getParcelableExtra("ingredients"));

        String recipeName = originIntent.getStringExtra("recipeName");

        if (recipeName != null) {
            setTitle(recipeName);
        }

        StringBuilder ingredientsBuilder = new StringBuilder();

        for (int i = 0; i < ingredientList.size(); i++) {

            ingredientsBuilder.append("<b>(" + (i + 1) + ") - " + ingredientList.get(i).getQuantity() + " " + ingredientList.get(i).getMeasure() + "</b><br> <i>" +
                    "" + ingredientList.get(i).getIngredient() + "</i><br><br>");

        }

        tvIngredients.setText(Html.fromHtml(ingredientsBuilder.toString()));

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
