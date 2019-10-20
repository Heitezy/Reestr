package com.heitezy.reestr;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.IOException;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    static String textPerson = "";
    static String textCompany = "";
    static String signUri = "";

    SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        EditText editText = findViewById(R.id.editText);
        loadText("input", editText);
        EditText editText2 = findViewById(R.id.editText2);
        loadText("output", editText2);
        loadText("person", 1);
        loadText("company", 2);
        loadText("sign", 3);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Copyrights (C) Heitezy 2019", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) throws IOException {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText input = new EditText(this);
        EditText editText = findViewById(R.id.editText);
        EditText editText2 = findViewById(R.id.editText2);

        switch (view.getId()) {
            case (R.id.button3):
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent,"Select directory"), 1);
                break;
            case (R.id.button5):
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent,"Select directory"), 2);
                break;
            case (R.id.button9):
                builder.setTitle("Person");
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText(textPerson);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", (dialog, which) -> {
                    textPerson = input.getText().toString();
                    saveText("person", textPerson);
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

                builder.show();
                break;
            case (R.id.button10):
                builder.setTitle("Company");
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText(textCompany);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", (dialog, which) -> {
                    textCompany = input.getText().toString();
                    saveText("company", textCompany);
                });
                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

                builder.show();
                break;
            case (R.id.button8):
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Select image"), 3);
                break;
            case (R.id.button15):
                Convertor.convert(editText.getText().toString(), editText2.getText().toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String path = ASFUriHelper.getPath(this, uri);
            int lastSlash = Objects.requireNonNull(path).lastIndexOf("/");
            switch (requestCode) {
                case 1:
                    EditText editText = findViewById(R.id.editText);
                    path = path.substring(0, lastSlash);
                    editText.setText(path);
                    saveText("input", path);
                    break;
                case 2:
                    EditText editText2 = findViewById(R.id.editText2);
                    path = path.substring(0, lastSlash);
                    editText2.setText(path);
                    saveText("output", path);
                    break;
                case 3:
                    signUri = path;
                    saveText("sign", path);
            }
        }

    }

    void saveText(String variable, String data) {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(variable, data);
        ed.apply();
    }

    void loadText(String variable, int data) {
        sPref = getPreferences(MODE_PRIVATE);
        switch (data) {
            case 1:
                textPerson = sPref.getString(variable, "");
            case 2:
                textCompany = sPref.getString(variable, "");
            case 3:
                signUri = sPref.getString(variable, "");
        }
    }

    void loadText(String variable, EditText data) {
        sPref = getPreferences(MODE_PRIVATE);
        String savedText = sPref.getString(variable, "");
        data.setText(savedText);
    }


}
