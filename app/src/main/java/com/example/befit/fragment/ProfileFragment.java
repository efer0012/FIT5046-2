package com.example.befit.fragment;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.befit.LaunchActivity;
import com.example.befit.R;
import com.example.befit.database.Firestore;
import com.example.befit.entity.Customer;
import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.befit.databinding.ProfileFragmentBinding;

import java.util.List;
import java.util.Objects;

public class ProfileFragment extends Fragment implements OnMapReadyCallback {

    private ProfileFragmentBinding addBinding;
    private MapView mapView;
    private MapboxMap mapboxMap;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firestore firestore = new Firestore();
        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        addBinding = ProfileFragmentBinding.inflate(inflater, container, false);
        View view = addBinding.getRoot();

        mapView = view.findViewById(R.id.user_map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        TextView usernameTextView = view.findViewById(R.id.tv_name);
        TextView emailTextView = view.findViewById(R.id.tv_email);
        TextView dateOfBirthTextView = view.findViewById(R.id.tv_date_of_birth);
        TextView addressTextView = view.findViewById(R.id.tv_user_address);
        Button logoutButton = view.findViewById(R.id.btn_log_out);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
                // After logging out, redirect to the launch activity
                Intent intent = new Intent(getActivity(), LaunchActivity.class);
                startActivity(intent);
                // Finish the current activity
                requireActivity().finish();
            }
        });

        Firestore firestore = new Firestore();
        firestore.getUserInfo("example@email.com", new Firestore.OnGetDataListener() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                if (document != null && document.exists()) {
                    String firstName = document.getString("firstName");
                    String lastName = document.getString("lastName");
                    String email = document.getString("email");
                    String dateOfBirth = document.getString("dateOfBirth");
                    String address = document.getString("address");

                    usernameTextView.setText(firstName + " " + lastName);
                    emailTextView.setText(email);
                    dateOfBirthTextView.setText(dateOfBirth);
                    addressTextView.setText(address);
                    geocodeAddress(address);
                } else {
                    // Handle document does not exist
                    usernameTextView.setText("User not found");
                    emailTextView.setText("Email not found");
                    dateOfBirthTextView.setText("Date of birth not found");
                }
            }

            @Override
            public void onFailure() {
                usernameTextView.setText("Failed to retrieve user info");
                emailTextView.setText("Failed to retrieve email info");
                dateOfBirthTextView.setText("Failed to retrieve date of birth info");
            }
        });

        return view;
    }


    private void geocodeAddress(String address) {
        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken(getString(R.string.mapbox_access_token))
                .query(address)
                .build();

        mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(@NonNull Call<GeocodingResponse> call, @NonNull Response<GeocodingResponse> response) {
                assert response.body() != null;
                List<CarmenFeature> results = response.body().features();
                if (results.size() > 0) {
                    Point firstResultPoint = results.get(0).center();
                    assert firstResultPoint != null;
                    double latitude = firstResultPoint.latitude();
                    double longitude = firstResultPoint.longitude();
                    // Use latitude and longitude here
                    Timber.tag(TAG).d("Latitude: " + latitude + ", Longitude: " + longitude);

                    // 在这里设置相机的目标位置
                    CameraPosition position = new CameraPosition.Builder()
                            .target(new LatLng(latitude, longitude))
                            .zoom(15)
                            .build();
                    mapboxMap.setCameraPosition(position);
                } else {
                    // No results found
                    Timber.tag(TAG).d("No results found");
                }
            }

            @Override
            public void onFailure(@NonNull Call<GeocodingResponse> call, @NonNull Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
            }
        });
    }


    public void Logout() {
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
        addBinding = null;
    }
}
