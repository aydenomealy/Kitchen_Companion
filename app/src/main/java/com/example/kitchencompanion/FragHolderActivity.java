package com.example.kitchencompanion;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class FragHolderActivity extends AppCompatActivity implements GenerateRecipeFrag.OnMessageGen {
    FragmentManager fragmentManager = getSupportFragmentManager();
    int currentFrag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holder);
        setSupportActionBar(findViewById(R.id.toolbar));

        findViewById(R.id.imageView2).setOnClickListener(v -> {
            ((Toolbar)findViewById(R.id.toolbar)).setTitle("Recipes");

            if(currentFrag == R.id.generate) {
                finish();
                startActivity(new Intent(this, MainActivity.class));
            }
            fragmentManager.popBackStackImmediate();
            currentFrag = R.id.generate;
        });

        ((Toolbar)findViewById(R.id.toolbar)).setTitle("Recipes");

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("list", getIntent().getStringArrayListExtra("list"));
        GenerateRecipeFrag frag = new GenerateRecipeFrag();
        frag.setArguments(bundle);

        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frag_view, frag, null)
                .commit();
        currentFrag = R.id.generate;
    }

    @Override
    public void sendMessageGen(Bundle message) {
        ViewRecipeFrag frag = new ViewRecipeFrag();
        frag.setArguments(message);

        ((Toolbar)findViewById(R.id.toolbar)).setTitle(message.getString("title"));

        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.frag_view, frag)
                .addToBackStack("view")
                .commit();
        currentFrag = R.id.frag_view;
    }
}

