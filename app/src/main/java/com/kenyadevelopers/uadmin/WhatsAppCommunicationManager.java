package com.kenyadevelopers.uadmin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WhatsAppCommunicationManager
{
    private String text;
    String toNumber;
    // Replace with mobile phone number without +Sign or leading zeros, but with country code
    //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.

    public void openWhatsApp(Context context, String message)
    {

       DatabaseReference mDatabaseRef= FirebaseDatabase.getInstance().getReference("admin").child("number");
        mDatabaseRef.keepSynced(true);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String number=dataSnapshot.getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        try {
            if(toNumber==null)
            {
                toNumber="254778968420";
            }


                text = context.getResources().getString(R.string.whatsapp_message)+" "+message;// Replace with your message.
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
                context.startActivity(intent);

        }
        catch (Exception e){
            e.printStackTrace();

        }
        finally {

        }
    }
}
