package masacre.galleryimage.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import masacre.galleryimage.R;
import masacre.galleryimage.fragments.GalleryFragment;
import masacre.galleryimage.model.GalleryItem;
import masacre.galleryimage.model.GalleryPhoto;

public class GalleryActivity extends AppCompatActivity {

    private static final String GALLERY_ITEMS = "GALLERY_ITEMS";
    private final Set<GalleryPhoto> selectedPhotos = new HashSet<>();


    public static Intent getIntent(Context context, ArrayList<GalleryItem> galleryItems) {
        Intent intent = new Intent(context, GalleryActivity.class);
        intent.putParcelableArrayListExtra(GALLERY_ITEMS, galleryItems);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity);

        if (savedInstanceState == null) {
            ArrayList<GalleryItem> galleryItems = getIntent().getParcelableArrayListExtra(GALLERY_ITEMS);
            GalleryFragment fragment = GalleryFragment.newInstance(galleryItems);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_content, fragment)
                    .commit();
        }
    }

    public void addPhotoSelected(GalleryPhoto galleryPhoto) {
        selectedPhotos.add(galleryPhoto);
    }

    public boolean isPhotoSelected(GalleryPhoto galleryPhoto) {
        return selectedPhotos.contains(galleryPhoto);
    }

    public void removePhotoSelected(GalleryPhoto galleryPhoto) {
        selectedPhotos.remove(galleryPhoto);
    }
}
