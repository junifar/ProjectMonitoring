package com.prodroid.projectmonitoring

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("use11r")

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
