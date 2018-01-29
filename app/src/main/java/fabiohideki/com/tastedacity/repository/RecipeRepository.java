package fabiohideki.com.tastedacity.repository;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.List;

import fabiohideki.com.tastedacity.BakingService;
import fabiohideki.com.tastedacity.Utils;
import fabiohideki.com.tastedacity.model.Recipe;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hidek on 28/01/2018.
 */

public class RecipeRepository {

    private Context context;

    //http://go.udacity.com/android-baking-app-json
    private String API_BASE_URL = "http://go.udacity.com";

    private List<Recipe> recipeList = null;


    public RecipeRepository(Context context) {

        this.context = context;

    }

    public void getRecipes(Callback<List<Recipe>> callBack) {

        File httpCacheDirectory = new File(context.getCacheDir(), "httpCache");
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(OFFLINE_INTERCEPTOR)
                .addNetworkInterceptor(ONLINE_INTERCEPTOR);

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit =
                builder.client(
                        httpClient.build()
                ).build();

        BakingService client = retrofit.create(BakingService.class);

        Call<List<Recipe>> call = client.tastedacityRecipes();

        call.enqueue(callBack);

    }


    Interceptor OFFLINE_INTERCEPTOR = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!Utils.isNetworkAvailable(context)) {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                request = request.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }

            return chain.proceed(request);
        }
    };


    Interceptor ONLINE_INTERCEPTOR = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            okhttp3.Response response = chain.proceed(chain.request());
            int maxAge = 60; // read from cache
            return response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        }
    };


}
