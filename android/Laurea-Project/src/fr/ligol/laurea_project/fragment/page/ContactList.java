package fr.ligol.laurea_project.fragment.page;

import android.os.Bundle;
import fr.ligol.laurea_project.adapter.ContactAdapter;
import fr.ligol.laurea_project.fragment.AListFragment;
import fr.ligol.laurea_project.model.Contact;

public class ContactList extends AListFragment {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(new ContactAdapter(getActivity(),
                Contact.listAll(Contact.class)));
    }
}
