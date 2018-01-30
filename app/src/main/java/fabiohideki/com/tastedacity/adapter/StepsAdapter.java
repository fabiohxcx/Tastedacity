package fabiohideki.com.tastedacity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fabiohideki.com.tastedacity.R;
import fabiohideki.com.tastedacity.model.Step;

/**
 * Created by fabio.lagoa on 30/01/2018.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsHolder> {

    private List<Step> steps;
    private Context context;

    public StepsAdapter(List<Step> steps, Context context) {
        this.steps = steps;
        this.context = context;
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
            Toast.makeText(context, steps.get(getAdapterPosition()).getDescription().toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
