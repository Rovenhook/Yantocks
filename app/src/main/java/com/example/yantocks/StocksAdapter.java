package com.example.yantocks;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StocksAdapter extends RecyclerView.Adapter<StocksAdapter.StocksViewHolder> {
    private ArrayList<Stock> stocks;

    public StocksAdapter(ArrayList<Stock> stocks) {
        this.stocks = stocks;
    }

    @NonNull
    @Override
    public StocksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_item, parent, false);

        return new StocksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StocksViewHolder holder, int position) {
        Stock stock = stocks.get(position);
        holder.textViewStockFullName.setText(stock.getFullName());
        holder.textViewStockShortName.setText(stock.getShortName());
        holder.textViewStockChangeFlat.setText(stock.getChangeDayFlat());
        holder.textViewStockChangePercent.setText(stock.getChangeDayPercent());

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#f9ecec"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#e0e0eb"));
        }


        if (stock.getChangeDayFlat().trim().charAt(0) == '+') {
            holder.textViewStockChangeFlat.setTextColor(Color.parseColor("#009900"));
            holder.textViewStockChangePercent.setTextColor(Color.parseColor("#009900"));
        } else {
            holder.textViewStockChangeFlat.setTextColor(Color.parseColor("#FF0000"));
            holder.textViewStockChangePercent.setTextColor(Color.parseColor("#FF0000"));
        }

        if (stock.getFavourite() == 0) {
            holder.imageViewFavourite.setImageResource(R.drawable.empty_star);
        } else if (stock.getFavourite() > 0) {
            holder.imageViewFavourite.setImageResource(R.drawable.full_star);
        }
    }

    @Override
    public int getItemCount() {
        return stocks.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class StocksViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewStockFullName;
        private TextView textViewStockShortName;
        private TextView textViewStockChangeFlat;
        private TextView textViewStockChangePercent;
        private ImageView imageViewFavourite;

        public StocksViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), DetailsActivity.class);
                    int i = getAdapterPosition();

                    intent.putExtra("id", stocks.get(i).getId());
                    intent.putExtra("shortName", stocks.get(i).getShortName());
                    intent.putExtra("fullName", stocks.get(i).getFullName());
                    intent.putExtra("country", stocks.get(i).getCountry());
                    intent.putExtra("sector", stocks.get(i).getSector());
                    intent.putExtra("currency", stocks.get(i).getCurrency());
                    intent.putExtra("close", stocks.get(i).getClose());
                    intent.putExtra("high", stocks.get(i).getHigh());
                    intent.putExtra("low", stocks.get(i).getLow());
                    intent.putExtra("changeDayFlat", stocks.get(i).getChangeDayFlat());
                    intent.putExtra("changeDayPercent", stocks.get(i).getChangeDayPercent());
                    intent.putExtra("timeUpdate", stocks.get(i).getTimeUpdate());
                    intent.putExtra("favourite", stocks.get(i).getFavourite());

                    itemView.getContext().startActivity(intent);
                }
            });

            textViewStockFullName = itemView.findViewById(R.id.textViewStockFullName);
            imageViewFavourite = itemView.findViewById(R.id.imageViewStockFavourite);
 //           textViewStockFullName.setText(getAdapterPosition() + "");
            textViewStockShortName = itemView.findViewById(R.id.textViewStockShortName);
            textViewStockChangeFlat = itemView.findViewById(R.id.textViewStockChangeFlat);
            textViewStockChangePercent = itemView.findViewById(R.id.textViewStockChangePercent);


            imageViewFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SQLiteDatabase sqLiteDatabase = new StocksDBHelper(itemView.getContext()).getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    if (stocks.get(getAdapterPosition()).getFavourite() == 0) {
                        imageViewFavourite.setImageResource(R.drawable.full_star);
                        stocks.get(getAdapterPosition()).setFavourite(1);

                        contentValues.put(StocksContract.StocksEntry.COLUMN_FAVOURITE, 1);
                        String where = StocksContract.StocksEntry.COLUMN_ID + " = ?";
                        String[] args = {Integer.toString(stocks.get(getAdapterPosition()).getId())};
                        sqLiteDatabase.update(StocksContract.StocksEntry.TABLE_NAME, contentValues, where, args);

                        Toast.makeText(itemView.getContext(), "Added to favourites", Toast.LENGTH_SHORT).show();
                    } else {    //if (stocks.get(getAdapterPosition()).getFavourite() > 0) {
                        imageViewFavourite.setImageResource(R.drawable.empty_star);
                        stocks.get(getAdapterPosition()).setFavourite(0);

                        contentValues.put(StocksContract.StocksEntry.COLUMN_FAVOURITE, 0);
                        String where = StocksContract.StocksEntry.COLUMN_ID + " = ?";
                        String[] args = {Integer.toString(stocks.get(getAdapterPosition()).getId())};
                        sqLiteDatabase.update(StocksContract.StocksEntry.TABLE_NAME, contentValues, where, args);

                        Toast.makeText(itemView.getContext(), "Deleted from favourites", Toast.LENGTH_SHORT).show();
                    }
                    sqLiteDatabase.close();
                }
            });
        }
    }
}
