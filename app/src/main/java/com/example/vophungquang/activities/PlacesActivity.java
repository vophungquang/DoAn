package com.example.vophungquang.activities;
/**
 * Created by vophungquang
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vophungquang.R;
import com.example.vophungquang.adapter.PlaceAdapter;
import com.example.vophungquang.model.Place;
import com.example.vophungquang.sqlite.DatabaseUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PlacesActivity extends AppCompatActivity {

    private static final String TAG = PlacesActivity.class.getName();

    private static final String KEY_CATEGORY_PUT_EXTRA = "key_category";
    private static final String KEY_PLACE_PUT_EXTRA = "key_place";
    private static final String ACTION_ADD = "add";
    public static final String ACTION_SHOW_ALL_PLACE = "action_show_all";

    private static final int REQUEST_CODE_ADD = 10;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lv_places)
    ListView lvPlaces;
    @BindView(R.id.txt_place_no_data)
    TextView txtPlaceNoData;
    @BindView(R.id.float_add_place)
    FloatingActionButton floatAddPlace;
    @BindView(R.id.btn_show_all_on_map)
    Button btnShowAllOnMap;

    private DatabaseUtil databaseUtil;
    private Category category;
    private List<Place> placeList = new ArrayList<>();
    private PlaceAdapter placeAdapter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        initProgressDialog();
        init();
        setOnClick();
    }

    private void init() {
        databaseUtil = DatabaseUtil.getInstance(this);
        Intent intent = getIntent();
        category = (Category) intent.getSerializableExtra(KEY_CATEGORY_PUT_EXTRA);
        String categoryId = category.getId();
        new GetPlacesFromDatabase(databaseUtil).execute(categoryId);

        placeAdapter = new PlaceAdapter(this, placeList);
        lvPlaces.setAdapter(placeAdapter);
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(PlacesActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
    }

    private void setOnClick() {
        lvPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Place place = placeList.get(position);
                Intent intent = new Intent(PlacesActivity.this, PlaceDetailActivity.class);
                intent.putExtra(KEY_PLACE_PUT_EXTRA, place.getPlaceId());
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.float_add_place, R.id.btn_show_all_on_map})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.float_add_place:
                Intent intent = new Intent(PlacesActivity.this,AddEditActivity.class);
                intent.setAction(ACTION_ADD);
                intent.putExtra(KEY_CATEGORY_PUT_EXTRA,category);
                startActivityForResult(intent,REQUEST_CODE_ADD);
                break;
            case R.id.btn_show_all_on_map:
                Intent intentMap = new Intent(PlacesActivity.this,AddEditActivity.class);
                intentMap.setAction(ACTION_SHOW_ALL_PLACE);
                startActivity(intentMap);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    class GetPlacesFromDatabase extends AsyncTask<String, Void, List<Place>> {

        private DatabaseUtil databaseUtil;

        public GetPlacesFromDatabase(DatabaseUtil databaseUtil) {
            this.databaseUtil = databaseUtil;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected List<Place> doInBackground(String... params) {
            String caregoryId = params[0];
            return databaseUtil.getListPlacesWithCategory(caregoryId);
        }

        @Override
        protected void onPostExecute(List<Place> places) {
            super.onPostExecute(places);
            placeList = places;
            if (!placeList.isEmpty()) {
                txtPlaceNoData.setVisibility(View.GONE);
                placeAdapter.updateList(placeList);
            } else txtPlaceNoData.setVisibility(View.VISIBLE);

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
}
