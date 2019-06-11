package com.juanj.prueba_taller.Actividades;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.juanj.prueba_taller.MainActivity;
import com.juanj.prueba_taller.R;

import java.util.concurrent.TimeUnit;

public class RegistroActivity extends AppCompatActivity {
    private EditText etxtPhone;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText etxtPhoneCode;
    private String mVerificationId;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etxtPhone = findViewById(R.id.etxtPhone);
        etxtPhoneCode = findViewById(R.id.etxtPhoneCode);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Toast.makeText(RegistroActivity.this, "Bienvenido a Taller Onucar", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    public void requestCode(View view) {
        String phoneNumber = "+34"+etxtPhone.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(RegistroActivity.this, "No ha ingresado ningún número de teléfono", Toast.LENGTH_SHORT).show();
            return;

        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, 60, TimeUnit.SECONDS, RegistroActivity.this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        //Called if it is not needed to enter verification code
                        signInWithCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        //incorrect phone number, verification code, emulator, etc.
                        Toast.makeText(RegistroActivity.this, "onVerificationFailed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(e.getMessage());
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        //now the code has been sent, save the verificationId we may need it
                        super.onCodeSent(verificationId, forceResendingToken);

                        mVerificationId = verificationId;
                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(String verificationId) {
                        //called after timeout if onVerificationCompleted has not been called
                        super.onCodeAutoRetrievalTimeOut(verificationId);
                        Toast.makeText(RegistroActivity.this, "onCodeAutoRetrievalTimeOut :" + verificationId, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void signInWithCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegistroActivity.this, "Registrado correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegistroActivity.this, "Error al registrar. Vuelva a intentarlo en unos minutos" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void signIn(View view) {
        String code = etxtPhoneCode.getText().toString();
        if (TextUtils.isEmpty(code)){
            Toast.makeText(RegistroActivity.this, "No se ha registrado aún", Toast.LENGTH_SHORT).show();
            return;

        }

        signInWithCredential(PhoneAuthProvider.getCredential(mVerificationId, code));
    }
}
