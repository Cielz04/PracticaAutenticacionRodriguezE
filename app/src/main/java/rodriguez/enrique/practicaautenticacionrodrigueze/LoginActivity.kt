package rodriguez.enrique.practicaautenticacionrodrgueze

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var errorTv: TextView
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth
        email = findViewById(R.id.etEmail)
        password = findViewById(R.id.etPassword)
        errorTv = findViewById(R.id.tvError)
        loginButton = findViewById(R.id.btnLogin)
        registerButton = findViewById(R.id.btnGoRegister)

        errorTv.visibility = View.INVISIBLE

        loginButton.setOnClickListener {
            if (email.text.isEmpty() || password.text.isEmpty()) {
                showError("Todos los campos son obligatorios", true)
            } else {
                login(email.text.toString(), password.text.toString())
            }
        }

        registerButton.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            goToMain(currentUser)
        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    showError(visible = false)
                    goToMain(user!!)
                } else {
                    showError("Usuario y/o contraseña incorrectos", true)
                }
            }
    }

    private fun goToMain(user: FirebaseUser) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("user", user.email)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    private fun showError(text: String = "", visible: Boolean) {
        errorTv.text = text
        errorTv.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }
}