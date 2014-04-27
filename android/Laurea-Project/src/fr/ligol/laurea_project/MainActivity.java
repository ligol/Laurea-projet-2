package fr.ligol.laurea_project;

import fr.ligol.laurea_project.fragment.page.ContactList;
import fr.ligol.laurea_project.model.Contact;
import fr.ligol.laurea_project.util.RSAUtils;
import fr.ligol.laurea_project.util.SocketIOCallback;
import io.socket.SocketIO;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {
    private SocketIO socket;
    public List<Contact> contact;
    private final ContactList listFragment = new ContactList();

    @SuppressLint("TrulyRandom")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contact = Contact.listAll(Contact.class);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, listFragment).commit();
        }
        SharedPreferences sp = getSharedPreferences("laurea_project",
                MODE_PRIVATE);
        boolean isPrivateKey = sp.getBoolean("private", false);
        if (isPrivateKey == false) {
            RSAUtils.generatePrivateKey(getApplicationContext());
        }
        try {
            socket = new SocketIO("http://ligol.fr:8080/");
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JSONObject userInfo = new JSONObject();
        JSONObject userFollow = new JSONObject();
        try {
            Log.d("test key00",
                    RSAUtils.getPublicKeyHash(getApplicationContext()));
            userInfo.put("id",
                    RSAUtils.getPublicKeyHash(getApplicationContext()));
            List<String> id = new ArrayList<String>();
            for (Contact c : contact) {
                id.add(c.getHisHash());
            }
            JSONArray a = new JSONArray(id);
            userFollow.put("id", a);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("tet3", socket.toString());
        SocketIOCallback.getInstance().setContext(getApplicationContext());
        socket.connect(SocketIOCallback.getInstance());

        socket.emit("userInfo", userInfo.toString());
        socket.emit("userFollow", userFollow.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        // MenuInflater inflater = getMenuInflater();
        // inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        // Respond to the action bar's Up/Home button
        case android.R.id.home:
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // @Override
    // public boolean onPrepareOptionsMenu(Menu menu) {
    // FragmentManager fm = getSupportFragmentManager();
    // if (fm.getBackStackEntryCount() == 0) {
    // getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    // menu.findItem(R.id.add_contact).setVisible(true);
    // } else {
    // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    // menu.findItem(R.id.add_contact).setVisible(false);
    // }
    //
    // return true;
    // }

    @Override
    public void onBackPressed() {
        supportInvalidateOptionsMenu();
        super.onBackPressed();
    }

    public SocketIO getSocket() {
        return socket;
    }

    public void setSocket(SocketIO socket) {
        this.socket = socket;
    }

    @Override
    public void onDestroy() {
        JSONObject userInfo = new JSONObject();
        try {
            // Log.d("test",
            // RSAUtils.getPublicKeyHash(getApplicationContext()));
            userInfo.put("id",
                    RSAUtils.getPublicKeyHash(getApplicationContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Log.d("test2", userInfo.getString("id"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.d("tet3", socket.toString());
        socket.emit("disconnected", userInfo.toString());
        socket.disconnect();
        super.onDestroy();
    }

    public void resetContact() {
        contact = Contact.listAll(Contact.class);
        JSONObject userFollow = new JSONObject();
        try {
            List<String> id = new ArrayList<String>();
            for (Contact c : contact) {
                id.add(c.getHisHash());
            }
            JSONArray a = new JSONArray(id);
            userFollow.put("id", a);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("userFollow", userFollow.toString());
    }
}
