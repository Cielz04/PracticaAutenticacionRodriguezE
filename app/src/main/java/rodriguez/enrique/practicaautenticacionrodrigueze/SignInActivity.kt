package rodriguez.enrique.practicaautenticacionrodrgueze

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var errorTv: TextView
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()
        email = findViewById(R.id.etrEmail)
        password = findViewById(R.id.etrPassword)
        confirmPassword = findViewById(R.id.etrConfirmPassword)
        errorTv = findViewById(R.id.tvrError)
        registerButton = findViewById(R.id.btnRegister)

        errorTv.visibility = View.INVISIBLE

        registerButton.setOnClickListener {
            if (email.text.isEmpty() || password.text.isEmpty() || confirmPassword.text.isEmpty()) {
                errorTv.text = "Todos los campos son obligatorios"
                errorTv.visibility = View.VISIBLE
            } else if (password.text.toString() != confirmPassword.text.toString()) {
                errorTv.text = "Las contraseñas no coinciden"
                errorTv.visibility = View.VISIBLE
            } else {
                errorTv.visibility = View.INVISIBLE
                registerUser(email.text.toString(), password.text.toString())
            }
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("SignInActivity", "createUserWithEmail:success")
                    startActivity(Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                    finish()
                } else {
                    Log.w("SignInActivity", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Registro fallido: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
