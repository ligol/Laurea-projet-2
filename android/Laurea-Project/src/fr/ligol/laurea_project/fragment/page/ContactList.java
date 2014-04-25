package fr.ligol.laurea_project.fragment.page;

import java.net.URLEncoder;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import fr.ligol.laurea_project.MainActivity;
import fr.ligol.laurea_project.R;
import fr.ligol.laurea_project.adapter.ContactAdapter;
import fr.ligol.laurea_project.fragment.AListFragment;
import fr.ligol.laurea_project.listener.OnContactListListener;
import fr.ligol.laurea_project.model.Contact;
import fr.ligol.laurea_project.model.Message;
import fr.ligol.laurea_project.util.SocketIOCallback;

public class ContactList extends AListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(false);
        Log.d("fragment", "activitycreated");
        setHasOptionsMenu(true);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(
                R.string.app_name);
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
        getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                    int arg2, long arg3) {
                final ContactAdapter c = (ContactAdapter) getListAdapter();
                final Contact contact = c.getItem(arg2);
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            List<Message> messages = Message.find(
                                    Message.class, "contact = ?", contact
                                            .getId().toString());
                            for (Message m : messages) {
                                m.delete();
                            }
                            contact.delete();
                            ((MainActivity) getActivity()).resetContact();
                            c.update(((MainActivity) getActivity()).contact);
                            getActivity().runOnUiThread(dataChanged);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            // No button clicked
                            break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(
                        getActivity());
                builder.setMessage("Do you really want to delete this contact?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                return true;
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
