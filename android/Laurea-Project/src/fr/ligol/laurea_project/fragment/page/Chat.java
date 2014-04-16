package fr.ligol.laurea_project.fragment.page;

import io.socket.SocketIO;

import java.net.URLEncoder;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import fr.ligol.laurea_project.MainActivity;
import fr.ligol.laurea_project.R;
import fr.ligol.laurea_project.adapter.ChatAdapter;
import fr.ligol.laurea_project.fragment.AListFragment;
import fr.ligol.laurea_project.listener.OnContactChatListener;
import fr.ligol.laurea_project.model.Contact;
import fr.ligol.laurea_project.model.Message;
import fr.ligol.laurea_project.util.RSAUtils;
import fr.ligol.laurea_project.util.SocketIOCallback;

public class Chat extends AListFragment {
    private Contact contact;
    private SocketIO socket;
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
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        SocketIOCallback.getInstance().setNewChatListener(
                new OnContactChatListener() {

                    @Override
                    public void onMessage() {
                        Log.d("message", "reload");
                        List<Message> m = Message.find(Message.class,
                                "contact = ?", contact.getId().toString());
                        Message.setAllRead(m);
                        ((ChatAdapter) getListAdapter()).update(m);
                        getActivity().runOnUiThread(dataChanged);
                    }
                });
        contact = Contact.findById(Contact.class, getArguments().getLong("id"));
        socket = ((MainActivity) getActivity()).getSocket();
        List<Message> m = Message.find(Message.class, "contact = ?", contact
                .getId().toString());
        Message.setAllRead(m);
        setListAdapter(new ChatAdapter(getActivity(), m));
        send = (Button) getView().findViewById(R.id.send);
        message = (EditText) getView().findViewById(R.id.message);
        send.setOnClickListener(new OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                Message newMessage = new Message();
                newMessage.setContact(contact);
                newMessage.setMe(true);
                newMessage.setRead(true);
                newMessage.setMessage(message.getText().toString());
                newMessage.save();
                ((ChatAdapter) getListAdapter()).add(newMessage);
                ((ChatAdapter) getListAdapter()).notifyDataSetChanged();
                JSONObject o = new JSONObject();
                try {
                    o.put("id", URLEncoder.encode(contact.getHisHash()));
                    o.put("sender", URLEncoder.encode(RSAUtils
                            .getPublicKeyHash(getActivity())));
                    o.put("content", RSAUtils.encrypt(
                            contact.getHisPublicKey(), message.getText()
                                    .toString()));

                    SharedPreferences sp = getActivity().getSharedPreferences(
                            "laurea_project", Context.MODE_PRIVATE);
                    String publicK = sp.getString("pub", null);
                    String cipher = RSAUtils.encrypt(publicK, message.getText()
                            .toString());
                    Log.d("encrypt1", cipher);
                    Log.d("encrypt3", RSAUtils.decrypt(getActivity(), cipher));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                message.setText("");
                socket.emit("message", o.toString());
            }
        });
    }

    @Override
    public void onDestroy() {
        SocketIOCallback.getInstance().setNewChatListener(null);
        Log.d("destroy", "fragment");
        super.onDestroy();
    }

    protected Runnable dataChanged = new Runnable() {
        @Override
        public void run() {
            ChatAdapter c = (ChatAdapter) getListAdapter();
            c.notifyDataSetChanged();
        }
    };
}
