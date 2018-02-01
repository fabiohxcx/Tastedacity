package fabiohideki.com.tastedacity.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fabiohideki.com.tastedacity.DataContract;
import fabiohideki.com.tastedacity.R;
import fabiohideki.com.tastedacity.RecipeStepActivity;
import fabiohideki.com.tastedacity.StepFragment;
import fabiohideki.com.tastedacity.model.Step;

/**
 * Created by fabio.lagoa on 30/01/2018.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsHolder> {

    private List<Step> steps;
    private Context context;
    private String recipeName;
    private boolean twoPanel;
    private FragmentManager fragmentManager;

    public StepsAdapter(List<Step> steps, Context context, String recipeName, boolean twoPanel, FragmentManager fragmentManager) {
        this.steps = steps;
        this.context = context;
        this.recipeName = recipeName;
        this.twoPanel = twoPanel;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public StepsAdapter.StepsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StepsHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_step, parent, false));
    }

    @Override
    public void onBindViewHolder(StepsAdapter.StepsHolder holder, int position) {

        if (steps != null && !steps.isEmpty()) {
            Step step = steps.get(position);
            holder.stepShortDesc.setText(step.getShortDescription());
            if (step.getId() > 0)
                holder.stepId.setText("#" + step.getId());
        }

    }

    @Override
    public int getItemCount() {
        return steps != null ? steps.size() : 0;
    }

    public class StepsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.layout_item_step)
        LinearLayout layoutStep;

        @BindView(R.id.tv_step_id)
        TextView stepId;

        @BindView(R.id.tv_step_short_desc)
        TextView stepShortDesc;


        public StepsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            layoutStep.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Toast.makeText(context, steps.get(getAdapterPosition()).getDescription().toString(), Toast.LENGTH_SHORT).show();

            if (steps != null) {
                if (twoPanel) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(DataContract.ARG_ITEM, Parcels.wrap(steps.get(getAdapterPosition())));

                    StepFragment fragment = new StepFragment();
                    fragment.setArguments(arguments);

                    fragmentManager.beginTransaction().replace(R.id.step_container, fragment).commit();

                } else {
                    Intent intent = new Intent(context, RecipeStepActivity.class);
                    intent.putExtra("step", Parcels.wrap(steps.get(getAdapterPosition())));
                    intent.putExtra("recipeName", recipeName);
                    context.startActivity(intent);
                }
            }
        }
    }
}
