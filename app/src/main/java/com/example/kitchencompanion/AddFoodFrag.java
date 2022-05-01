package com.example.kitchencompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kitchencompanion.db.Fridge;
import com.example.kitchencompanion.db.FridgeDatabase;

public class AddFoodFrag extends Fragment {
    public AddFoodFrag() {
        super(R.layout.frag_add_food);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO
//        TransitionInflater inflater = TransitionInflater.from(requireContext());
//        setEnterTransition(inflater.inflateTransition(R.transition.fade));
//        setExitTransition(inflater.inflateTransition(R.transition.fade));

        RecyclerView recyclerView = requireView().findViewById(R.id.recycle);
        AddFoodAdapter adapter = new AddFoodAdapter(Constants.meats, getArguments().getBoolean("use"));
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        requireView().findViewById(R.id.meat).setOnClickListener( v ->
                adapter.setFoodSet(Constants.meats));
        requireView().findViewById(R.id.veggie).setOnClickListener( v ->
                adapter.setFoodSet(Constants.veggies));
        requireView().findViewById(R.id.fruit).setOnClickListener( v ->
                adapter.setFoodSet(Constants.fruits));
        requireView().findViewById(R.id.dairy).setOnClickListener( v ->
                adapter.setFoodSet(Constants.dairy));
        requireView().findViewById(R.id.fat).setOnClickListener( v ->
                adapter.setFoodSet(Constants.fats));
        requireView().findViewById(R.id.spice).setOnClickListener( v ->
                adapter.setFoodSet(Constants.spices));
    }

    public static class AddFoodAdapter extends RecyclerView.Adapter<AddFoodAdapter.ViewHolder> {
        private String[] foodSet;
        private final boolean bool;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            private final TextView textView;
            private final ImageView imageView;

            public ViewHolder(View view) {
                super(view);

                textView = view.findViewById(R.id.foodName);
                imageView = view.findViewById(R.id.foodImage);
            }

            public TextView getTextView() {
                return textView;
            }

            public ImageView getImageView() {
                return imageView;
            }
        }

        public AddFoodAdapter(String[] dataSet, boolean bool) {
            foodSet = dataSet;
            this.bool = bool;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.getTextView().setText(foodSet[position]);
            holder.getImageView().setOnClickListener((v) -> {
                if(bool) {
                    String category;
                    switch (foodSet[0]) {
                        case ("Orange"):
                            category = "Fruit";
                            break;
                        case ("Potato"):
                            category = "Veggie";
                            break;
                        case ("Chicken Breast"):
                            category = "Meat";
                            break;
                        case ("Milk"):
                            category = "Dairy";
                            break;
                        case ("Coconut Oil"):
                            category = "Fat";
                            break;
                        case ("Black Pepper"):
                            category = "Spice";
                            break;
                        case ("Flour"):
                            category = "Grains";
                            break;
                        default:
                            category = "Other";
                            break;
                    }
                    FridgeDatabase.insert(new Fridge(0, foodSet[position], category, 3));
                } else {
                    Intent intent = new Intent("add-food-recipe");
                    intent.putExtra("message", foodSet[position]);
                    LocalBroadcastManager.getInstance(holder.imageView.getContext()).sendBroadcast(intent);

                }
                Toast.makeText(holder.getTextView().getContext(), "Success " + foodSet[position].toLowerCase() + " added!", Toast.LENGTH_SHORT).show();
            });
        }

        void setFoodSet(String[] foodSet) {
            this.foodSet = foodSet;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return foodSet.length;
        }
    }
}
