package opet.projetopi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText edit_Email, edit_Pas;
    private Button bt_log, bt_cad;
    private ProgressBar pb;
    String[] mensagens = {"Preencha todos os campos","Falha ao cadastrar"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciarComp();

        getSupportActionBar().hide();

    }

    private void iniciarComp(){
        edit_Email = findViewById(R.id.edit_EmailLog);
        edit_Pas = findViewById(R.id.edit_Pas);
        bt_log = findViewById(R.id.btn_login);
        bt_cad = findViewById(R.id.btn_itnCad);
        pb = findViewById(R.id.loading_log);
    }

    public void intentCad(View view){
        startActivity(new Intent(this,Register.class));
    }

    public void verify(View view){
        Log.i("TAG", "Verificando");
        String email = edit_Email.getText().toString();
        String pas = edit_Pas.getText().toString();

        if(email.isEmpty() || pas.isEmpty()){
            Log.i("TAG", "Campo vazio");
            Toast.makeText(MainActivity.this, mensagens[0], Toast.LENGTH_LONG).show();
        }else{
            Log.i("TAG", "Campo com conteudo");
            auth(view);
        }
    }

    public void auth(View view){
        Log.i("TAG", "Testando Auth");
        String email = edit_Email.getText().toString();
        String pas = edit_Pas.getText().toString();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pas).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    pb.setVisibility(View.VISIBLE);
                    Log.i("TAG", "Sucesso");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                           telaPerfil();
                        }
                    }, 3000);
                }else {
                    String erro;
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        erro = "Erro ao logar";
                    }

                    Toast.makeText(MainActivity.this, erro, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser userLogado = FirebaseAuth.getInstance().getCurrentUser();

        if(userLogado != null){
            telaPerfil();
        }
    }

    public void telaPerfil(){
        startActivity(new Intent(this, Perfil.class));
        finish();
    }
}