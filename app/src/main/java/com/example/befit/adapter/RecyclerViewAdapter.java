package com.example.befit.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.befit.R;
import com.example.befit.databinding.CardLayoutBinding;
import com.example.befit.entity.Classes;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Classes> beFitClasses;

    private Context mContext;

    public RecyclerViewAdapter(Context context) {
        mContext = context;
    }

    //creates a new viewholder that is constructed with a new View, inflated from a layout
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                             int viewType) {
        CardLayoutBinding binding=
                CardLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    // this method binds the view holder created with data that will be displayed
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder, int
            position) {
        viewHolder.binding.cardClassName.setText(beFitClasses.get(position).className);
        viewHolder.binding.cardClassTime.setText(beFitClasses.get(position).startTime);
        viewHolder.binding.cardEndTime.setText(beFitClasses.get(position).endTime);

        viewHolder.binding.cardInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass data
                Bundle bundle = new Bundle();
                bundle.putString("classId", String.valueOf(beFitClasses.get(viewHolder.getAdapterPosition()).classId));
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_to_classInfoFragment, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (beFitClasses == null){
            //Log.d("BeFiT", "null zzz");
            return 0;}
        else{
            //Log.d("BeFiT", "there's something!");
            return beFitClasses.size();}
    }

    public void setClasses(List<Classes> beFitClasses) {
        this.beFitClasses = beFitClasses;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardLayoutBinding binding;
        public ViewHolder(CardLayoutBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
