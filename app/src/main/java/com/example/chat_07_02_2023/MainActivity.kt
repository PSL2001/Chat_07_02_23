package com.example.chat_07_02_2023

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.chat_07_02_2023.databinding.ActivityMainBinding
import com.example.chat_07_02_2023.prefs.Prefs
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {
    lateinit var prefs: Prefs
    lateinit var binding: ActivityMainBinding

    private val responseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val credenciales = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credenciales).addOnCompleteListener {
                        if (it.isSuccessful) {
                            prefs.saveEmail(account.email ?: "")
                            goToChat()
                        }
                    }
                }
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = Prefs(this)
        comprobarSesion()
        setListeners()
    }

    private fun comprobarSesion() {
        if (prefs.getEmail() != null) {
            goToChat()
        }
    }

    private fun goToChat() {
        startActivity(Intent(this, ChatActivity::class.java))
    }

    private fun setListeners() {
        binding.googleButton.setOnClickListener { googleLogin() }
    }

    private fun googleLogin() {
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("269035647166-h198g5r5oe44ro66pjvj90d23dqs92hm.apps.googleusercontent.com")
            .requestEmail()
            .build()

        val googleClient = GoogleSignIn.getClient(this, googleConf)
        googleClient.signOut()
        responseLauncher.launch(googleClient.signInIntent)
    }

}