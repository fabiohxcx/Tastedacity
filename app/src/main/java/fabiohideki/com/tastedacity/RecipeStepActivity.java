package fabiohideki.com.tastedacity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import fabiohideki.com.tastedacity.model.Step;

public class RecipeStepActivity extends AppCompatActivity {

    @BindView(R.id.step_detail)
    TextView stepDetail;

    @BindView(R.id.player)
    SimpleExoPlayerView playerView;

    @BindView(R.id.thumbnail)
    ImageView imageView;

    private Step step;
    private SimpleExoPlayer mPlayer;
    private SimpleExoPlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        playerView.setVisibility(View.GONE);

        Intent originIntent = getIntent();
        step = (Step) Parcels.unwrap(originIntent.getParcelableExtra("step"));
        String recipeName = originIntent.getStringExtra("recipeName");

        if (step != null && recipeName != null) {
            if (step.getId() == 0) {
                setTitle(recipeName + " - " + step.getShortDescription());
            } else {
                setTitle(recipeName + " - Step " + step.getId());
            }
        }

        stepDetail.setText(step.getDescription());

        if (step.getVideoURL() != null && !step.getVideoURL().equals("")) {
            mPlayerView = playerView;
            playerView.setVisibility(View.VISIBLE);
            initializePlayer();
        }

        if (step.getThumbnailURL() != null && !TextUtils.isEmpty(step.getThumbnailURL())) {
            imageView.setVisibility(View.VISIBLE);
            Picasso.with(this).load(Uri.parse(step.getThumbnailURL()))
                    .into(imageView);
        }

    }

    private void initializePlayer() {
        if (mPlayer == null && mPlayerView != null) {
            // Create the player
            mPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());

            mPlayer.prepare(buildMediaSource(Uri.parse(step.getVideoURL())), false, false);

            mPlayerView.setPlayer(mPlayer);

        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        String ua = Util.getUserAgent(this, this.getApplicationInfo().name);

        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(ua)).
                createMediaSource(uri);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
