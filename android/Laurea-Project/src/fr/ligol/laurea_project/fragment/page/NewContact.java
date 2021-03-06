package fr.ligol.laurea_project.fragment.page;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import fr.ligol.laurea_project.MainActivity;
import fr.ligol.laurea_project.R;
import fr.ligol.laurea_project.fragment.AFragment;
import fr.ligol.laurea_project.model.Contact;
import fr.ligol.laurea_project.util.RSAUtils;

@SuppressWarnings("deprecation")
public class NewContact extends AFragment {
    private EditText login;
    private EditText hisHash;
    private EditText hisKey;
    private Button pasteHash;
    private Button pasteKey;
    private Button valid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_contact,
                container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        login = (EditText) getView().findViewById(R.id.login);
        hisHash = (EditText) getView().findViewById(R.id.hishash);
        hisKey = (EditText) getView().findViewById(R.id.hiskey);
        pasteHash = (Button) getView().findViewById(R.id.copy);
        pasteKey = (Button) getView().findViewById(R.id.paste);
        valid = (Button) getView().findViewById(R.id.valid);
        SharedPreferences sp = getActivity().getSharedPreferences(
                "laurea_project", Context.MODE_PRIVATE);
        String publicK = sp.getString("pub", null);
        hisHash.setText(publicK);

        valid.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Contact c = new Contact();
                c.setName(login.getText().toString());
                c.setHisPublicKey(hisKey.getText().toString().replace("\n", ""));
                c.setHisHash(RSAUtils.getPublicKeyHash(
                        hisKey.getText().toString()).replace("\n", ""));
                c.save();
                ((MainActivity) getActivity()).resetContact();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });
        pasteHash.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getActivity()
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(hisHash.getText().toString());
            }
        });
        pasteKey.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getActivity()
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                hisKey.setText(cm.getText());
            }
        });
    }
}
