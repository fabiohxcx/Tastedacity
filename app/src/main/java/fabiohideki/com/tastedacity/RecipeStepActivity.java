package fabiohideki.com.tastedacity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.net.URLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import fabiohideki.com.tastedacity.model.Step;

public class RecipeStepActivity extends AppCompatActivity {

    @BindView(R.id.step_detail)
    TextView stepDetail;

    @BindView(R.id.thumbnail)
    ImageView imageView;

    private Step step;
    private SimpleExoPlayer mPlayer;
    private SimpleExoPlayerView mPlayerView;

    private static final String SIS_PLAYER_POSITION = "current_position";
    private static final String SIS_PLAYER_WINDOW = "current_window";
    private static final String SIS_PLAYER_PLAYING = "is_playing";

    private long mPlaybackPosition;
    private int mCurrentWindow;
    private boolean mIsPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        SimpleExoPlayerView playerView = findViewById(R.id.player);

        playerView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);

        Intent originIntent = getIntent();
        step = (Step) Parcels.unwrap(originIntent.getParcelableExtra("step"));
        String recipeName = originIntent.getStringExtra("recipeName");


        if (step != null && recipeName != null) {
            if (step.getId() == 0) {
                setTitle(recipeName + " - " + step.getShortDescription());
            } else {
                setTitle(recipeName + " - Step " + step.getId());
            }


            stepDetail.setText(step.getDescription());

            if (step.getVideoURL() != null && !step.getVideoURL().equals("")) {
                mPlayerView = playerView;
                playerView.setVisibility(View.VISIBLE);
                initializePlayer();
            }

            if (step.getThumbnailURL() != null && !TextUtils.isEmpty(step.getThumbnailURL()) && isImageFile(step.getThumbnailURL())) {
                imageView.setVisibility(View.VISIBLE);
                Picasso.with(this).load(Uri.parse(step.getThumbnailURL()))
                        .into(imageView);
            }

        }

        if (savedInstanceState != null) {

            if (savedInstanceState.containsKey(SIS_PLAYER_POSITION)) {
                mPlaybackPosition = savedInstanceState.getLong(SIS_PLAYER_POSITION);
            }

            if (savedInstanceState.containsKey(SIS_PLAYER_WINDOW)) {
                mCurrentWindow = savedInstanceState.getInt(SIS_PLAYER_WINDOW);
            }

            if (savedInstanceState.containsKey(SIS_PLAYER_PLAYING)) {
                mIsPlaying = savedInstanceState.getBoolean(SIS_PLAYER_PLAYING);
            }
        }

    }

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    private void initializePlayer() {
        if (mPlayer == null && mPlayerView != null) {
            // Create the player


            Uri videoUri = Uri.parse(step.getVideoURL());
            // 1. Create a default TrackSelector

            Handler mainHandler = new Handler();
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);

            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            mPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

            mPlayer.seekTo(mCurrentWindow, mPlaybackPosition);
            mPlayer.setPlayWhenReady(mIsPlaying);

            mPlayerView.setPlayer(mPlayer);

            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                    Util.getUserAgent(this, BuildConfig.APPLICATION_ID), null);
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(videoUri);
            // Prepare the mExoPlayer with the source.
            mPlayer.prepare(videoSource);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPlayer != null) {
            outState.putLong(SIS_PLAYER_POSITION, mPlayer.getCurrentPosition());
            outState.putInt(SIS_PLAYER_WINDOW, mPlayer.getCurrentWindowIndex());
            outState.putBoolean(SIS_PLAYER_PLAYING, mPlayer.getPlayWhenReady());
        }
        super.onSaveInstanceState(outState);
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlaybackPosition = mPlayer.getCurrentPosition();
            mCurrentWindow = mPlayer.getCurrentWindowIndex();
            mIsPlaying = mPlayer.getPlayWhenReady();
            mPlayer.release();
            mPlayer = null;
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

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mPlayer == null) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}
