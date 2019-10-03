package com.example.firebasekotlin


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_dialog.view.*


class OnayMailTekrarFragment : DialogFragment() {

    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var mContext: FragmentActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view=inflater.inflate(R.layout.fragment_dialog, container, false)
        emailEditText=view.findViewById(R.id.etDialogMail)
        passwordEditText=view.findViewById(R.id.etDialogPassword)
        mContext=activity!!

        var btnIptal=view.findViewById<Button>(R.id.btnDialogIptal)
        btnIptal.setOnClickListener {

            dialog.dismiss()
        }

        var btnGonder=view.findViewById<Button>(R.id.btnDialogGonder)
btnGonder.setOnClickListener {

    if (emailEditText.text.toString().isNotEmpty()&& passwordEditText.text.toString().isNotEmpty()){

        girisYapVeOnayMailiniTekrarGonder(emailEditText.text.toString(),passwordEditText.text.toString())

    }else{
        Toast.makeText(activity,"Boş alanları doldurunuz",Toast.LENGTH_LONG).show()
    }


}

        return view
    }

    private fun girisYapVeOnayMailiniTekrarGonder(email: String, sifre: String) {
        var credential=EmailAuthProvider.getCredential(email,sifre)
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener{task ->
            if (task.isSuccessful){

                onayMailiniTekrarGonder()
                dialog.dismiss()


            }else{
                Toast.makeText(activity,"E mail ya da şifre hatalı.",Toast.LENGTH_LONG).show()


            }


        }

    }

    private fun onayMailiniTekrarGonder() {
        var kullanıcı = FirebaseAuth.getInstance().currentUser
        if (kullanıcı != null) {

            kullanıcı.sendEmailVerification()
                .addOnCompleteListener(object : OnCompleteListener<Void> {
                    override fun onComplete(p0: Task<Void>) {

                        if (p0.isSuccessful) {

                            Toast.makeText(
                                mContext,
                                "Mail kutunuzu kontrol edin ",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {

                            Toast.makeText(
                                mContext,
                                "Mail gönderilirken sorun oluştu",
                                Toast.LENGTH_SHORT
                            )


                        }
                    }


                })

        }
    }


}
