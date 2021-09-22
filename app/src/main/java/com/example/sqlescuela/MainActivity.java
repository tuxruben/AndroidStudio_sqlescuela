package com.example.sqlescuela;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    private EditText et1, et2, et3, et4;
    private TextView resultado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        et1 = (EditText) findViewById(R.id.editText1);
        et2 = (EditText) findViewById(R.id.editText2);
        et3 = (EditText) findViewById(R.id.editText3);
        et4 = (EditText) findViewById(R.id.editText4);
        resultado = (TextView) findViewById(R.id.tabla);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alta();
                Snackbar.make(view, "Registro almacenado", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void alta()
    {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String clave = et1.getText().toString();
        String nombre = et2.getText().toString();
        String materia = et3.getText().toString();
        String calificacion = et4.getText().toString();
        ContentValues registro = new ContentValues();
        registro.put("clave", clave);
        registro.put("nombre", nombre);
        registro.put("materia", materia);
        registro.put("calificacion", calificacion);
        bd.insert("alumnos", null, registro);
        bd.close();
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        resultado.setText("");
        Toast.makeText(this, "Se cargaron los datos de la persona",
                Toast.LENGTH_SHORT).show();
    }
    public void baja(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String clave = et1.getText().toString();
        int cant = bd.delete("alumnos", "clave=" + clave, null);
        bd.close();
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        if (cant == 1)
            Toast.makeText(this, "Se borró la persona con dicho documento",
                    Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "No existe una persona con dicho documento",
                    Toast.LENGTH_SHORT).show();
    }
    public void mostrarTabla(){
        resultado.setText("");
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery(
                "select * from alumnos", null);
        resultado.append(" \n CLAVE - NOMBRE - MATERIA - CALIFICACIÓN"+"\n");
        if (fila.moveToFirst()) {
            do{
                String cod = fila.getString(0);
                String nom = fila.getString(1);
                String mat = fila.getString(2);
                String cal = fila.getString(3);
                resultado.append(" " + cod + " - " + nom + " - " + mat + " - " + cal + "\n");
            } while(fila.moveToNext());
        } else
            Toast.makeText(this, "No existe una persona con dicha clave",
                    Toast.LENGTH_SHORT).show();
        bd.close();
    }
    public void modificar(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String clave = et1.getText().toString();
        String nombre = et2.getText().toString();
        String materia = et3.getText().toString();
        String calificacion = et4.getText().toString();
        ContentValues registro = new ContentValues();
        registro.put("nombre", nombre);
        registro.put("materia", materia);
        registro.put("calificacion", calificacion);
        int cant = bd.update("alumnos", registro, "clave=" + clave, null);
        bd.close();
        if (cant == 1)
            Toast.makeText(this, "se modificaron los datos", Toast.LENGTH_SHORT)
                    .show();
        else
            Toast.makeText(this, "no existe una persona con dicho documento",
                    Toast.LENGTH_SHORT).show();
    }
    public void consultaID(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        String clave = et1.getText().toString();
        Cursor fila = bd.rawQuery(
                "select nombre,materia,calificacion from alumnos where clave=" + clave, null);
        if (fila.moveToFirst()) {
            et2.setText(fila.getString(0));
            et3.setText(fila.getString(1));
            et4.setText(fila.getString(2));
        } else
            Toast.makeText(this, "No existe una persona con dicha clave",
                    Toast.LENGTH_SHORT).show();
        bd.close();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.alta) {
            alta();
            return true;
        }
        if (id == R.id.baja) {
            baja();
            return true;
        }
        if (id == R.id.modificar) {
            modificar();
            return true;
        }
        if (id == R.id.consulta) {
            consultaID();
            return true;
        }
        if (id == R.id.mostrar) {
            mostrarTabla();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}