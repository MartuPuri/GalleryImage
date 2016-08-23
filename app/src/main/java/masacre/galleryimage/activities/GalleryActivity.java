package masacre.galleryimage.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import masacre.galleryimage.R;
import masacre.galleryimage.fragments.GalleryFragment;
import masacre.galleryimage.model.GalleryItem;

public class GalleryActivity extends AppCompatActivity {

    private static final String GALLERY_ITEMS = "GALLERY_ITEMS";

    public static Intent getIntent(Context context, ArrayList<GalleryItem> galleryItems) {
        Intent intent = new Intent(context, GalleryActivity.class);
        intent.putExtra(GALLERY_ITEMS, galleryItems);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity);

        if (savedInstanceState == null) {
            ArrayList<GalleryItem> galleryItems = (ArrayList<GalleryItem>) getIntent().getSerializableExtra(GALLERY_ITEMS);
            GalleryFragment fragment = GalleryFragment.newInstance(galleryItems);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.main_content, fragment)
                    .commit();
        }
    }
}
