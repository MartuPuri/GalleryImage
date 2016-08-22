package masacre.galleryimage.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import masacre.galleryimage.GalleryAdapter;
import masacre.galleryimage.R;
import masacre.galleryimage.activities.GalleryActivity;
import masacre.galleryimage.interfaces.OnAlbumClickListener;
import masacre.galleryimage.interfaces.OnGalleryItemAdded;
import masacre.galleryimage.interfaces.OnPhotoClickListener;
import masacre.galleryimage.model.GalleryAlbum;
import masacre.galleryimage.model.GalleryItem;
import masacre.galleryimage.utils.GalleryUtils;
import masacre.galleryimage.utils.SpacesItemDecoration;

public class GalleryFragment extends Fragment implements OnGalleryItemAdded, OnPhotoClickListener {
    private static final String GALLERY_ITEMS = "GALLERY_ITEMS";
    private static final String GALLERY_ALBUM = "GALLERY_ALBUM";
    public static final int MAX_COLUMN = 3;

    private RecyclerView recyclerView;
    private GalleryAdapter galleryAdapter;

    private List<GalleryItem> galleryItems;
    private GalleryAlbum galleryAlbum;
    private ImageView expandedImageView;
    private Animator mCurrentAnimator;
    private View view;

    public static GalleryFragment newInstance(ArrayList<GalleryItem> galleryItems) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList(GALLERY_ITEMS, galleryItems);
        fragment.setArguments(arguments);
        return fragment;
    }

    public static GalleryFragment newInstance(GalleryAlbum galleryAlbum) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(GALLERY_ALBUM, galleryAlbum);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.gallery_fragment, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            galleryItems = arguments.getParcelableArrayList(GALLERY_ITEMS);
            if (galleryItems == null) {
                galleryItems = new ArrayList<>();
            }
            galleryAlbum = arguments.getParcelable(GALLERY_ALBUM);
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        expandedImageView = (ImageView) view.findViewById(R.id.expanded_image);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), MAX_COLUMN));
        recyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.medium_padding)));
        galleryAdapter = new GalleryAdapter(galleryItems, getAlbumClickListener(), (GalleryActivity) getActivity(), this, galleryAlbum);
        recyclerView.setAdapter(galleryAdapter);
        if (galleryAlbum != null && galleryItems.isEmpty()) {
            GalleryUtils.retrieveFileFromDirectory(galleryAlbum.getFile(), this);
        }

        return view;
    }

    private OnAlbumClickListener getAlbumClickListener() {
        return new OnAlbumClickListener() {
            @Override
            public void onAlbumClick(View view, GalleryAlbum galleryAlbum) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Fragment fragment = GalleryFragment.newInstance(galleryAlbum);

                transaction.add(R.id.main_content, fragment, fragment.getClass().getName());
                transaction.addToBackStack(fragment.getClass().getName());
                transaction.commitAllowingStateLoss();
            }
        };
    }

    @Override
    public void onGalleryItemAdded(GalleryItem galleryItem) {
        galleryAdapter.addGalleryItem(galleryItem);
    }

    private void zoomImageFromThumb(final View thumbView, Bitmap bitmap) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        expandedImageView.setImageBitmap(bitmap);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        view.findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(300);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(100);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }

    @Override
    public void onPhotoClickListener(ImageView photo, Bitmap bitmap) {
        zoomImageFromThumb(photo, bitmap);
    }
}
