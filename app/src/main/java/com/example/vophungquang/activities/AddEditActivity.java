package com.example.vophungquang.activities;
/**
 * Created by vophungquang
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.vophungquang.R;
import com.example.vophungquang.model.Category;
import com.example.vophungquang.model.Place;
import com.example.vophungquang.network.api.ApiUtils;
import com.example.vophungquang.network.api.MapService;
import com.example.vophungquang.network.pojo.GeocodingRoot;
import com.example.vophungquang.sqlite.DatabaseUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEditActivity extends AppCompatActivity {

    private static final String TAG = AddEditActivity.class.getName();
    private static final String KEY_CATEGORY_PUT_EXTRA = "key_category";
    private static final String KEY_PLACE_PUT_EXTRA = "key_place";
    private static final String ACTION_ADD = "add";
    private static final String ACTION_EDIT = "edit";
    private static final int REQUEST_CODE_ADD = 10;
    private static final int REQUEST_CODE_EDIT = 11;

    @BindView(R.id.iv_place_edit)
    ImageView ivPlaceEdit;
    @BindView(R.id.edt_name_edit)
    EditText edtNameEdit;
    @BindView(R.id.edt_adress_edit)
    EditText edtAdressEdit;
    @BindView(R.id.edt_description_edit)
    EditText edtDescriptionEdit;
    @BindView(R.id.btn_save)
    Button btnSave;

    private Category category;
    private DatabaseUtil databaseUtil;
    private Place place;
    private ProgressDialog progressDialog;

    private boolean isAdd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        ButterKnife.bind(this);

        initProgressDialog();
        init();
    }

    private void init() {
        databaseUtil = DatabaseUtil.getInstance(this);
        Intent intent = getIntent();
        Log.e(TAG, intent.getAction());

        if (intent.getAction() == ACTION_ADD) {
            isAdd = true;
            Glide.with(this).load(R.drawable.icon_no_image).into(ivPlaceEdit);
            category = (Category) intent.getSerializableExtra(KEY_CATEGORY_PUT_EXTRA);
            Log.e(TAG, category.getName());

        } else if (intent.getAction() == ACTION_EDIT) {
            isAdd = false;
            Log.e(TAG, isAdd + "");
//            if (place.getImage() != null) {
//
//            } else {
//                Glide.with(this).load(R.drawable.icon_no_image).into(ivPlaceEdit);
//            }
            String placeId = intent.getStringExtra(KEY_PLACE_PUT_EXTRA);
            new GetPlaceFromDatabase(databaseUtil).execute(placeId);
        }
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
    }

    @OnClick(R.id.btn_save)
    public void onViewClicked() {
        progressDialog.setMessage("Saving");
        progressDialog.show();
        String placeName = edtNameEdit.getText().toString();
        String placeDes = edtDescriptionEdit.getText().toString();
        String placeAddress = edtAdressEdit.getText().toString();

        if (validate(placeName, placeAddress, placeDes)) {
            if (isAdd) {
                addPlaceToDatabase(category);
            } else {
                if (place != null)
                    editPlaceToDatabase(place);
            }
        }
    }

    private void addPlaceToDatabase(Category category) {
        String placeName = edtNameEdit.getText().toString();
        String placeDes = edtDescriptionEdit.getText().toString();
        String placeAddress = edtAdressEdit.getText().toString();

        final Place.Builder builder = new Place.Builder()
                .setName(placeName)
                .setDescription(placeDes)
                .setAddress(placeAddress)
                .setCategoryId(category.getId());

        MapService mapService = ApiUtils.getMapService();
        Call<GeocodingRoot> call = mapService.getLocationResults(
                placeAddress, getString(R.string.google_api_key));
        call.enqueue(new Callback<GeocodingRoot>() {
            @Override
            public void onResponse(retrofit2.Call<GeocodingRoot> call, Response<GeocodingRoot> response) {
                GeocodingRoot geocodingRoot = response.body();
                Log.e(TAG, geocodingRoot.getStatus());
                if (geocodingRoot.getStatus().equals("OK")) {
                    double lat = geocodingRoot.getResults().get(0).getGeometry().getLocation().getLat();
                    double lng = geocodingRoot.getResults().get(0).getGeometry().getLocation().getLng();
                    builder.setLng(lng);
                    builder.setLat(lat);

                    Log.e("Lat ", String.valueOf(lat));
                    Log.e("Lng ", String.valueOf(lng));

                    Toast.makeText(AddEditActivity.this, "Lat " + lat + " Lng " + lng, Toast.LENGTH_SHORT).show();

                    Place place = builder.build();
                    databaseUtil.insertPlace(place);
                    Toast.makeText(AddEditActivity.this, "Save Succesfull", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AddEditActivity.this, CategoriesActivity.class);
                    startActivity(intent);
                    finish();
                }
                progressDialog.dismiss();
            }


            @Override
            public void onFailure(retrofit2.Call<GeocodingRoot> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }

//            @Override
//            public void onResponse(Call<GeocodingRoot> call, Response<GeocodingRoot> response) {
//                GeocodingRoot geocodingRoot = response.body();
//                Log.e(TAG, geocodingRoot.getStatus());
//                if (geocodingRoot.getStatus().equals("OK")) {
//                    double lat = geocodingRoot.getResults().get(0).getGeometry().getLocation().getLat();
//                    double lng = geocodingRoot.getResults().get(0).getGeometry().getLocation().getLng();
//                    builder.setLng(lng);
//                    builder.setLat(lat);
//
//                    Log.e("Lat ", String.valueOf(lat));
//                    Log.e("Lng ", String.valueOf(lng));
//
//                    Toast.makeText(AddEditActivity.this, "Lat " + lat + " Lng " + lng, Toast.LENGTH_SHORT).show();
//
//                    Place place = builder.build();
//                    databaseUtil.insertPlace(place);
//                    Toast.makeText(AddEditActivity.this, "Save Succesfull", Toast.LENGTH_SHORT).show();
//
//                    Intent intent = new Intent(AddEditActivity.this, CategoriesActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onFailure(Call<GeocodingRoot> call, Throwable t) {
//                Log.e(TAG, t.getMessage());
//            }
        });
    }

    private void editPlaceToDatabase(Place place) {

    }


    class GetPlaceFromDatabase extends AsyncTask<String, Void, Place> {

        private DatabaseUtil databaseUtil;

        public GetPlaceFromDatabase(DatabaseUtil databaseUtil) {
            this.databaseUtil = databaseUtil;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Place doInBackground(String... params) {
            String placeId = params[0];
            return databaseUtil.getPlace(placeId);
        }

        @Override
        protected void onPostExecute(Place data) {
            super.onPostExecute(data);
            place = data;

            edtNameEdit.setText(place.getName());
            edtAdressEdit.setText(place.getAddress());
            edtDescriptionEdit.setText(place.getDescription());

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    private boolean validate(String name, String address, String des) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address)) {
            return false;
        }
        return true;
    }
}

