package fr.ligol.laurea_project;

import fr.ligol.laurea_project.fragment.page.ContactList;
import fr.ligol.laurea_project.fragment.page.NewContact;
import fr.ligol.laurea_project.model.Contact;
import fr.ligol.laurea_project.util.RSAUtils;
import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {
    private SocketIO socket;

    @SuppressLint("TrulyRandom")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ContactList()).commit();
            supportInvalidateOptionsMenu();
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
            Log.d("test", RSAUtils.getPrivateKeyHash(getApplicationContext()));
            userInfo.put("id",
                    RSAUtils.getPrivateKeyHash(getApplicationContext()));
            List<String> id = new ArrayList<String>();
            for (Contact c : Contact.listAll(Contact.class)) {
                id.add(c.getHisHash());
            }
            userFollow.put("id", id);
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
        socket.connect(new IOCallback() {
            @Override
            public void onMessage(JSONObject json, IOAcknowledge ack) {
                try {
                    System.out.println("Server said:" + json.toString(2));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(String data, IOAcknowledge ack) {
                System.out.println("Server said: " + data);
            }

            @Override
            public void onError(SocketIOException socketIOException) {
                System.out.println("an Error occured");
                socketIOException.printStackTrace();
            }

            @Override
            public void onDisconnect() {
                System.out.println("Connection terminated.");
            }

            @Override
            public void onConnect() {
                System.out.println("Connection established");
            }

            @Override
            public void on(String event, IOAcknowledge ack, Object... args) {
                System.out.println("Server triggered event '" + event + "'");
            }
        });

        socket.emit("userInfo", userInfo.toString());
        socket.emit("userFollow", userFollow.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
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
                supportInvalidateOptionsMenu();
            }
            return true;
        case R.id.add_contact:
            supportInvalidateOptionsMenu();
            FragmentTransaction ft = getSupportFragmentManager()
                    .beginTransaction();
            ft.replace(R.id.container, new NewContact());
            ft.addToBackStack(null);
            ft.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            menu.findItem(R.id.add_contact).setVisible(true);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            menu.findItem(R.id.add_contact).setVisible(false);
        }

        return true;
    }
}
