package fabiohideki.com.tastedacity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.parceler.Parcels;

import fabiohideki.com.tastedacity.model.Step;

public class RecipeStepActivity extends AppCompatActivity {

    private Step step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent originIntent = getIntent();
        step = (Step) Parcels.unwrap(originIntent.getParcelableExtra("step"));
        String recipeName = originIntent.getStringExtra("recipeName");


        if (step != null && recipeName != null) {
            if (step.getId() == 0) {
                setTitle(recipeName + " - " + step.getShortDescription());
            } else {
                setTitle(recipeName + " - Step " + step.getId());
            }

        }

        if (savedInstanceState == null) {

            Bundle arguments = new Bundle();
            arguments.putParcelable(DataContract.ARG_ITEM, getIntent().getParcelableExtra("step"));
            StepFragment fragment = new StepFragment();

            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
