package fr.ligol.laurea_project.fragment.page;

import java.net.URLEncoder;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import fr.ligol.laurea_project.MainActivity;
import fr.ligol.laurea_project.R;
import fr.ligol.laurea_project.adapter.ContactAdapter;
import fr.ligol.laurea_project.fragment.AListFragment;
import fr.ligol.laurea_project.listener.OnContactListListener;
import fr.ligol.laurea_project.model.Contact;
import fr.ligol.laurea_project.util.SocketIOCallback;

public class ContactList extends AListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(false);
        Log.d("fragment", "activitycreated");
        setHasOptionsMenu(true);
        super.onActivityCreated(savedInstanceState);
        final MainActivity activity = ((MainActivity) getActivity());
        setListAdapter(new ContactAdapter(getActivity(), activity.contact));
        SocketIOCallback.getInstance().setNewConnectionListener(
                new OnContactListListener() {

                    @SuppressWarnings("deprecation")
                    @Override
                    public void onConnection(String user, boolean state) {
                        Log.d("listener", user + " " + state);
                        for (Contact c : activity.contact) {
                            if (URLEncoder.encode(c.getHisHash()).equals(user) == true) {
                                c.setConnected(state);
                            }
                        }
                        activity.runOnUiThread(dataChanged);
                    }

                    @SuppressWarnings("deprecation")
                    @Override
                    public void onDisconnetion(String user, boolean state) {
                        Log.d("listener", user + " " + state);
                        for (Contact c : activity.contact) {
                            if (URLEncoder.encode(c.getHisHash()).equals(user) == true) {
                                c.setConnected(state);
                            }
                        }
                        activity.runOnUiThread(dataChanged);
                    }

                    @Override
                    public void onMessage() {
                        activity.runOnUiThread(dataChanged);
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        // Respond to the action bar's Up/Home button
        case R.id.add_contact:
            FragmentTransaction ft = getActivity().getSupportFragmentManager()
                    .beginTransaction();
            ft.replace(R.id.container, new NewContact());
            ft.addToBackStack(null);
            ft.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected Runnable dataChanged = new Runnable() {
        @Override
        public void run() {
            ContactAdapter c = (ContactAdapter) getListAdapter();
            c.notifyDataSetChanged();
        }
    };

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ContactAdapter c = (ContactAdapter) getListAdapter();
        Chat chat = new Chat();
        Bundle b = new Bundle();
        b.putLong("id", c.getItem(position).getId());
        chat.setArguments(b);
        FragmentTransaction ft = getActivity().getSupportFragmentManager()
                .beginTransaction();
        ft.replace(R.id.container, chat);
        ft.addToBackStack(null);
        ft.commit();
    }

}
