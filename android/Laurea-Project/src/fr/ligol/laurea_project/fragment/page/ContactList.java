package fr.ligol.laurea_project.fragment.page;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
        Log.d("fragment", "activitycreated");
        super.onActivityCreated(savedInstanceState);
        final MainActivity activity = ((MainActivity) getActivity());
        setListAdapter(new ContactAdapter(getActivity(), activity.contact));
        SocketIOCallback.getInstance().setNewConnectionListener(
                new OnContactListListener() {

                    @Override
                    public void newConnection(String user, boolean state) {
                        Log.d("listener", user + " " + state);
                        for (Contact c : activity.contact) {
                            if (c.getHisHash().equals(user) == true) {
                                c.setConnected(state);
                            }
                        }
                        activity.runOnUiThread(dataChanged);
                    }

                    @Override
                    public void newDisconnetion(String user, boolean state) {
                        Log.d("listener", user + " " + state);
                        for (Contact c : activity.contact) {
                            if (c.getHisHash().equals(user) == true) {
                                c.setConnected(state);
                            }
                        }
                        activity.runOnUiThread(dataChanged);
                    }
                });
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
