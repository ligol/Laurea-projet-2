package fr.ligol.laurea_project.fragment.page;

import io.socket.SocketIO;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
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
        setHasOptionsMenu(true);
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
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(
                contact.getName());
        socket = ((MainActivity) getActivity()).getSocket();
        List<Message> m = Message.find(Message.class, "contact = ?", contact
                .getId().toString());
        Message.setAllRead(m);
        setListAdapter(new ChatAdapter(getActivity(), m));
        send = (Button) getView().findViewById(R.id.send);
        message = (EditText) getView().findViewById(R.id.message);
        send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("encrypttest", "his|" + contact.getHisPublicKey() + "|");
                SharedPreferences sp = getActivity().getSharedPreferences(
                        "laurea_project", Context.MODE_PRIVATE);
                String publicK = sp.getString("pub", null);
                Log.d("encrypttest", "my|" + publicK + "|");
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
                    o.put("id", contact.getHisHash());
                    o.put("sender", RSAUtils.getPublicKeyHash(getActivity()));
                    o.put("content", RSAUtils.encrypt(
                            contact.getHisPublicKey(), message.getText()
                                    .toString()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                message.setText("");
                socket.emit("message", o.toString());
            }
        });

        getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                    int arg2, long arg3) {
                final ChatAdapter c = (ChatAdapter) getListAdapter();
                final Message message = c.getItem(arg2);
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            message.delete();
                            List<Message> message = Message.find(Message.class,
                                    "contact = ?", contact.getId().toString());
                            c.update(message);
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
                builder.setMessage("Do you really want to delete this message?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                return true;
            }
        });
    }

    @Override
    public void onDestroy() {
        SocketIOCallback.getInstance().setNewChatListener(null);
        Log.d("destroy", "fragment");
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.delete, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        // Respond to the action bar's Up/Home button
        case R.id.delete_message:
            ChatAdapter c = (ChatAdapter) getListAdapter();
            List<Message> message = Message.find(Message.class, "contact = ?",
                    contact.getId().toString());
            for (Message m : message) {
                m.delete();
            }
            c.clear();
            getActivity().runOnUiThread(dataChanged);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected Runnable dataChanged = new Runnable() {
        @Override
        public void run() {
            ChatAdapter c = (ChatAdapter) getListAdapter();
            c.notifyDataSetChanged();
        }
    };
}
