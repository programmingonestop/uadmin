package com.kenyadevelopers.uadmin;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecentFragment extends Fragment implements UnganishwaActivity.OnUploadItemClicked
{


    public static final int GET_IMAGE_REQUEST=1;
    public static final  String SEXY_HOOKUP="sexy_hookup";
    public static final int REQUEST_EDIT_ENTRY=2;
    private DatabaseReference mDatabaseReference;
    private List<Recent> mRecentList;
    private RecyclerView mRecentsRecyclerView;
    private StorageReference mStorageReference;
    private String name;
    private RecentAdapter recentAdapter;
    private Uri imageUri;
    private StorageReference fileReference;
    private int pos;
    private ValueEventListener mValueEventListener;

    @Override
    public void uploadToDatabase()
    {

        chooseFile();
    }

    private void showDialog() {
        SexyDialogFragment sexyDialogFragment=new SexyDialogFragment();
        sexyDialogFragment.setTargetFragment(this,REQUEST_EDIT_ENTRY);
        sexyDialogFragment.show(getFragmentManager(),SEXY_HOOKUP);
    }

    private void chooseFile()
    {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,GET_IMAGE_REQUEST);
    }

    private class RecentAdapter extends RecyclerView.Adapter<RecentViewHolder>
    {

        public RecentAdapter(List<Recent> bigButtList)
        {
            mRecentList=bigButtList;
        }

        @NonNull
        @Override
        public RecentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
        {
            View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sexy_item,viewGroup,false);
            return new RecentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecentViewHolder recentViewHolder, int i)
        {
            recentViewHolder.bind(i);

        }

        @Override
        public int getItemCount() {
            return mRecentList.size();
        }
    }

    private class RecentViewHolder extends RecyclerView.ViewHolder
    {
        private final ImageView mRecentImageView;
        private final TextView textViewRecent;

        public RecentViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mRecentImageView=(ImageView)itemView.findViewById(R.id.imageview_sexy);
            textViewRecent=(TextView)itemView.findViewById(R.id.textview_sexy);

            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
                {
                    menu.setHeaderTitle("select option");
                    MenuItem delete=menu.add(1,1,1,"delete");
                    delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item)
                        {
                            int itemId=item.getItemId();
                            switch (itemId)
                            {
                                case 1:
                                    StorageReference dref=FirebaseStorage.getInstance().getReferenceFromUrl(mRecentList.get(getAdapterPosition()).downloadUrl);
                                    dref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            String key=mRecentList.get(getAdapterPosition()).getKey();
                                            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("recents").child(key);
                                            databaseReference.removeValue();
                                            Toast.makeText(RecentFragment.this.getContext(), "deleted successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RecentFragment.this.getContext(), "Item not deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    return  true;
                                default:
                                    Toast.makeText(RecentFragment.this.getContext(), "Item not found", Toast.LENGTH_SHORT).show();
                            }

                            return false;
                        }
                    });
                }
            });




            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    if(getAdapterPosition()!=RecyclerView.NO_POSITION)
                    {
                        openWhatsApp(v,  mRecentList.get(getAdapterPosition()).getName());
                    }

                }
            });
        }

        public void bind(int i)
        {
            textViewRecent.setBackgroundColor(getResources().getColor(android.R.color.white));
            textViewRecent.setText(mRecentList.get(i).getName());
            pos=i;
            Picasso.with(getContext()).load(mRecentList.get(i).getDownloadUrl()).fit().centerCrop()
                    .placeholder(getResources().getDrawable(R.drawable.placeholder))
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(mRecentImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                            Picasso.with(getContext()).load(mRecentList.get(pos).getDownloadUrl()).fit().centerCrop()
                                    .placeholder(getResources().getDrawable(R.drawable.placeholder))
                                    .into(mRecentImageView);
                        }
                    });
        }
    }



    public void openWhatsApp(View view,String name)
    {
        WhatsAppCommunicationManager wm=new WhatsAppCommunicationManager();
        wm.openWhatsApp(getContext(),name);
    }



    public RecentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_display, container, false);


        mStorageReference=FirebaseStorage.getInstance().getReference("recents");
        mDatabaseReference=FirebaseDatabase.getInstance().getReference("recents");
        mDatabaseReference.keepSynced(true);
        mRecentsRecyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        mRecentList=new ArrayList<>();
        getValuesFromFirebaseDb();
        mRecentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recentAdapter=new RecentAdapter(mRecentList);
        mRecentsRecyclerView.setAdapter(recentAdapter);
        return view;
    }

    private void getValuesFromFirebaseDb()
    {

        mValueEventListener=mDatabaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                mRecentList.clear();
                for(DataSnapshot postSnapShot:dataSnapshot.getChildren())
                {
                    Recent recent=postSnapShot.getValue(Recent.class);
                    mRecentList.add(recent);
                }
                recentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(getContext(), "reading failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GET_IMAGE_REQUEST&&resultCode==Activity.RESULT_OK&&data!=null&&data.getData()!=null)
        {
            showDialog();
            imageUri=data.getData();

        }
        else if(requestCode==REQUEST_EDIT_ENTRY&&resultCode==Activity.RESULT_OK&&data !=null)
        {
            name=data.getStringExtra(SexyDialogFragment.EXTRA_EDIT_ENTRY);
            if(name==null)
            {
                name="Nairobi";
            }
            fileReference = mStorageReference.child(System.currentTimeMillis()+getFileExtension(imageUri));
            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            Uri myUri=uri;
                            Recent recent=new Recent(myUri.toString().trim(),name);
                            String key=mDatabaseReference.push().getKey();
                            recent.setKey(key);
                            mDatabaseReference.child(key).setValue(recent);

                        }
                    });
                    Toast.makeText(getContext(), "uploaded sucessfully", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "upload failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else
        {
            Toast.makeText(getContext(), getString(R.string.not_picked_image), Toast.LENGTH_SHORT).show();
        }

    }
    private String getFileExtension(Uri uri)
    {
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(getActivity().getContentResolver().getType(uri));
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mDatabaseReference.removeEventListener(mValueEventListener);
    }
}
