package com.example.firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class CreateCollectionActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText collection_et,newTitle,newThought;
    private Button saveCollection,saveDocument,showAllDocs;

    private AlertDialog dialog;
    private AlertDialog.Builder builder;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference collection = firestore.collection("Journal");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_collection);

        collection_et = findViewById(R.id.collection_et);
        saveCollection = findViewById(R.id.saveCollection);
        showAllDocs = findViewById(R.id.showAllDocs);

        saveCollection.setOnClickListener(this);
        showAllDocs.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.showAllDocs):
                collection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot snapshot: queryDocumentSnapshots){
                            Log.e("Tester", "onSuccess: " +  snapshot.getData());
                        }
                    }
                });
                break;
            case (R.id.saveCollection):
                if (collection_et.getText().toString().trim().isEmpty()){
                    Toast.makeText(this, "Check your credentials!", Toast.LENGTH_SHORT).show();
                    return;
                }

                final CollectionReference collectionReference = firestore.collection("Journal");
                HashMap<String,String> params = new HashMap<>();

                params.put("title" , "");
                params.put("thought" , "");

                collectionReference.document(collection_et.getText().toString().trim()).set(params).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CreateCollectionActivity.this, "Success!", Toast.LENGTH_SHORT).show();

                        //Inflating the popup window for input
                        builder = new AlertDialog.Builder(CreateCollectionActivity.this );
                        View view = getLayoutInflater().inflate(R.layout.inflater_layout , null);

                        newTitle = view.findViewById(R.id.newDoc_title);
                        newThought = view.findViewById(R.id.newDoc_thought);

                        saveDocument = view.findViewById(R.id.saveDoc);

                        dialog = builder.create();

                        dialog.setCancelable(false);

                        dialog.show();

                        saveDocument.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                HashMap<String,Object> params = new HashMap<>();

                                params.put("title" , newTitle.getText().toString().trim());
                                params.put("thought" , newThought.getText().toString().trim());


                                collectionReference.add(params);
                            }
                        });
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
                break;
        }
    }

    private void showDialog(){


    }
}