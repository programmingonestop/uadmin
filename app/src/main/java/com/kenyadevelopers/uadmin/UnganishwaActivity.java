package com.kenyadevelopers.uadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class UnganishwaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private Fragment mfrgmt;
    private FragmentManager fm;
    private static final String TAG="Unganishwa";
    private WhatsAppCommunicationManager whatsappman;
    private Intent shareIntent;
    private Intent intentSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unganishwa);
        shareIntent=new Intent(Intent.ACTION_SEND);
        intentSignUp=new Intent(this,EmailPasswordActivity.class);
        whatsappman=new WhatsAppCommunicationManager();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fm = getSupportFragmentManager();
        if(mfrgmt==null)
        {
            mfrgmt=new ChicksFragment();
        }
        putFragmentOnScreen(mfrgmt);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void putFragmentOnScreen(Fragment frgmt)
    {
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if(fragment==null)
        {
            fm.beginTransaction().add(R.id.fragment_container,frgmt).commit();
        }
        else{
            fm.beginTransaction().replace(R.id.fragment_container,frgmt).commit();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_chick)
        {
            mfrgmt=new ChicksFragment();
            putFragmentOnScreen(mfrgmt);
        } else if (id == R.id.nav_dude) {
            mfrgmt=new DudesFragment();
            putFragmentOnScreen(mfrgmt);
        }
        else if (id == R.id.nav_upload)
        {
           if(mfrgmt!=null)
           {
               OnUploadItemClicked ouc=(OnUploadItemClicked)mfrgmt;
               ouc.uploadToDatabase();
           }

        } else if (id == R.id.nav_recent)
        {
            mfrgmt=new RecentFragment();
            putFragmentOnScreen(mfrgmt);
        } else if (id == R.id.nav_share)
        {
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,"I like Unganishwa App.It lets you get connected with friends.You can download it on playstore");
            startActivity(shareIntent);
        } else if (id == R.id.nav_whatsapp_admin)
        {
            whatsappman.openWhatsApp(UnganishwaActivity.this," this blog");
        }
        else if(id==R.id.nav_sign_in)
        {
            startActivity(intentSignUp);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    interface OnUploadItemClicked
    {
        void uploadToDatabase();
    }
}
