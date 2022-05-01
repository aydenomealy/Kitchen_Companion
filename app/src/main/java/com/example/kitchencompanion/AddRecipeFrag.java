package com.example.kitchencompanion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kitchencompanion.db.FridgeDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddRecipeFrag extends Fragment {
    public AddRecipeFrag() {
        super(R.layout.frag_add_recipe);
    }

    List<String> data = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    IngredientListAdapter adapter;

    private final BroadcastReceiver receiver = (new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            data.add(intent.getStringExtra("message"));
        }
    });

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO
//        TransitionInflater inflater = TransitionInflater.from(requireContext());
//        setEnterTransition(inflater.inflateTransition(R.transition.fade));
//        setExitTransition(inflater.inflateTransition(R.transition.fade));
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,
                new IntentFilter("add-food-recipe"));

        requireView().findViewById(R.id.add_ingredient).setOnClickListener(v ->
                ((MainActivity) getActivity()).addToRecipe());

        adapter = new IngredientListAdapter(data);
        RecyclerView recyclerView = requireView().findViewById(R.id.recycle);
        recyclerView.setAdapter(adapter);
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        requireView().findViewById(R.id.finish).setOnClickListener(v -> {
            String title = ((EditText) requireView().findViewById(R.id.title)).getText().toString();
            String servings = ((EditText) requireView().findViewById(R.id.servings)).getText().toString();
            String directions = ((EditText) requireView().findViewById(R.id.directions)).getText().toString();
            String video = ((EditText) requireView().findViewById(R.id.video)).getText().toString();
            List<String> servingList = adapter.servingsList;

            HashMap<String, Boolean> search = new HashMap<>();
            HashMap<String, String> ingredients = new HashMap<>();
            for (int i = 0; i < data.size(); i++) {
                search.put(data.get(i), true);
                ingredients.put(data.get(i), servingList.get(i));
            }

            if (!video.isEmpty())
                video = video.split("=")[1];

            if (!(title.isEmpty() || directions.isEmpty() || servings.isEmpty() || ingredients.isEmpty())) {
                Map<String, Object> recipe = new HashMap<>();
                recipe.put("title", title);
                recipe.put("video", video);
                recipe.put("serving", servings);
                recipe.put("directions", directions);
                recipe.put("ingredients", ingredients);
                recipe.put("search", search);

                db.collection("recipes").document()
                        .set(recipe)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(getActivity(), "Success recipe added!", Toast.LENGTH_LONG).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getActivity(), "Recipe submission failed try again later", Toast.LENGTH_LONG).show();
                        });

                ((EditText) requireView().findViewById(R.id.title)).setText("");
                ((EditText) requireView().findViewById(R.id.video)).setText("");
                ((EditText) requireView().findViewById(R.id.servings)).setText("");
                ((EditText) requireView().findViewById(R.id.directions)).setText("");
                data.clear();
                adapter.setRecipeList(data);
                adapter.servingsList = new ArrayList<>();
            } else
                Toast.makeText(getActivity(), "Invalid submission", Toast.LENGTH_LONG).show();
        });
    }

    public static class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.IngredientViewHolder> {
        private List<String> recipeList;
        public List<String> servingsList;

        public static class IngredientViewHolder extends RecyclerView.ViewHolder {
            private final TextView textView;
            private final EditText editText;
            private final EditTextListener editTextListener;

            public IngredientViewHolder(View itemView, EditTextListener editTextListener) {
                super(itemView);

                this.textView = itemView.findViewById(R.id.foodName);
                this.editText = itemView.findViewById(R.id.servings);
                this.editTextListener = editTextListener;
            }

            void enableTextWatcher() {
                editText.addTextChangedListener(editTextListener);
            }

            void disableTextWatcher() {
                editText.removeTextChangedListener(editTextListener);
            }

            public TextView getTextView() {
                return textView;
            }
        }

        public IngredientListAdapter(List<String> dataset) {
            recipeList = dataset;
            servingsList = new ArrayList<>();
            if (dataset.size() != 0)
                dataset.forEach(s -> servingsList.add(" "));
        }

        @NonNull
        @Override
        public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);
            return new IngredientViewHolder(view, new EditTextListener());
        }

        @Override
        public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
            holder.getTextView().setText(recipeList.get(position));
            holder.editTextListener.updatePosition(holder.getAbsoluteAdapterPosition());
        }

        @Override
        public void onViewAttachedToWindow(@NonNull IngredientViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            holder.enableTextWatcher();
        }

        @Override
        public void onViewDetachedFromWindow(@NonNull IngredientViewHolder holder) {
            super.onViewDetachedFromWindow(holder);
            holder.disableTextWatcher();
        }

        //TODO
        void setRecipeList(List<String> recipeList) {
            this.recipeList = recipeList;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return recipeList.size();
        }

        private class EditTextListener implements TextWatcher {
            private int position;

            public void updatePosition(int position) {
                this.position = position;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                servingsList.set(position, s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        }
    }
    ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int pos = viewHolder.getAbsoluteAdapterPosition();
            data.remove(pos);
            adapter.notifyItemRemoved(pos);
        }
    };
}
