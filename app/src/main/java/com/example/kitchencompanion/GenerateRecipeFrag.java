package com.example.kitchencompanion;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GenerateRecipeFrag extends Fragment {

    public interface OnMessageGen{
        void sendMessageGen(Bundle message);
    }

    public GenerateRecipeFrag() {
        super(R.layout.frag_gen_recipe);
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    OnMessageGen onMessageListener;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Query query = db.collection("recipes");

        for (String item : getArguments().getStringArrayList("list"))
            query = query.whereNotEqualTo("ingredients." + item, false);

        query.get()
                .addOnSuccessListener(task -> {
                    Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT).show();
                    RecyclerView recyclerView = requireView().findViewById(R.id.recycle);
                    RecipeListAdapter adapter = new RecipeListAdapter(task.getDocuments());
                    recyclerView.setAdapter(adapter);

                    recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnMessageGen)
            onMessageListener = (OnMessageGen) context;

        else
            throw new ClassCastException();
    }

    public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {
        protected List<DocumentSnapshot> recipeList;

        public class RecipeViewHolder extends RecyclerView.ViewHolder {
            private final TextView textView;

            public RecipeViewHolder(View itemView) {
                super(itemView);

                this.textView = itemView.findViewById(R.id.recipename);
            }

            public TextView getTextView() {
                return textView;
            }
        }

        public RecipeListAdapter(List<DocumentSnapshot> dataset) {
            recipeList = dataset;
        }


        @NonNull
        @Override
        public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
            return new RecipeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
            String name = recipeList.get(position).get("title").toString();
            holder.getTextView().setText(name);

            //TODO fix directions being one giant paragraph
            holder.itemView.setOnClickListener(v -> {
                Bundle args = new Bundle();
                ArrayList<String> ingredients = new ArrayList<>();
                ArrayList<String> servings = new ArrayList<>();
                args.putString("title", name);
                args.putString("serving", recipeList.get(position).get("serving").toString());
                args.putString("directions", recipeList.get(position).get("directions").toString());
                args.putString("video", recipeList.get(position).get("video").toString());

                ((HashMap<String, String>) recipeList.get(position).get("ingredients")).forEach((key, val) -> {
                    ingredients.add(key);
                    servings.add(val);
                });

                args.putStringArrayList("servings", servings);
                args.putStringArrayList("ingredients", ingredients);

                onMessageListener.sendMessageGen(args);
            });
        }

        @Override
        public int getItemCount() {
            return recipeList.size();
        }
    }
}
