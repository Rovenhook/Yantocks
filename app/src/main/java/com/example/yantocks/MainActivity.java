package com.example.yantocks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private boolean favourite = false;
    private RecyclerView recyclerViewStocks;
    private ArrayList<Stock> stocks;
    private ArrayList<Stock> favouriteStocks;
    private EditText editTextSearch;
    private final String linkList = "https://fcsapi.com/api-v3/stock/list?country=russia&access_key=EwqlSJpfLI3WhvLCMyOqbyr";
    private final String linkLatest = "https://fcsapi.com/api-v3/stock/latest?country=Russia&access_key=EwqlSJpfLI3WhvLCMyOqbyr";
//    private StocksDBHelper stocksDBHelper;
//    private SQLiteDatabase sqLiteDatabase;
    private StocksAdapter stocksAdapter;
    private StocksAdapter stocksFavouriteAdapter;
    private StocksAdapter stocksSearchAdapter;
    private TextView textViewAllStocks;
    private TextView textViewFavoriteStocks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextSearch = findViewById(R.id.editTextTextSearch);
        textViewAllStocks = findViewById(R.id.textViewAllStocks);
        textViewFavoriteStocks = findViewById(R.id.textViewFavoriteStocks);
        recyclerViewStocks = findViewById(R.id.recyclerViewStocks);
//        stocksDBHelper = new StocksDBHelper(this);
//        sqLiteDatabase = stocksDBHelper.getWritableDatabase();
        try {
            stocks = new FillStocksArrayTask().execute().get();
        } catch (Exception e) {
            Log.i("fatality", "Coundn't get info from async task properly");
        }

        stocksAdapter = new StocksAdapter(stocks);

        //       StocksAdapter stocksAdapter = new StocksAdapter(stocks);
        recyclerViewStocks.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewStocks.setAdapter(stocksAdapter);
    }

    public void onclickCancelSearch(View view) {
        editTextSearch.setText("");
        editTextSearch.clearFocus();
        if (favourite) {
            recyclerViewStocks.setAdapter(stocksFavouriteAdapter);
        } else {
            recyclerViewStocks.setAdapter(stocksAdapter);
        }
    }

    public void onClickChangeStocksAndFavourite(View view) {
        editTextSearch.setText("");
        if (view.equals(textViewAllStocks)) {
            textViewAllStocks.setTypeface(Typeface.DEFAULT_BOLD);
            textViewAllStocks.setTextSize(24);

            textViewFavoriteStocks.setTypeface(Typeface.DEFAULT);
            textViewFavoriteStocks.setTextSize(20);

            recyclerViewStocks.setAdapter(stocksAdapter);
            favourite = false;

        } else if (view == textViewFavoriteStocks) {
            textViewFavoriteStocks.setTypeface(Typeface.DEFAULT_BOLD);
            textViewFavoriteStocks.setTextSize(24);//(R.dimen.textview_focused_label_size);

            textViewAllStocks.setTypeface(Typeface.DEFAULT);
            textViewAllStocks.setTextSize(20);

            setFavouriteAdapterList();
            favourite = true;
        }
    }

    private void setFavouriteAdapterList() {
        favouriteStocks = new ArrayList<>();

        for (Stock s : stocks) {
            if (s.getFavourite() > 0) {
                favouriteStocks.add(s);
            }
        }
        stocksFavouriteAdapter = new StocksAdapter(favouriteStocks);
        recyclerViewStocks.setAdapter(stocksFavouriteAdapter);
    }

    public void onClickSearch(View view) {
        String str = editTextSearch.getText().toString();
        if (!str.isEmpty()) {
            ArrayList<Stock> foundStocks = new ArrayList<>();
            str = str.toLowerCase();
            if (favourite) {
                Log.i("tryout", "About to know favouriteStocks size");
                Log.i("tryout", "favouriteStocks size = " + favouriteStocks.size() + "");
                for (Stock s : favouriteStocks) {
                    if (s.getFullName().toLowerCase().contains(str) || s.getShortName().toLowerCase().contains(str)) {
                        foundStocks.add(s);
                    }
                }
            } else {
                for (Stock s : stocks) {
                    if (s.getFullName().toLowerCase().contains(str) || s.getShortName().toLowerCase().contains(str)) {
                        foundStocks.add(s);
                    }
                }
            }
            stocksSearchAdapter = new StocksAdapter(foundStocks);
            recyclerViewStocks.setAdapter(stocksSearchAdapter);
        }
    }

    private class FillStocksArrayTask extends AsyncTask<Void, Void, ArrayList<Stock>> { //ArrayList<Stock>> {
        StocksDBHelper stocksDBHelper;// = new StocksDBHelper(MainActivity.this);
        SQLiteDatabase sqLiteDatabase;// = stocksDBHelper.getWritableDatabase();
        URL urlList;// = null;
        URL urlLatest;;// = null;
        HttpURLConnection httpURLConnectionList;// = null;
        HttpURLConnection httpURLConnectionLatest;// = null;
        StringBuilder resultList;// = new StringBuilder();
        StringBuilder resultLatest;// = new StringBuilder();
        String list;// = null;
        String latest;// = null;

        @Override
        //       protected ArrayList<Stock> doInBackground(Void... voids) {
        protected ArrayList<Stock> doInBackground(Void... voids) {
            stocksDBHelper = new StocksDBHelper(MainActivity.this);
            sqLiteDatabase = stocksDBHelper.getWritableDatabase();
            ArrayList<Stock> stocksTask = null;
            resultList = new StringBuilder();
            resultLatest = new StringBuilder();

            try {
                urlList = new URL(linkList);
                urlLatest = new URL(linkLatest);
                httpURLConnectionList = (HttpURLConnection) urlList.openConnection();
                httpURLConnectionLatest = (HttpURLConnection) urlLatest.openConnection();
                InputStream inputStreamList = httpURLConnectionList.getInputStream();
                InputStream inputStreamLatest = httpURLConnectionLatest.getInputStream();
                InputStreamReader inputStreamReaderList = new InputStreamReader(inputStreamList);
                InputStreamReader inputStreamReaderLatest = new InputStreamReader(inputStreamLatest);
                BufferedReader bufferedReaderList = new BufferedReader(inputStreamReaderList);
                BufferedReader bufferedReaderLatest = new BufferedReader(inputStreamReaderLatest);

                String lineList = "";
                while ((lineList = bufferedReaderList.readLine()) != null) {
                    resultList.append(lineList);
                }
                String lineLatest = "";
                while ((lineLatest = bufferedReaderLatest.readLine()) != null) {
                    resultLatest.append(lineLatest);
                }
                list = resultList.toString();
                latest = resultLatest.toString();
                if (list == "" || latest == "") {
                    throw new Exception();
                }

                JSONObject jsonMainList = new JSONObject(list);
                JSONObject jsonMainlatest = new JSONObject(latest);
                stocksTask = getArrayFromJSONs(jsonMainList, jsonMainlatest);

            } catch (Exception e) {
                Log.i("tryout", "Getting stocks from DB instead");
                stocksTask = getArrayFromDB();
            } finally {
                if (httpURLConnectionList != null) {
                    httpURLConnectionList.disconnect();
                }
                if (httpURLConnectionLatest != null) {
                    httpURLConnectionLatest.disconnect();
                }
            }// end try block
            sqLiteDatabase.close();
            stocksDBHelper.close();

            return stocksTask;
        }// end doInBackground method

        private void putValuesToDB(ArrayList<Stock> stockList) {
            try {
                Log.i("tryout", "Putting values to DB, stocks.size = " + stockList.size());
                ContentValues contentValues = new ContentValues();
                for (Stock stock : stockList) {
                    contentValues.put(StocksContract.StocksEntry.COLUMN_ID, stock.getId());
                    contentValues.put(StocksContract.StocksEntry.COLUMN_SHORT_NAME, stock.getShortName());
                    contentValues.put(StocksContract.StocksEntry.COLUMN_FULL_NAME, stock.getFullName());
                    contentValues.put(StocksContract.StocksEntry.COLUMN_COUNTRY, stock.getCountry());
                    contentValues.put(StocksContract.StocksEntry.COLUMN_SECTOR, stock.getSector());
                    contentValues.put(StocksContract.StocksEntry.COLUMN_CURRENCY, stock.getCurrency());
                    contentValues.put(StocksContract.StocksEntry.COLUMN_CLOSE, stock.getClose());
                    contentValues.put(StocksContract.StocksEntry.COLUMN_HIGH, stock.getHigh());
                    contentValues.put(StocksContract.StocksEntry.COLUMN_LOW, stock.getLow());
                    contentValues.put(StocksContract.StocksEntry.COLUMN_CHANGE_DAY_FLAT, stock.getChangeDayFlat());
                    contentValues.put(StocksContract.StocksEntry.COLUMN_CHANGE_DAY_PERCENT, stock.getChangeDayPercent());
                    contentValues.put(StocksContract.StocksEntry.COLUMN_TIME_UPDATE, stock.getTimeUpdate());
                    contentValues.put(StocksContract.StocksEntry.COLUMN_FAVOURITE, stock.getFavourite());
                    sqLiteDatabase.insert(StocksContract.StocksEntry.TABLE_NAME, null, contentValues);
                }

            } catch (Exception e) {
                Log.i("tryout", "inside puValuesToDB - exception");
            }
        }

        private ArrayList<Stock> getArrayFromDB() {
            ArrayList<Stock> stocksFromDB = new ArrayList<>();
            Cursor cursor = sqLiteDatabase.query(StocksContract.StocksEntry.TABLE_NAME, null, null,
                    null, null, null, null);

            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(StocksContract.StocksEntry.COLUMN_ID));
                String shortName = cursor.getString(cursor.getColumnIndex(StocksContract.StocksEntry.COLUMN_SHORT_NAME));
                String fullName = cursor.getString(cursor.getColumnIndex(StocksContract.StocksEntry.COLUMN_FULL_NAME));
                String country = cursor.getString(cursor.getColumnIndex(StocksContract.StocksEntry.COLUMN_COUNTRY));
                String sector = cursor.getString(cursor.getColumnIndex(StocksContract.StocksEntry.COLUMN_SECTOR));
                String currency = cursor.getString(cursor.getColumnIndex(StocksContract.StocksEntry.COLUMN_CURRENCY));
                double close = cursor.getDouble(cursor.getColumnIndex(StocksContract.StocksEntry.COLUMN_CLOSE));
                double high = cursor.getDouble(cursor.getColumnIndex(StocksContract.StocksEntry.COLUMN_HIGH));
                double low = cursor.getDouble(cursor.getColumnIndex(StocksContract.StocksEntry.COLUMN_LOW));
                String changeFlat = cursor.getString(cursor.getColumnIndex(StocksContract.StocksEntry.COLUMN_CHANGE_DAY_FLAT));
                String changePercent = cursor.getString(cursor.getColumnIndex(StocksContract.StocksEntry.COLUMN_CHANGE_DAY_PERCENT));
                String timeUpdate = cursor.getString(cursor.getColumnIndex(StocksContract.StocksEntry.COLUMN_TIME_UPDATE));
                int favourite = cursor.getInt(cursor.getColumnIndex(StocksContract.StocksEntry.COLUMN_FAVOURITE));

                Stock stock = new Stock(id, shortName, fullName, country, sector, currency, close,
                        high, low, changeFlat, changePercent, timeUpdate, favourite);
                stocksFromDB.add(stock);
                Log.i("tryout", " Getting array from DB, stocksFromDB.size = " + stocksFromDB.size());
            }
            cursor.close();

            return stocksFromDB;
        }

        private ArrayList<Stock> getArrayFromJSONs(JSONObject jsonMainList, JSONObject jsonMainLatest) {
            ArrayList<Stock> stocksJSON = new ArrayList<>();

            try {
                JSONArray jsonListArray = jsonMainList.getJSONArray("response");
                JSONArray jsonLatestArray = jsonMainLatest.getJSONArray("response");

                for (int i = 0; i < jsonListArray.length(); i++) {
                    int idList = jsonListArray.getJSONObject(i).getInt("id");

                    for (int j = 0; j < jsonLatestArray.length(); j++) {
                        int idLatest = jsonLatestArray.getJSONObject(j).getInt("id");

                        if (idList == idLatest) {
                            String nameList = jsonListArray.getJSONObject(i).getString("name");
                            String shortNameList = jsonListArray.getJSONObject(i).getString("short_name");
                            String countryList = jsonListArray.getJSONObject(i).getString("country");
                            String sectorList = jsonListArray.getJSONObject(i).getString("sector");
                            String currencyList = jsonListArray.getJSONObject(i).getString("ccy");

                            double closeLatest = jsonLatestArray.getJSONObject(j).getInt("c");
                            double highLatest = jsonLatestArray.getJSONObject(j).getInt("h");
                            double lowLatest = jsonLatestArray.getJSONObject(j).getInt("l");
                            String changeFlatLatest = jsonLatestArray.getJSONObject(j).getString("ch");
                            String changePercentLatest = jsonLatestArray.getJSONObject(j).getString("cp");
                            String shortNameLatest = jsonLatestArray.getJSONObject(j).getString("s");
                            String countryLatest = jsonLatestArray.getJSONObject(j).getString("cty");
                            String currencyLatest = jsonLatestArray.getJSONObject(j).getString("ccy");
                            String timeUpdateLatest = jsonLatestArray.getJSONObject(j).getString("tm");

                            String result = idList + " " + nameList + " " + shortNameList + countryList + " " + sectorList + " "
                                    + currencyList + " " + idLatest + " " + closeLatest + " " + highLatest + " " + lowLatest + " "
                                    + changeFlatLatest + " " + changePercentLatest + " " + shortNameLatest + " " + countryLatest + " "
                                    + currencyLatest + " " + timeUpdateLatest + "\n";
                            Log.i("tryout", "Array from JSON " + result);

                            String[] column = {StocksContract.StocksEntry.COLUMN_FAVOURITE};
                            String where = StocksContract.StocksEntry.COLUMN_ID + " = ? ";
                            String[] idToFind = {Integer.toString(idLatest)};
                            Cursor cursor = sqLiteDatabase.query(StocksContract.StocksEntry.TABLE_NAME, column,
                                    where, idToFind, null, null, null);
                            int fav = 0;
                            if (cursor.moveToFirst()) {
                                fav = cursor.getInt(cursor.getColumnIndex(StocksContract.StocksEntry.COLUMN_FAVOURITE));
                            }
                            cursor.close();
                            stocksJSON.add(new Stock(
                                    idList,              //private int id;
                                    shortNameList,       //private String shortName;
                                    nameList,            //private String fullName;
                                    countryList,         //private String country;
                                    sectorList,          //private String sector;
                                    currencyList,        //private String currency;
                                    closeLatest,         //private Double close;
                                    highLatest,          //private Double high;
                                    lowLatest,           //private Double low;
                                    changeFlatLatest,    //private String changeDayFlat;
                                    changePercentLatest, //private String changeDayPercent;
                                    timeUpdateLatest,    //private String timeUpdate;
                                    fav));               //private int favourite;));
                        }
                    }
                }
                Log.i("tryout", "right before putting values to DB");
                putValuesToDB(stocksJSON);
                Log.i("tryout", "after putting values to DB");
            } catch (NullPointerException | JSONException e) {
                Log.i("tryout", "Get array from DB instead of json");
                stocksJSON = getArrayFromDB();
            }
            return stocksJSON;
        }
    }// end FillStocksArrayTask class
}