package fabiohideki.com.tastedacity.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import org.parceler.Parcels;

import fabiohideki.com.tastedacity.R;
import fabiohideki.com.tastedacity.model.Recipe;

/**
 * Created by fabio.lagoa on 01/02/2018.
 */

public class IngredientsListWidgetProvider extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId, Recipe item) {

        // Instantiate the RemoteViews object for the app widget layout.
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.ingredients_list_widget);

        // Set ingredients list
        Intent intent = new Intent(context, IngredientsListWidgetService.class);

        // Set recipe title
        rv.setTextViewText(R.id.appwidget_name,
                item != null
                        ? item.getName()
                        : WidgetDAO.restoreRecipeName(context, appWidgetId));

        // Ingredients values
        if (item != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(WidgetContract.WIDGET_ITEM_LIST, Parcels.wrap(item));
            intent.putExtra(WidgetContract.WIDGET_ITEM_LIST, bundle);
        }

        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        rv.setRemoteAdapter(R.id.appwidget_list, intent);

        // The empty view is displayed when the collection has no items.
        rv.setEmptyView(R.id.appwidget_list, R.id.empty_view);

        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, null);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}
