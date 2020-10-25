package com.example.aplikasicrudproduk;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {
    DBHelper helper;
    EditText KodeProduk, NamaProduk, Harga, Jumlah, TglMasuk;
    Spinner SpJP;
    long id;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        helper = new DBHelper(this);

        id = getIntent().getLongExtra(DBHelper.row_id, 0);


        KodeProduk = (EditText)findViewById(R.id.KodeProduk_Edit);
        NamaProduk = (EditText)findViewById(R.id.NamaProduk_Edit);
        Harga = (EditText)findViewById(R.id.Harga_Edit);
        Jumlah = (EditText)findViewById(R.id.Jumlah_Edit);
        TglMasuk = (EditText)findViewById(R.id.TglMasuk_Edit);
        SpJP = (Spinner)findViewById(R.id.spJP_Edit);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        TglMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
        getData();
    }

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, dayOfMonth);
                TglMasuk.setText(dateFormatter.format(newDate.getTime()));
            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void getData(){
        Cursor cursor = helper.oneData(id);
        if(cursor.moveToFirst()){
            String kodeProduk = cursor.getString(cursor.getColumnIndex(DBHelper.row_kodeProduk));
            String namaProduk = cursor.getString(cursor.getColumnIndex(DBHelper.row_namaProduk));
            String jp = cursor.getString(cursor.getColumnIndex(DBHelper.row_jp));
            String harga = cursor.getString(cursor.getColumnIndex(DBHelper.row_harga));
            String jumlah = cursor.getString(cursor.getColumnIndex(DBHelper.row_jumlah));
            String tglMasuk = cursor.getString(cursor.getColumnIndex(DBHelper.row_tanggalMasuk));

            KodeProduk.setText(kodeProduk);
            NamaProduk.setText(namaProduk);

            if (jp.equals("Baju")){
                SpJP.setSelection(0);
            }else if(jp.equals("Celana")){
                SpJP.setSelection(1);
            }

            Harga.setText(harga);
            Jumlah.setText(jumlah);
            TglMasuk.setText(tglMasuk);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_edit:
                String kodeProduk = KodeProduk.getText().toString().trim();
                String namaProduk = NamaProduk.getText().toString().trim();
                String jp = SpJP.getSelectedItem().toString().trim();
                String harga = Harga.getText().toString().trim();
                String jumlah = Jumlah.getText().toString().trim();
                String tglMasuk = TglMasuk.getText().toString().trim();

                ContentValues values = new ContentValues();
                values.put(DBHelper.row_kodeProduk, kodeProduk);
                values.put(DBHelper.row_namaProduk, namaProduk);
                values.put(DBHelper.row_jp, jp);
                values.put(DBHelper.row_harga, harga);
                values.put(DBHelper.row_jumlah, jumlah);
                values.put(DBHelper.row_tanggalMasuk, tglMasuk);


                if (kodeProduk.equals("") || namaProduk.equals("") || harga.equals("") || jumlah.equals("") || tglMasuk.equals("")){
                    Toast.makeText(EditActivity.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT);
                }else{
                    helper.updateData(values, id);
                    Toast.makeText(EditActivity.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
        switch (item.getItemId()){
            case R.id.delete_edit:
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                builder.setMessage("Data ini akan dihapus.");
                builder.setCancelable(true);
                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        helper.deleteData(id);
                        Toast.makeText(EditActivity.this, "Data Terhapus", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
}

