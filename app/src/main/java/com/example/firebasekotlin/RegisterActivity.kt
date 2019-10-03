package com.example.firebasekotlin

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnKayitOl.setOnClickListener {

            if (etMail.text.isNotEmpty() && etSifre.text.isNotEmpty() && etSifreTekrar.text.isNotEmpty()) {


                if (etSifre.text.toString().equals(etSifreTekrar.text.toString())) {

                    yeniUyeKayıt(etMail.text.toString(), etSifre.text.toString())

                } else {

                    Toast.makeText(this, "Şifreler aynı değil", Toast.LENGTH_SHORT).show()
                }

            } else {

                Toast.makeText(this, "Boş alanları doldurunuz", Toast.LENGTH_SHORT).show()
            }


        }

    }

    private fun yeniUyeKayıt(mail: String, sifre: String) {

        progressBarGoster()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail, sifre)
            .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                override fun onComplete(p0: Task<AuthResult>) {

                    if (p0.isSuccessful) {

                        Toast.makeText(
                            this@RegisterActivity,
                            "Üye Kaydedildi" + FirebaseAuth.getInstance().currentUser?.uid,
                            Toast.LENGTH_SHORT
                        )
                            .show()

                        onayMailiGonder()
                        FirebaseAuth.getInstance().signOut()

                    } else {

                        Toast.makeText(
                            this@RegisterActivity,
                            "Üye Keydedilirken Sorun oluştu" + p0.exception?.message,
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    }


                }


            })
        progressBarGizle()

    }

    private fun onayMailiGonder() {

        var kullanıcı = FirebaseAuth.getInstance().currentUser
        if (kullanıcı != null) {

            kullanıcı.sendEmailVerification()
                .addOnCompleteListener(object : OnCompleteListener<Void> {
                    override fun onComplete(p0: Task<Void>) {

                        if (p0.isSuccessful) {

                            Toast.makeText(
                                this@RegisterActivity,
                                "Mail kutunuzu kontrol edin ",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {

                            Toast.makeText(
                                this@RegisterActivity,
                                "Mail gönderilirken sorun oluştu",
                                Toast.LENGTH_SHORT
                            )


                        }
                    }


                })

        }
    }

    private fun progressBarGoster() {
        progressBar.visibility = View.VISIBLE

    }

    private fun progressBarGizle() {

        progressBar.visibility = View.INVISIBLE
    }


}



