package fr.ligol.laurea_project.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.ligol.laurea_project.R;
import fr.ligol.laurea_project.model.Contact;

public class ContactAdapter extends BaseAdapter {
    protected List<Contact> biblio;
    protected LayoutInflater inflater;
    protected Context context;
    private final int[] bgColors = new int[] { 0x30CECECE, 0x30EDEDED };

    public ContactAdapter(Context context, List<Contact> biblio) {
        inflater = LayoutInflater.from(context);
        this.biblio = biblio;
        this.context = context;
    }

    private class ViewHolder {
        TextView tvName;
        ImageView imgState;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.contact_item, null);
            holder.tvName = (TextView) convertView.findViewById(R.id.login);
            holder.imgState = (ImageView) convertView.findViewById(R.id.state);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        final int colorPosition = position % bgColors.length;
        convertView.setBackgroundColor(bgColors[colorPosition]);
        holder.tvName.setText(biblio.get(position).getName());
        if (biblio.get(position).isConnected() == true) {
            holder.imgState
                    .setImageResource(android.R.drawable.presence_online);
        } else {
            holder.imgState
                    .setImageResource(android.R.drawable.presence_invisible);
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return biblio.size();
    }

    @Override
    public Contact getItem(int arg0) {
        return biblio.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    public void update(List<Contact> list) {
        biblio.clear();
        biblio.addAll(list);
    }
}
