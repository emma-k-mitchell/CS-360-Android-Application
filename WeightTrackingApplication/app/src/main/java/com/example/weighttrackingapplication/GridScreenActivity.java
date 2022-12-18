package com.example.weighttrackingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GridScreenActivity extends AppCompatActivity {

    // private variables on the grid screen
    private Button enterDataButton;
    private EditText weightEditText;
    private EditText dateEditText;
    private GraphView graphView;

    // database related variables
    WeightDBHelper weightDB;
    SQLiteDatabase sq;

    // graph series
    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[0]);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_screen);

        // Assign the views using IDs
        enterDataButton = (Button) findViewById(R.id.enterWeightButton);
        weightEditText = (EditText) findViewById(R.id.weightData);
        dateEditText = (EditText) findViewById(R.id.dateData);
        graphView = (GraphView) findViewById(R.id.graphView);

        weightDB = new WeightDBHelper(this);
        sq = weightDB.getWritableDatabase();

        graphView.addSeries(series);

        // code for when the user presses the enter button on the grid screen
        enterDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get the x value
                String date = dateEditText.getText().toString();
                // get the y value
                Double weight = Double.parseDouble(String.valueOf(weightEditText.getText()));

                // confirmation to the user that their data was entered
                Toast.makeText(GridScreenActivity.this, "Your weight of " + weight + " lbs was recorded on " + date, Toast.LENGTH_SHORT).show();

                // calls the method below, and inserts the data from the edit texts into the table weights
                insert(date, weight);
            }
        });
    }

    // insert the entered data into the weights table
    public void insert(String date, Double weight){
        weightDB.insertData(date, weight);

        series.resetData(GrabData());
    }

    // takes the values in the weights table and adds them to the gridView
    private DataPoint[] GrabData(){

        String[] column = {"date", "weight"};
        @SuppressLint("Recycle") Cursor cursor = sq.query("weights", column, null, null, null, null, null);

        DataPoint[] dataPoints = new DataPoint[cursor.getCount()];

        for (int i = 0; i < cursor.getCount(); i++){
            cursor.moveToNext();
            dataPoints[i] = new DataPoint(cursor.getInt(0), cursor.getInt(1));
        }

        return dataPoints;
    }
}