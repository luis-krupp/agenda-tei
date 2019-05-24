package br.uit.helper;

import android.widget.EditText;

import br.uit.FormularioContato;
import br.uit.R;
import br.uit.model.Contato;

public class FormularioHelper {

    private final EditText campoNome;
    private final EditText campoEndereco;
    private final EditText campoEmail;
    private final EditText campoTelefone;

    private Contato contato;


    public FormularioHelper(FormularioContato frmContato){

        campoNome = frmContato.findViewById(R.id.formulario_contato_nome);
        campoEndereco = frmContato.findViewById(R.id.formulario_contato_endereco);
        campoEmail = frmContato.findViewById(R.id.formulario_contato_email);
        campoTelefone = frmContato.findViewById(R.id.formulario_contato_telefone);

        contato = new Contato();

    }


    public Contato pegaContato(){
        contato.setNome(campoNome.getText().toString());
        contato.setEndereco(campoEndereco.getText().toString());
        contato.setEmail(campoEmail.getText().toString());
        contato.setTelefone(campoTelefone.getText().toString());

        return contato;

    }

    public void preencheFormulario(Contato contato) {
        campoNome.setText(contato.getNome());
        campoEndereco.setText(contato.getEndereco());
        campoEmail.setText(contato.getEmail());
        campoTelefone.setText(contato.getTelefone());
        this.contato = contato;
    }
}
