package com.example.android.pets;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.data.PetCursorAdapter;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

  private static final int PET_LOADER = 0;
  PetCursorAdapter petCursorAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_catalog);

    // Setup FAB to open EditorActivity
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
        startActivity(intent);
      }
    });

    ListView displayView = (ListView) findViewById(R.id.pet_list_view);
    View emptyView = findViewById(R.id.empty_view);
    displayView.setEmptyView(emptyView);

    petCursorAdapter = new PetCursorAdapter(this, null);
    displayView.setAdapter(petCursorAdapter);

    getLoaderManager().initLoader(PET_LOADER, null, this);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu options from the res/menu/menu_catalog.xml file.
    // This adds menu items to the app bar.
    getMenuInflater().inflate(R.menu.menu_catalog, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // User clicked on a menu option in the app bar overflow menu
    switch (item.getItemId()) {
      // Respond to a click on the "Insert dummy data" menu option
      case R.id.action_insert_dummy_data:
        insertPet();
        return true;
      // Respond to a click on the "Delete all entries" menu option
      case R.id.action_delete_all_entries:
        getContentResolver().delete(PetEntry.CONTENT_URI, null, null);
        // Do nothing for now
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onStart() {
    super.onStart();
  }

  private void insertPet() {
    ContentValues values = new ContentValues();
    values.put(PetEntry.COLUMN_PET_NAME, "Toto");
    values.put(PetEntry.COLUMN_PET_BREED, "Terrier");
    values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
    values.put(PetEntry.COLUMN_PET_WEIGHT, 7);

    getContentResolver().insert(PetEntry.CONTENT_URI, values);
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    String[] projection = {
        PetEntry._ID,
        PetEntry.COLUMN_PET_NAME,
        PetEntry.COLUMN_PET_BREED,
    };

    switch (id) {
      case PET_LOADER:
        return new CursorLoader(
            this,
            PetEntry.CONTENT_URI,
            projection,
            null,
            null,
            null
        );
      default:
        return null;
    }
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    petCursorAdapter.swapCursor(data);

  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    petCursorAdapter.changeCursor(null);
  }
}