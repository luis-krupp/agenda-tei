package br.uit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import br.uit.dao.ContatoDAO;
import br.uit.model.Contato;

public class ListaContatos extends AppCompatActivity {

    private ListView listView_contatos;
    private String telefoneLigar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contatos);

        listView_contatos = findViewById(R.id.listView_contatos);

        listView_contatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> lista, View item, int position, long id) {
                Contato contato = (Contato) listView_contatos.getItemAtPosition(position);

                Intent irProFormulario = new Intent(ListaContatos.this, FormularioContato.class);
                irProFormulario.putExtra("contato", contato);
                startActivity(irProFormulario);

            }
        });


        Button novoContato = findViewById(R.id.lista_contatos_novo_contato);

        novoContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vaiProFormulario = new Intent(ListaContatos.this, FormularioContato.class);
                startActivity(vaiProFormulario);
            }
        });

        registerForContextMenu(listView_contatos);

    }

    private void carregaLista() {
        ContatoDAO dao = new ContatoDAO(this);
        List<Contato> contatos = dao.buscaContatos();
        dao.close();
        
        ArrayAdapter<Contato> adapter = new ArrayAdapter<Contato>(this, android.R.layout.simple_list_item_1, contatos);

        listView_contatos.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        final Contato contato = (Contato)listView_contatos.getItemAtPosition(info.position);
        telefoneLigar = contato.getTelefone();

        MenuItem ligar = menu.add("Ligar");
        ligar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (ActivityCompat.checkSelfPermission(ListaContatos.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(ListaContatos.this,
                            new String[]{Manifest.permission.CALL_PHONE}, 80);

                }else{

                    Intent intentLigar = new Intent(Intent.ACTION_CALL);
                    intentLigar.setData(Uri.parse("tel:" + contato.getTelefone()));
                    startActivity(intentLigar);

                }

                return false;
            }
        });

        MenuItem verGoogleMaps = menu.add("Ver no Google Maps");
        Intent intentGoogleMaps = new Intent(Intent.ACTION_VIEW);
        intentGoogleMaps.setData(Uri.parse("geo:0,0?q=" + contato.getEndereco()));
        verGoogleMaps.setIntent(intentGoogleMaps);

        MenuItem apagar = menu.add("Apagar");

        apagar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                ContatoDAO dao = new ContatoDAO(ListaContatos.this);
                dao.apaga(contato);
                dao.close();

                Toast.makeText(ListaContatos.this, contato.getNome() + " foi apagado da Agenda!", Toast.LENGTH_SHORT).show();

                carregaLista();

                return false;
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 80){
            Intent intentLigar = new Intent(Intent.ACTION_CALL);
            intentLigar.setData(Uri.parse("tel:" + telefoneLigar));
            startActivity(intentLigar);
        }

    }
}
