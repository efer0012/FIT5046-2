package com.example.befit.fragment;

import com.example.befit.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.befit.databinding.ClubFragmentBinding;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClubFragment extends Fragment {

    private ClubFragmentBinding addBinding;

    public ClubFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        addBinding = ClubFragmentBinding.inflate(inflater, container, false);
        View view = addBinding.getRoot();

        // Initialize the Mapbox map
        addBinding.clubMap.onCreate(savedInstanceState);
        addBinding.clubMap.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                // Set the map style
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        // Perform geocoding for the address
                        geocodeAddress(mapboxMap);
                    }
                });
            }
        });

        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addBinding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        addBinding.clubMap.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        addBinding.clubMap.onResume();
    }

    @Override
    public void onPause() {
        addBinding.clubMap.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        addBinding.clubMap.onStop();
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        addBinding.clubMap.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        if (addBinding != null) {
            addBinding.clubMap.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        addBinding.clubMap.onLowMemory();
    }

    private void geocodeAddress(MapboxMap mapboxMap) {
        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken(getString(R.string.mapbox_access_token))
                .query("20 Exhibition Walk, Clayton VIC 3168")
                .build();

        mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(@NonNull Call<GeocodingResponse> call, @NonNull Response<GeocodingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GeocodingResponse geocodingResponse = response.body();
                    List<CarmenFeature> results = geocodingResponse.features();
                    if (!results.isEmpty()) {
                        CarmenFeature feature = results.get(0);
                        Point center = feature.center();
                        if (center != null) {
                            double latitude = center.latitude();
                            double longitude = center.longitude();

                            CameraPosition position = new CameraPosition.Builder()
                                    .target(new LatLng(latitude, longitude))
                                    .zoom(11)
                                    .build();
                            mapboxMap.setCameraPosition(position);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<GeocodingResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}