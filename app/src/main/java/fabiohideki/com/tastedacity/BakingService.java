package fabiohideki.com.tastedacity;

import java.util.List;

import fabiohideki.com.tastedacity.model.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by hidek on 28/01/2018.
 */

public interface BakingService {

    @GET("android-baking-app-json")
    Call<List<Recipe>> tastedacityRecipes();
}
