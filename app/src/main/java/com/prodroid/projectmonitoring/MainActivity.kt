package com.prodroid.projectmonitoring

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.btn_sign_in->signIn()
            }
        }
    }

    private var mGoogleApiClient : GoogleApiClient? = null
    private val REQUEST_CODE_SIGN_IN = 1000

    override fun onConnectionFailed(result: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun signIn(){
//        Toast.makeText(this, "Sample11", Toast.LENGTH_LONG).show()
        val intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(intent, REQUEST_CODE_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SIGN_IN){
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess){
                Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
            }else{
                updateUI(null)
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null){
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_sign_in.setOnClickListener(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        this.mGoogleApiClient = GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("users")

        myRef.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.w("Error", "Failed to read value", error.toException())
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.w("Success", "Value is: ${dataSnapshot.value}")

            }
        })
        myRef.setValue("testDahulu")

        val db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder().setTimestampsInSnapshotsEnabled(true).build()
        db.firestoreSettings = settings
        val user = HashMap<String, Any>()
        user["First"] = "Ada"
        user["last"] = "Lovelace"
        user["born"] = 1815

        db.collection("users").add(user)
            .addOnSuccessListener({
                    documentReference -> Log.d("Success", "DocumentSnapshot added with ID: ${documentReference.id}")
                }
            ).addOnFailureListener({
                    exception -> Log.w("Error", "Error Adding Document", exception)
                })
    }
}
