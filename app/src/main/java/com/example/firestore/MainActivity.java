package com.example.firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "FirebaseFirestore" ;
    private EditText title_et,thoughts_et;
    private Button saveBtn,showBtn;
    private TextView title_tv,thought_tv;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private DocumentReference documentReference = firestore.document("Journal/First thoughts");

    public static final String KEY_TITLE = "title";
    public static final String KEY_THOUGHT = "thought";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title_et = findViewById(R.id.title);
        thoughts_et = findViewById(R.id.thoughts);

        title_tv = findViewById(R.id.title_tv);
        thought_tv = findViewById(R.id.thought_tv);

        saveBtn = findViewById(R.id.saveBtn);
        showBtn = findViewById(R.id.showBtn);

        saveBtn.setOnClickListener(this);
        showBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.showBtn):
                documentReference.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists()){
                                    String title = documentSnapshot.getString("title");
                                    String thoughts = documentSnapshot.getString("thought");

                                    title_tv.setText("Title : " + title);
                                    thought_tv.setText("Thought : " + thoughts);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Could not load data!", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "onFailure: " + e.toString());
                            }
                        });
                break;

            case (R.id.saveBtn):
                String title = title_et.getText().toString().trim();
                String thoughts = thoughts_et.getText().toString().trim();

                if (title.isEmpty() || thoughts.isEmpty()){
                    Toast.makeText(this, "Check your credentials!", Toast.LENGTH_SHORT).show();
                    return;
                }

                HashMap<String,Object> params = new HashMap<>();

                params.put(KEY_TITLE,title);
                params.put(KEY_THOUGHT,thoughts);

                firestore.collection("Journal")
                        .document("First thoughts")
                        .set(params)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                            }
                        })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "onFailure: " + e.toString() );
                        }
                    });
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        documentReference.addSnapshotListener(this , new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    Toast.makeText(MainActivity.this, "Somewthing went wrong!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG + " inside snapshot", "onEvent: " + e.toString() );
                    return;
                }
                if (documentSnapshot.exists()){
                    String title = documentSnapshot.getString("title");
                    String thoughts = documentSnapshot.getString("thought");

                    title_tv.setText("Title : " + title);
                    thought_tv.setText("Thought : " + thoughts);
                }
            }
        });
    }
}