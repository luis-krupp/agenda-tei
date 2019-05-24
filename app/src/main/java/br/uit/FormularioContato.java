package br.uit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import br.uit.dao.ContatoDAO;
import br.uit.helper.FormularioHelper;
import br.uit.model.Contato;

public class FormularioContato extends AppCompatActivity {

    private FormularioHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_contato);

        helper = new FormularioHelper(this);

        Intent intent = getIntent();
        Contato contato = (Contato)intent.getSerializableExtra("contato");

        if (contato != null){
            helper.preencheFormulario(contato);
        }

        Button botaoSalvar = findViewById(R.id.formulario_contato_salvar);

        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contato c = helper.pegaContato();
                ContatoDAO dao = new ContatoDAO(FormularioContato.this);

                if (c.getId() != null){

                    if (validaContato(c)){
                        dao.altera(c);
                        Toast.makeText(FormularioContato.this, c.getNome() + " foi atualizado na Agenda!", Toast.LENGTH_SHORT).show();
                        dao.close();
                        finish();
                    }

                }else{

                    if (validaContato(c)){
                        dao.insere(c);
                        Toast.makeText(FormularioContato.this, c.getNome() + " foi salvo na Agenda!", Toast.LENGTH_SHORT).show();
                        dao.close();
                        finish();
                    }
                }


            }
        });

    }

    private boolean validaContato(Contato c){

        if (c.getNome().trim().equals("")){
            Toast.makeText(this, "Preencha o campo Nome", Toast.LENGTH_SHORT).show();
            return false;
        }else if (c.getEndereco().trim().equals("")){
            Toast.makeText(this, "Preencha o campo Endereço", Toast.LENGTH_SHORT).show();
            return false;
        }else if (c.getTelefone().trim().equals("")){
            Toast.makeText(this, "Preencha o campo Telefone", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }

    }

}
