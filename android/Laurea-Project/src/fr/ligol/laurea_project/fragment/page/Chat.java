package fr.ligol.laurea_project.fragment.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import fr.ligol.laurea_project.R;
import fr.ligol.laurea_project.adapter.ChatAdapter;
import fr.ligol.laurea_project.fragment.AListFragment;
import fr.ligol.laurea_project.model.Contact;

public class Chat extends AListFragment {
    private Contact contact;
    private Button send;
    private EditText message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container,
                false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contact = Contact.findById(Contact.class, getArguments()
                .getLong("name"));
        setListAdapter(new ChatAdapter(getActivity(), contact.getMessages()));
        send = (Button) getView().findViewById(R.id.send);
        message = (EditText) getView().findViewById(R.id.message);
    }
}
