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
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private val TAG:String = "LoginActivity"
    private var mGoogleApiClient: GoogleApiClient? = null

    companion object {
        const val SIGN_IN = 100
    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.sign_in_button->signIn()
        }
    }

    private fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(this.mGoogleApiClient)
        startActivityForResult(signInIntent, SIGN_IN)
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Toast.makeText(this, connectionResult.errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sign_in_button.setOnClickListener(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == SIGN_IN){
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if(result.isSuccess){
                Log.d(TAG, "handleSignInResult: " + result.isSuccess)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient)

        if(opr.isDone){
            Log.d(TAG, "Got cached sign-in")
        }else{
            Log.d(TAG, "Didn't Got cached sign-in")
        }
    }


}
