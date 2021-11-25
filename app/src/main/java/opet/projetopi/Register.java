package opet.projetopi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private EditText edit_Name, edit_CPF, edit_Email, edit_Endereco, edit_Telefone, edit_Pas;
    private ProgressBar pb;

    String[] mensagens = {"Preencha todos os campos","Cadastro realizado","Falha ao cadastrar"};

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        iniciarComp();
        getSupportActionBar().hide();
    }

    public void verificar(View view){
        Log.i("RegisterAcitivity", "Verificar");
        pb = findViewById(R.id.loading_log);
        pb.setVisibility(View.VISIBLE);

        String text_Name = edit_Name.getText().toString();
        String text_CPF = edit_CPF.getText().toString();
        String text_Email = edit_Email.getText().toString();
        String text_Endereco = edit_Endereco.getText().toString();
        String text_Telefone = edit_Telefone.getText().toString();
        String text_Pas = edit_Pas.getText().toString();

        if (text_Name.isEmpty() || text_CPF.isEmpty() || text_Email.isEmpty() || text_Endereco.isEmpty() || text_Telefone.isEmpty() || text_Pas.isEmpty()) {
            Toast.makeText(Register.this,mensagens[0],Toast.LENGTH_LONG).show();
            pb.setVisibility(View.INVISIBLE);
        }else {
            cadastrar(view);
        }
    }

    public void cadastrar(View view) {

        Log.i("RegisterAcitivity", "Cadastrar");
        String text_Name = edit_Name.getText().toString();
        String text_CPF = edit_CPF.getText().toString();
        String text_Email = edit_Email.getText().toString();
        String text_Endereco = edit_Endereco.getText().toString();
        String text_Telefone = edit_Telefone.getText().toString();
        String text_Pas = edit_Pas.getText().toString();

        mAuth.createUserWithEmailAndPassword(text_Email, text_Pas).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    User user = new User(text_Name, text_CPF, text_Email, text_Endereco, text_Telefone, text_Pas);

                    FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.i("RegisterAcitivity", "isSuccessful");
                                Toast.makeText(Register.this, mensagens[1], Toast.LENGTH_LONG).show();
                                pb.setVisibility(View.INVISIBLE);
                                limparCampos(edit_Name, edit_CPF, edit_Email, edit_Endereco, edit_Telefone, edit_Pas);
                            }
                        }
                    });
                }else {
                    pb.setVisibility(View.INVISIBLE);
                    String erro;
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Digite uma senha com no minimo 6 caracteres";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erro = "Esta conta ja foi cadastrada";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = "Email invalido";
                    } catch (Exception e) {
                        erro = "Erro ao cadastrar";
                    }

                    Toast.makeText(Register.this, erro, Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void login(View view){
        startActivity(new Intent(this, MainActivity.class));
    }

    public void limparCampos(EditText et_name, EditText et_CPF, EditText et_email, EditText et_endereco, EditText et_telefone, EditText et_pas){
        et_name.setText("");
        et_CPF.setText("");
        et_email.setText("");
        et_endereco.setText("");
        et_telefone.setText("");
        et_pas.setText("");
    }

    private void iniciarComp(){
        edit_Name = findViewById(R.id.edit_Name);
        edit_CPF = findViewById(R.id.edit_CPF);
        edit_Email = findViewById(R.id.edit_EmailLog);
        edit_Endereco = findViewById(R.id.edit_Endereco);
        edit_Telefone = findViewById(R.id.edit_Telefone);
        edit_Pas = findViewById(R.id.edit_Pas);
        pb = findViewById(R.id.loading_log);
        Log.i("RegisterAcitivity", "Iniciando os componentes da tela");
    }
}