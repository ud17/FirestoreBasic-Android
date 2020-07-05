package com.example.firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class CreateCollectionActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText collection_et;
    private Button saveCollection;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = firestore.collection("Journal");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_collection);

        collection_et = findViewById(R.id.collection_et);
        saveCollection = findViewById(R.id.saveCollection);

        saveCollection.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.saveCollection):
                createCollection();
                break;
        }
    }

    private void createCollection() {
        if (collection_et.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Check your credentials!", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String,String> params = new HashMap<>();

        params.put("key" , "value");
        params.put("value" , "pair");

        collectionReference.document(collection_et.getText().toString().trim()).set(params).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(CreateCollectionActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateCollectionActivity.this, "Could not create collection!", Toast.LENGTH_SHORT).show();
                Log.e("CollectionActivityAG" , "onFailure: " + e.toString() );
            }
        });
        Log.e("CollectionActivity", "createCollection: "  + collectionReference.document(collection_et.getText().toString().trim()) );
    }
}