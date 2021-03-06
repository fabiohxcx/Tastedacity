package fabiohideki.com.tastedacity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class DetailFragment extends Fragment {

    @BindView(R.id.bt_ingredients)
    Button btIngredients;

    @BindView(R.id.recyclerview_steps)
    RecyclerView mRecyclerView;

    private Recipe recipe;

    boolean twoPanel;

    private List<Step> steps;
    private List<Ingredient> ingredients;

    private Context context = getContext();

    private View rootView;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(DataContract.ARG_ITEM)) {

            recipe = Parcels.unwrap(getArguments().getParcelable(DataContract.ARG_ITEM));

            twoPanel = getArguments().getBoolean(DataContract.ARG_BOOL);

            if (recipe != null) {

                steps = recipe.getSteps();
                ingredients = recipe.getIngredients();
            }


        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        ButterKnife.bind(this, rootView);

        if (recipe != null && steps != null && ingredients != null)
            setupRecycler();

        return rootView;
    }

    private void setupRecycler() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);

        mRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getActivity().getDrawable(R.drawable.line));

        mRecyclerView.addItemDecoration(dividerItemDecoration);
        StepsAdapter mAdapter = new StepsAdapter(steps, getActivity(), recipe.getName(), twoPanel, getFragmentManager());

        mRecyclerView.setAdapter(mAdapter);
    }

    @OnClick(R.id.bt_ingredients)
    public void callIngredientsScreen() {
        //Toast.makeText(this, ingredients.get(0).getIngredient(), Toast.LENGTH_SHORT).show();

        if (ingredients != null) {

            if (twoPanel) {
                Bundle arguments = new Bundle();
                arguments.putParcelable(DataContract.ARG_ITEM, Parcels.wrap(ingredients));
                IngredientsFragment fragment = new IngredientsFragment();

                fragment.setArguments(arguments);
                getFragmentManager().beginTransaction()
                        .replace(R.id.step_container, fragment)
                        .commit();

            } else {

                Intent intent = new Intent(getActivity(), RecipeIngredientsActivity.class);
                intent.putExtra("ingredients", Parcels.wrap(ingredients));
                intent.putExtra("recipeName", recipe.getName());
                startActivity(intent);
            }
        }

    }


}
