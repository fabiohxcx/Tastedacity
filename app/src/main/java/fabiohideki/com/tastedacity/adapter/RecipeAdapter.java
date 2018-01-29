package fabiohideki.com.tastedacity.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fabiohideki.com.tastedacity.R;
import fabiohideki.com.tastedacity.RecipeDetailsActivity;
import fabiohideki.com.tastedacity.model.Recipe;

/**
 * Created by fabio.lagoa on 29/01/2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {

    private List<Recipe> recipes;
    private Context context;
    private Toast toast;


    public RecipeAdapter(List<Recipe> recipes, Context context) {
        this.recipes = recipes;
        this.context = context;
    }

    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new RecipeHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(RecipeHolder holder, int position) {

        if (recipes != null && recipes.size() > 0) {
            holder.id.setText("#" + Integer.toString(recipes.get(position).getId()));
            holder.title.setText(recipes.get(position).getName());
            holder.servings.append(recipes.get(position).getServings());
        }

    }

    @Override
    public int getItemCount() {
        return recipes != null ? recipes.size() : 0;
    }


    public class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.item_constraint_card)
        ConstraintLayout constraintLayoutItem;

        @BindView(R.id.item_recipe_id)
        TextView id;

        @BindView(R.id.item_recipe_title)
        TextView title;

        @BindView(R.id.item_recipe_servings)
        TextView servings;

        @BindView(R.id.item_recipe_imageview)
        ImageView image;

        public RecipeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            constraintLayoutItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (toast != null) {
                toast.cancel();
            }

            toast = Toast.makeText(context, recipes.get(getAdapterPosition()).getName(), Toast.LENGTH_SHORT);
            toast.show();

            Intent intent = new Intent(context, RecipeDetailsActivity.class);
            intent.putExtra("recipe", Parcels.wrap(recipes.get(getAdapterPosition())));
            context.startActivity(intent);

        }
    }

}
