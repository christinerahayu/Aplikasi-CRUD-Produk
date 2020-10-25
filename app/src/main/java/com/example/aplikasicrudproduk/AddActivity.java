package com.example.aplikasicrudproduk;

import android.app.DatePickerDialog;
import android.content.ContentValues;
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

public class AddActivity extends AppCompatActivity {

    DBHelper helper;
    EditText KodeProduk, NamaProduk, Harga, Jumlah, TglMasuk;
    Spinner SpJP;
    long id;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        helper = new DBHelper(this);

        id = getIntent().getLongExtra(DBHelper.row_id, 0);

        KodeProduk = (EditText)findViewById(R.id.KodeProduk_Add);
        NamaProduk = (EditText)findViewById(R.id.NamaProduk_Add);
        Harga = (EditText)findViewById(R.id.Harga_Add);
        Jumlah = (EditText)findViewById(R.id.Jumlah_Add);
        TglMasuk = (EditText)findViewById(R.id.TglMasuk_Add);
        SpJP = (Spinner)findViewById(R.id.spJP_Add);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        TglMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_add:
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
                    Toast.makeText(AddActivity.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else{
                    helper.insertData(values);
                    Toast.makeText(AddActivity.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
