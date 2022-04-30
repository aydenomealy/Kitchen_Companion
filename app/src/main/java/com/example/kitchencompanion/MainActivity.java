package com.example.kitchencompanion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.example.kitchencompanion.db.Fridge;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyFridgeFrag.OnMessageFridge{
    BottomNavigationView bNavView;
    FragmentManager fragmentManager = getSupportFragmentManager();
    int currentFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO fix duplication of fragments
        bNavView = findViewById(R.id.toolbar);
        bNavView.setSelectedItemId(R.id.home);
        bNavView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.add_food:
                    if(currentFrag != R.id.add_food) {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("use", true);
                        AddFoodFrag frag = new AddFoodFrag();
                        frag.setArguments(bundle);

                        fragmentManager.beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.frag_view, frag, null)
                                .addToBackStack("addFood")
                                .commit();
                        currentFrag = R.id.add_food;
                    }
                    return true;
                case R.id.home:
                    if(currentFrag != R.id.home) {
                        fragmentManager.beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.frag_view, MyFridgeFrag.class, null)
                                .addToBackStack("home")
                                .commit();
                        currentFrag = R.id.home;
                    }
                    return true;
                case R.id.add_recipe:
                    if(currentFrag != R.id.add_recipe) {
                        fragmentManager.beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.frag_view, AddRecipeFrag.class, null)
                                .addToBackStack("addRecipe")
                                .commit();
                        currentFrag = R.id.add_recipe;
                    }
                    return true;
            }
            return false;
        });

        findViewById(R.id.settings).setOnClickListener(v -> {
            finish();
            startActivity(new Intent(this, SettingsActivity.class));
        });


        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frag_view, MyFridgeFrag.class, null)
                .addToBackStack("home")
                .commit();
        currentFrag = R.id.home;
    }

    @Override
    public void sendMessageFridge(ArrayList<Fridge> message) {
        ArrayList<String> foods = new ArrayList<>();

        for(Fridge fridge : message)
            foods.add(fridge.item);

        Intent intent = new Intent(this, FragHolderActivity.class);
        intent.putStringArrayListExtra("list", foods);
        finish();
        startActivity(intent);
    }

    public void addToRecipe() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("use", false);
        AddFoodFrag frag = new AddFoodFrag();
        frag.setArguments(bundle);

        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frag_view, frag, null)
                .addToBackStack("addFood")
                .commit();
    }
}