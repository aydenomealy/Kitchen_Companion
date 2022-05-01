package com.example.kitchencompanion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kitchencompanion.db.Fridge;
import com.example.kitchencompanion.db.FridgeDatabase;
import com.example.kitchencompanion.db.FridgeViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyFridgeFrag extends Fragment {

    public interface OnMessageFridge {
        void sendMessageFridge(ArrayList<Fridge> message);
    }

    public MyFridgeFrag() {
        super(R.layout.frag_my_fridge);
    }

    private static final ArrayList<Fridge> foodIds = new ArrayList<>();

    OnMessageFridge onMessageListener;
    FridgeListAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String name = sharedPreferences.getString("name", "");

        if (!name.equals("")) {
            name = name.concat("'s Fridge");
            ((TextView) requireView().findViewById(R.id.textView)).setText(name);
        }

        //TODO
//        TransitionInflater inflater = TransitionInflater.from(requireContext());
//        setEnterTransition(inflater.inflateTransition(R.transition.fade));
//        setExitTransition(inflater.inflateTransition(R.transition.fade));


        RecyclerView recyclerView = requireView().findViewById(R.id.recycle);
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);
        adapter = new FridgeListAdapter(view.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        FridgeViewModel fridgeViewModel = new ViewModelProvider(this).get(FridgeViewModel.class);
        fridgeViewModel.getAllFridge().observe(getViewLifecycleOwner(), adapter::setFridge);

        requireView().findViewById(R.id.delete).setOnClickListener(v -> {
            for (Fridge fridge : foodIds) {
                FridgeDatabase.delete(fridge);
            }
            foodIds.clear();
            FridgeDatabase.getDatabase(getContext());
        });


        requireView().findViewById(R.id.generate).setOnClickListener(v -> {
            if (foodIds.size() != 0) {
                onMessageListener.sendMessageFridge(foodIds);
                foodIds.clear();
            } else
                Toast.makeText(getActivity(), "Select at least one item", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnMessageFridge)
            onMessageListener = (OnMessageFridge) context;

        else
            throw new ClassCastException();
    }

    public static class FridgeListAdapter extends RecyclerView.Adapter<FridgeListAdapter.FridgeViewHolder> {

        static class FridgeViewHolder extends RecyclerView.ViewHolder {

            private final TextView titleView;
            private final ImageView imageView;
            private final View root;

            public Fridge fridge;

            private FridgeViewHolder(View itemView) {
                super(itemView);

                titleView = itemView.findViewById(R.id.foodName);
                imageView = itemView.findViewById(R.id.foodImage);
                root = itemView.findViewById(R.id.root);
            }
        }

        private final LayoutInflater layoutInflater;
        private List<Fridge> fridgeList;

        FridgeListAdapter(Context context) {
            layoutInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public FridgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.list_item, parent, false);
            return new FridgeViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull FridgeViewHolder holder, int position) {
            if (fridgeList != null) {
                Fridge current = fridgeList.get(position);
                holder.fridge = current;
                holder.titleView.setText(current.item);
                switch (current.category) {
                    //TODO add grain and other category
                    case ("Fruit"):
                        holder.imageView.setImageResource(R.mipmap.fruit_icon);
                        break;
                    case ("Meat"):
                        holder.imageView.setImageResource(R.mipmap.meat_icon);
                        break;
                    case ("Veggie"):
                        holder.imageView.setImageResource(R.mipmap.veggie_icon);
                        break;
                    case ("Dairy"):
                        holder.imageView.setImageResource(R.mipmap.dairy_icon);
                        break;
                    case ("Spice"):
                        //TODO change other icon to spice
                        holder.imageView.setImageResource(R.mipmap.other_icon);
                        break;
                    case ("Fat"):
                        holder.imageView.setImageResource(R.mipmap.fats_icon);
                        break;
                }

                holder.root.setOnLongClickListener(v -> {
                    if (foodIds.contains(current)) {
                        foodIds.remove(current);
                        holder.root.setBackgroundResource(R.drawable.lightmode_background);
                    } else {
                        foodIds.add(current);
                        holder.root.setBackgroundResource(R.drawable.selected);
                    }
                    return true;
                });
            } else {
                //TODO
                holder.titleView.setText("...loading...");
                //holder.imageView.setImageResource(temp icon);
            }
        }

        @Override
        public void onViewAttachedToWindow(@NonNull FridgeViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            if (foodIds.contains(holder.fridge)) {
                holder.root.setBackgroundResource(R.drawable.selected);
            }
        }

        @Override
        public void onViewDetachedFromWindow(@NonNull FridgeViewHolder holder) {
            super.onViewDetachedFromWindow(holder);
            if (!foodIds.contains(holder.fridge)) {
                holder.root.setBackgroundResource(R.drawable.lightmode_background);
            }
        }

        void setFridge(List<Fridge> items) {
            this.fridgeList = items;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            if (fridgeList != null)
                return fridgeList.size();
            else return 0;
        }
    }

    ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            FridgeDatabase.delete(((FridgeListAdapter.FridgeViewHolder) viewHolder).fridge);
            adapter.notifyDataSetChanged();
        }
    };
}
