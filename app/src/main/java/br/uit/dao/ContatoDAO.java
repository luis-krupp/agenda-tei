package br.uit.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import br.uit.model.Contato;

public class ContatoDAO extends SQLiteOpenHelper {


    public ContatoDAO(Context context) {
        super(context, "Agenda", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Contatos (id INTEGER PRIMARY KEY, nome TEXT NOT NULL, endereco TEXT NOT NULL, email TEXT, telefone TEXT NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS Contatos";
        db.execSQL(sql);
        onCreate(db);

    }

    public void insere(Contato contato) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = pegaDadosContato(contato);
        db.insert("Contatos", null, dados);
    }

    private ContentValues pegaDadosContato(Contato contato) {
        ContentValues dados = new ContentValues();
        dados.put("nome", contato.getNome());
        dados.put("endereco", contato.getEndereco());
        dados.put("email", contato.getEmail());
        dados.put("telefone", contato.getTelefone());
        return dados;
    }

    public List<Contato> buscaContatos() {
        String sql = "SELECT * FROM Contatos ORDER BY nome ASC;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);

        List<Contato> contatos = new ArrayList<Contato>();

        while(c.moveToNext()){
            Contato contato = new Contato();
            contato.setId(c.getLong(c.getColumnIndex("id")));
            contato.setNome(c.getString(c.getColumnIndex("nome")));
            contato.setEndereco(c.getString(c.getColumnIndex("endereco")));
            contato.setEmail(c.getString(c.getColumnIndex("email")));
            contato.setTelefone(c.getString(c.getColumnIndex("telefone")));
            contatos.add(contato);
        }

        c.close();

        return contatos;

    }

    public void apaga(Contato contato) {
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {contato.getId().toString()};
        db.delete("Contatos", "id = ?", params);
    }

    public void altera(Contato contato) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = pegaDadosContato(contato);
        String[] params = {contato.getId().toString()};
        db.update("Contatos", dados, "id = ?", params);


    }
}
