package com.example.yantocks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity {
    private int favourite;
    private ImageView imageViewDetailsFavourite;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        String shortName = intent.getStringExtra("shortName");
        String fullName = intent.getStringExtra("fullName");
        String country = intent.getStringExtra("country");
        country = country.substring(0, 1).toUpperCase() + country.substring(1);
        String sector = intent.getStringExtra("sector");
        String currency = intent.getStringExtra("currency");
        double close = intent.getDoubleExtra("close", 0);
        double high = intent.getDoubleExtra("high", 0);
        double low = intent.getDoubleExtra("low", 0);
        String changeDayFlat = intent.getStringExtra("changeDayFlat");
        String changeDayPercent = intent.getStringExtra("changeDayPercent");
        String timeUpdate = intent.getStringExtra("timeUpdate");
        favourite = intent.getIntExtra("favourite", 0);

        imageViewDetailsFavourite = findViewById(R.id.imageViewDetailsFavourite);
        if (favourite == 1) {
            imageViewDetailsFavourite.setImageResource(R.drawable.full_star);
        } else {
            imageViewDetailsFavourite.setImageResource(R.drawable.empty_star);
        }

        TextView textViewDetailsID = findViewById(R.id.textViewDetailsID);
        textViewDetailsID.setText(String.format(getString(R.string.textview_details_id), id));
        TextView textViewDetailsShortName = findViewById(R.id.textViewDetailsShortName);
        textViewDetailsShortName.setText(String.format(getString(R.string.textview_details_short_name), shortName));
        TextView textViewDetailsLongName = findViewById(R.id.textViewDetailsLongName);
        textViewDetailsLongName.setText(String.format(getString(R.string.textview_details_full_name), fullName));
        TextView textViewDetailsCountry = findViewById(R.id.textViewDetailsCountry);
        textViewDetailsCountry.setText(String.format(getString(R.string.textview_details_country), country));
        TextView textViewDetailsSector = findViewById(R.id.textViewDetailsSector);
        textViewDetailsSector.setText(String.format(getString(R.string.textview_details_sector), sector));
        TextView textViewDetailsCurrency = findViewById(R.id.textViewDetailsCurrency);
        textViewDetailsCurrency.setText(String.format(getString(R.string.textview_details_currency), currency));
        TextView textViewDetailsClose = findViewById(R.id.textViewDetailsClose);
        textViewDetailsClose.setText(String.format(getString(R.string.textview_details_close), close));
        TextView textViewDetailsHigh = findViewById(R.id.textViewDetailsHigh);
        textViewDetailsHigh.setText(String.format(getString(R.string.textview_details_high), high));
        TextView textViewDetailsLow = findViewById(R.id.textViewDetailsLow);
        textViewDetailsLow.setText(String.format(getString(R.string.textview_details_low), low));
        TextView textViewDetailsChangeFlat = findViewById(R.id.textViewDetailsChangeFlat);
        textViewDetailsChangeFlat.setText(String.format(getString(R.string.textview_details_change_flat), changeDayFlat));
        TextView textViewDetailsChangePercent = findViewById(R.id.textViewDetailsChangePercent);
        textViewDetailsChangePercent.setText(String.format(getString(R.string.textview_details_change_percent), changeDayPercent));
        TextView textViewDetailsTimeUpdate = findViewById(R.id.textViewDetailsTimeUpdate);
        textViewDetailsTimeUpdate.setText(String.format(getString(R.string.textview_details_time_update), timeUpdate));
    }

    public void onClickDetailsFavourite(View view) {
        SQLiteDatabase sqLiteDatabase = new StocksDBHelper(this).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (favourite == 0) {
            imageViewDetailsFavourite.setImageResource(R.drawable.full_star);
            favourite = 1;

            contentValues.put(StocksContract.StocksEntry.COLUMN_FAVOURITE, 1);
            String where = StocksContract.StocksEntry.COLUMN_ID + " = ?";
            String[] args = {Integer.toString(id)};
            sqLiteDatabase.update(StocksContract.StocksEntry.TABLE_NAME, contentValues, where, args);

            Toast.makeText(this, "Added to favourites", Toast.LENGTH_SHORT).show();
        } else {
            imageViewDetailsFavourite.setImageResource(R.drawable.empty_star);
            favourite = 0;

            contentValues.put(StocksContract.StocksEntry.COLUMN_FAVOURITE, 0);
            String where = StocksContract.StocksEntry.COLUMN_ID + " = ?";
            String[] args = {Integer.toString(id)};
            sqLiteDatabase.update(StocksContract.StocksEntry.TABLE_NAME, contentValues, where, args);

            Toast.makeText(this, "Deleted from favourites", Toast.LENGTH_SHORT).show();
        }
        sqLiteDatabase.close();
    }

    public void onClickDetailsReturn(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}