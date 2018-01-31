package fabiohideki.com.tastedacity;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.net.URLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import fabiohideki.com.tastedacity.model.Step;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepFragment extends Fragment {

    @BindView(R.id.step_detail)
    TextView stepDetail;

    @BindView(R.id.thumbnail)
    ImageView imageView;

    @BindView(R.id.player)
    SimpleExoPlayerView playerView;

    private Step step;
    private SimpleExoPlayer mPlayer;
    private SimpleExoPlayerView mPlayerView;

    private static final String SIS_PLAYER_POSITION = "current_position";
    private static final String SIS_PLAYER_WINDOW = "current_window";
    private static final String SIS_PLAYER_PLAYING = "is_playing";

    private long mPlaybackPosition;
    private int mCurrentWindow;
    private boolean mIsPlaying;


    public StepFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(DataContract.ARG_ITEM)) {

            step = (Step) Parcels.unwrap(getArguments().getParcelable(DataContract.ARG_ITEM));

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, rootView);

        playerView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);

        stepDetail.setText(step.getDescription());

        if (step.getVideoURL() != null && !step.getVideoURL().equals("")) {
            mPlayerView = playerView;
            playerView.setVisibility(View.VISIBLE);
            initializePlayer();
        }

        if (step.getThumbnailURL() != null && !TextUtils.isEmpty(step.getThumbnailURL()) && isImageFile(step.getThumbnailURL())) {
            imageView.setVisibility(View.VISIBLE);
            Picasso.with(getContext()).load(Uri.parse(step.getThumbnailURL()))
                    .into(imageView);
        }

        return rootView;
    }

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    private void initializePlayer() {
        if (mPlayer == null && mPlayerView != null) {
            // Create the player
            mPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), new DefaultTrackSelector());

            mPlayer.seekTo(mCurrentWindow, mPlaybackPosition);
            mPlayer.setPlayWhenReady(mIsPlaying);

            mPlayer.prepare(buildMediaSource(Uri.parse(step.getVideoURL())), mPlaybackPosition > 0, false);

            mPlayerView.setPlayer(mPlayer);

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
        String ua = Util.getUserAgent(getContext(), getContext().getApplicationInfo().name);

        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(ua)).
                createMediaSource(uri);
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
