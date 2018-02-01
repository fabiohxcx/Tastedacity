package fabiohideki.com.tastedacity;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import fabiohideki.com.tastedacity.model.Recipe;
import fabiohideki.com.tastedacity.repository.RecipeRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

/**
 * Created by fabio.lagoa on 01/02/2018.
 */
@RunWith(AndroidJUnit4.class)
public class PopulateScreensTest implements Callback<List<Recipe>> {

    private Context instrumentationCtx;
    RecipeRepository repository;
    List<Recipe> recipeList;


    @Before
    public void setup() {

        instrumentationCtx = InstrumentationRegistry.getContext();
        repository = new RecipeRepository(instrumentationCtx);
        repository.getRecipes(this);
    }

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkRecipeScreen() {

        try {
            Thread.sleep(9000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction textView = onView(
                first(withId(R.id.item_recipe_title))
        );

        textView.check(matches(withText(recipeList.get(0).getName())));
    }

    @Test
    public void checkRecipeDetaisScreen() {

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction constraintLayout = onView(
                allOf(withId(R.id.item_constraint_card),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recyclerview_list_recipes),
                                        0),
                                0),
                        isDisplayed()));
        constraintLayout.perform(click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(isAssignableFrom(Toolbar.class))
                .check(matches(withToolbarTitle(is(recipeList.get(0).getName()))));

    }

    private static Matcher<Object> withToolbarTitle(
            final Matcher<CharSequence> textMatcher) {
        return new BoundedMatcher<Object, Toolbar>(Toolbar.class) {
            @Override
            public boolean matchesSafely(Toolbar toolbar) {
                return textMatcher.matches(toolbar.getTitle());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with toolbar title: ");
                textMatcher.describeTo(description);
            }
        };
    }

    @Test
    public void checkIngredientScreen() {

        try {
            Thread.sleep(9000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction constraintLayout = onView(
                allOf(withId(R.id.item_constraint_card),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recyclerview_list_recipes),
                                        0),
                                0),
                        isDisplayed()));
        constraintLayout.perform(click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.bt_ingredients), withText("Recipe Ingredients"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.details_container),
                                        0),
                                0),
                        isDisplayed()));
        appCompatButton.perform(click());


        ViewInteraction textView = onView(
                first(withId(R.id.tv_ingredients))
        );

        textView.check(matches(withText(containsString(recipeList.get(0).getIngredients().get(0).getIngredient()))));


    }

    @Test
    public void checkStepScreen() {

        try {
            Thread.sleep(9000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction constraintLayout = onView(
                allOf(withId(R.id.item_constraint_card),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recyclerview_list_recipes),
                                        0),
                                0),
                        isDisplayed()));
        constraintLayout.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerview_steps),
                        childAtPosition(
                                withClassName(Matchers.is("android.support.constraint.ConstraintLayout")),
                                2)));
        recyclerView.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction textView = onView(
                first(withId(R.id.step_detail))
        );

        textView.check(matches(withText(containsString(recipeList.get(0).getSteps().get(1).getDescription()))));

    }

    @Override
    public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

        if (response.isSuccessful()) {
            recipeList = response.body();
        }

    }

    @Override
    public void onFailure(Call<List<Recipe>> call, Throwable t) {

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    private static <T> Matcher<T> first(final Matcher<T> matcher) {
        return new BaseMatcher<T>() {
            boolean isFirst = true;

            @Override
            public boolean matches(final Object item) {
                if (isFirst && matcher.matches(item)) {
                    isFirst = false;
                    return true;
                }

                return false;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("should return first matching item");
            }
        };
    }
}