package fr.ligol.laurea_project.fragment.page;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import fr.ligol.laurea_project.R;
import fr.ligol.laurea_project.fragment.AFragment;
import fr.ligol.laurea_project.model.Contact;

public class NewContact extends AFragment {
    private EditText login;
    private EditText myKey;
    private EditText hisKey;
    private Button copy;
    private Button paste;
    private Button valid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container,
                false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        login = (EditText) getView().findViewById(R.id.login);
        myKey = (EditText) getView().findViewById(R.id.mykey);
        hisKey = (EditText) getView().findViewById(R.id.hiskey);
        copy = (Button) getView().findViewById(R.id.copy);
        paste = (Button) getView().findViewById(R.id.paste);
        valid = (Button) getView().findViewById(R.id.valid);

        myKey.setText("toto");

        valid.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Contact c = new Contact();
                c.setName(login.getText().toString());
                c.setHisPublicKey(hisKey.getText().toString());
                c.setMyPublicKey(myKey.getText().toString());
                c.save();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
                getActivity().supportInvalidateOptionsMenu();
            }
        });
        copy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ClipboardManager cm = (ClipboardManager) getActivity()
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(myKey.getText().toString());
            }
        });
        paste.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                ClipboardManager cm = (ClipboardManager) getActivity()
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                hisKey.setText(cm.getText());
            }
        });
    }
}
