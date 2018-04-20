package com.amandeep.booksentering;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {
    EditText title, author, subject, field, noOfBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = (EditText) findViewById(R.id.title);
        author = (EditText) findViewById(R.id.author);
        subject = (EditText) findViewById(R.id.subject);
        noOfBooks = (EditText) findViewById(R.id.noOfCopies);
        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subject.getText().toString().matches("") || title.getText().toString().matches("") || author.getText().toString().matches("") || noOfBooks.getText().toString().matches("")) {

                } else {
                    ParseObject object = new ParseObject("Books");
                    object.put("title", title.getText().toString());
                    object.put("author", author.getText().toString());
                    object.put("subject", subject.getText().toString());
                    object.put("NumberOfCopies", Integer.parseInt(noOfBooks.getText().toString()));
                    try {
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {

                                if (e == null) {

                                    //  startActivity(new Intent(getApplicationContext(),UploadImages.class).putExtra("username",phoneno.getText().toString()).putExtra(""));
                                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                    title.setText("");
                                    author.setText("");
                                    subject.setText("");
                                    noOfBooks.setText("");

                                } else {

                                    Toast.makeText(MainActivity.this, "You could not be registered - please try again later.", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        Log.i("ex", e.toString());

                    }
                }

            }
        });


    }
}

