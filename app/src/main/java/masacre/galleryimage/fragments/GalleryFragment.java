package masacre.galleryimage.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import masacre.galleryimage.GalleryAdapter;
import masacre.galleryimage.R;
import masacre.galleryimage.activities.GalleryActivity;
import masacre.galleryimage.interfaces.OnAlbumClickListener;
import masacre.galleryimage.interfaces.OnGalleryItemAdded;
import masacre.galleryimage.model.GalleryAlbum;
import masacre.galleryimage.model.GalleryItem;
import masacre.galleryimage.utils.GalleryUtils;

public class GalleryFragment extends Fragment implements OnGalleryItemAdded {
    private static final String GALLERY_ITEMS = "GALLERY_ITEMS";
    private static final String GALLERY_ALBUM = "GALLERY_ALBUM";
    public static final int MAX_COLUMN = 3;

    private RecyclerView recyclerView;
    private GalleryAdapter galleryAdapter;

    private List<GalleryItem> galleryItems;
    private GalleryAlbum galleryAlbum;

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
        View view = inflater.inflate(R.layout.gallery_fragment, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            galleryItems = arguments.getParcelableArrayList(GALLERY_ITEMS);
            if (galleryItems == null) {
                galleryItems = new ArrayList<>();
            }
            galleryAlbum = arguments.getParcelable(GALLERY_ALBUM);
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), MAX_COLUMN));
//        recyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.medium_padding)));
        galleryAdapter = new GalleryAdapter(galleryItems, getAlbumClickListener(), (GalleryActivity) getActivity());
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
}
