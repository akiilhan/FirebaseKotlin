package com.example.firebasekotlin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

     lateinit var mAuthStateListener:FirebaseAuth.AuthStateListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initMyAuthStateListener()

        tvKayıtOl.setOnClickListener {

            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        tvMailTekrar.setOnClickListener {
            var dialogGoster=OnayMailTekrarFragment()
            dialogGoster.show(supportFragmentManager,"gosterdialog")
        }

        btnGirisYap.setOnClickListener {


            if (etMailLogin.text.isNotEmpty() && etPasswordLogin.text.isNotEmpty()) {

                progressBarGoster()
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    etMailLogin.text.toString(),
                    etPasswordLogin.text.toString()
                ).addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                    override fun onComplete(p0: Task<AuthResult>) {

                        if (p0.isSuccessful) {

                            progressBarGizle()



                            if (!p0.result?.user!!.isEmailVerified){
                                FirebaseAuth.getInstance().signOut()

                            }
                            /**Toast.makeText(
                                this@LoginActivity,
                                "Başarılı giriş" + FirebaseAuth.getInstance().currentUser?.email,
                                Toast.LENGTH_SHORT
                            )
                                .show()**/


                        } else {
                            progressBarGizle()

                            Toast.makeText(
                                this@LoginActivity,
                                "Hatalı giriş " + p0.exception?.message,
                                Toast.LENGTH_SHORT
                            ).show()


                        }
                    }


                })


            } else {
                Toast.makeText(this@LoginActivity, "Boş alanları doldurunuz", Toast.LENGTH_SHORT)
                    .show()
            }

        }

    }

    private fun progressBarGoster() {
        progressBar2.visibility = View.VISIBLE

    }

    private fun progressBarGizle() {

        progressBar2.visibility = View.INVISIBLE
    }

    private fun initMyAuthStateListener(){
        mAuthStateListener=object : FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {

                var kullanıcı=p0.currentUser
                if (kullanıcı!=null){

                    if (kullanıcı.isEmailVerified){

                        Toast.makeText(this@LoginActivity, "Mail Onaylanmıştır.Giriş Yapın.", Toast.LENGTH_SHORT)
                            .show()
                        var intent=Intent(this@LoginActivity,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{

                        Toast.makeText(this@LoginActivity, "Mail Adresinizi Onaylayın.", Toast.LENGTH_SHORT)
                            .show()



                    }
                }


            }


        }

    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener)
    }
}
