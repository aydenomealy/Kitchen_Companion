package com.example.kitchencompanion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;

//TODO make this look pretty
public class ViewRecipeFrag extends Fragment {
    public ViewRecipeFrag() {
        super(R.layout.frag_view_recipe);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        YouTubePlayerView youTubePlayerView = requireView().findViewById(R.id.youtube);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                if (!getArguments().getString("video").isEmpty())
                    youTubePlayer.cueVideo(getArguments().getString("video"), 0);
                else
                    youTubePlayer.cueVideo("dQw4w9WgXcQ", 0);
            }
        });

        ((TextView) requireView().findViewById(R.id.directions)).setText(getArguments().getString("directions"));
        ((TextView) requireView().findViewById(R.id.servings)).setText("Number of servings: " + getArguments().getString("serving"));

        RecyclerView recyclerView = requireView().findViewById(R.id.recycle);
        ViewRecipeAdapter adapter = new ViewRecipeAdapter(getArguments().getStringArrayList("ingredients"), getArguments().getStringArrayList("servings"));
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    public static class ViewRecipeAdapter extends RecyclerView.Adapter<ViewRecipeAdapter.RecipeViewHolder> {
        private final ArrayList<String> ingredients;
        private final ArrayList<String> servings;

        public ViewRecipeAdapter(ArrayList<String> ingredients, ArrayList<String> servings) {
            this.ingredients = ingredients;
            this.servings = servings;
        }

        @NonNull
        @Override
        public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_recipe_item, parent, false);
            return new RecipeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
            holder.getIngredientView().setText(ingredients.get(position));
            holder.getServingView().setText(servings.get(position));
        }

        @Override
        public int getItemCount() {
            return ingredients.size();
        }

        public static class RecipeViewHolder extends RecyclerView.ViewHolder {
            private final TextView ingredient;
            private final TextView serving;

            public RecipeViewHolder(@NonNull View itemView) {
                super(itemView);

                this.ingredient = itemView.findViewById(R.id.foodName);
                this.serving = itemView.findViewById(R.id.servings);
            }

            public TextView getIngredientView() {
                return ingredient;
            }

            public TextView getServingView() {
                return serving;
            }
        }
    }
}
