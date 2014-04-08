package fr.ligol.laurea_project.fragment.page;

import android.os.Bundle;
import android.util.Log;
import fr.ligol.laurea_project.MainActivity;
import fr.ligol.laurea_project.adapter.ContactAdapter;
import fr.ligol.laurea_project.fragment.AListFragment;
import fr.ligol.laurea_project.listener.OnNewConnectionListener;

public class ContactList extends AListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final MainActivity activity = ((MainActivity) getActivity());
        setListAdapter(new ContactAdapter(getActivity(), activity.contact));
        activity.socketIOCallback
                .setNewConnectionListener(new OnNewConnectionListener() {

                    @Override
                    public void newConnection(String user, boolean state) {
                        Log.d("listener", user + " " + state);
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
}
