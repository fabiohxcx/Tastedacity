package fabiohideki.com.tastedacity.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import org.parceler.Parcels;

import java.util.List;

import fabiohideki.com.tastedacity.R;
import fabiohideki.com.tastedacity.model.Ingredient;
import fabiohideki.com.tastedacity.model.Recipe;

/**
 * Created by fabio.lagoa on 01/02/2018.
 */

public class IngredientsListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsListRemoteViewsFactory(getApplicationContext(), intent);
    }

    private class IngredientsListRemoteViewsFactory implements RemoteViewsFactory {

        private Context mContext;
        private List<Ingredient> mIngredients;

        public IngredientsListRemoteViewsFactory(Context applicationContext, Intent intent) {
            mContext = applicationContext;
            int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            // First time called, get and persist data
            if (intent.hasExtra(WidgetContract.WIDGET_ITEM_LIST)) {
                Bundle bundle = intent.getBundleExtra(WidgetContract.WIDGET_ITEM_LIST);
                Recipe recipe = Parcels.unwrap(bundle.getParcelable(WidgetContract.WIDGET_ITEM_LIST));

                mIngredients = recipe.getIngredients();

                WidgetDAO.saveIngredients(applicationContext, widgetId, recipe);
            } else { // Next calls, restore persisted data
                mIngredients = WidgetDAO.restoreIngredients(applicationContext, widgetId);
            }
        }


        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (mIngredients != null)
                return mIngredients.size();
            return 0;
        }

        @Override
        public RemoteViews getViewAt(int i) {

            if (mIngredients == null || mIngredients.size() == 0)
                return null;

            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.ingredient_item_widget);

            rv.setTextViewText(R.id.widget_quantity, Float.toString(mIngredients.get(i).getQuantity()));
            rv.setTextViewText(R.id.widget_measure, mIngredients.get(i).getMeasure());
            rv.setTextViewText(R.id.widget_name, mIngredients.get(i).getIngredient());

            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }

}
