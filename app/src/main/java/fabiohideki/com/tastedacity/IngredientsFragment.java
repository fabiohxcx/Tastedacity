package fabiohideki.com.tastedacity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fabiohideki.com.tastedacity.model.Ingredient;


/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientsFragment extends Fragment {

    @BindView(R.id.tv_ingredients)
    TextView tvIngredients;

    private List<Ingredient> ingredientList;


    public IngredientsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(DataContract.ARG_ITEM)) {

            ingredientList = (List<Ingredient>) Parcels.unwrap(getArguments().getParcelable(DataContract.ARG_ITEM));

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);

        ButterKnife.bind(this, rootView);

        if (ingredientList != null) {

            StringBuilder ingredientsBuilder = new StringBuilder();

            for (int i = 0; i < ingredientList.size(); i++) {

                ingredientsBuilder.append("<b>(" + (i + 1) + ") - " + ingredientList.get(i).getQuantity() + " " + ingredientList.get(i).getMeasure() + "</b><br> <i>" +
                        "" + ingredientList.get(i).getIngredient() + "</i><br><br>");

            }

            tvIngredients.setText(Html.fromHtml(ingredientsBuilder.toString()));

        }

        return rootView;
    }

}
