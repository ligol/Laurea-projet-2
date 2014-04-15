package fr.ligol.laurea_project.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import fr.ligol.laurea_project.model.Message;

public class ChatAdapter extends BaseAdapter {
    protected List<Message> biblio;
    protected LayoutInflater inflater;
    protected Context context;
    private final int[] bgColors = new int[] { 0x30CECECE, 0x30EDEDED };

    public ChatAdapter(Context context, List<Message> biblio) {
        inflater = LayoutInflater.from(context);
        this.biblio = biblio;
        this.context = context;
    }

    private class ViewHolder {
        TextView tvName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(android.R.layout.simple_list_item_1,
                    null);
            holder.tvName = (TextView) convertView
                    .findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        final int colorPosition = position % bgColors.length;
        convertView.setBackgroundColor(bgColors[colorPosition]);
        holder.tvName.setText(biblio.get(position).getMessage());
        return convertView;
    }

    @Override
    public int getCount() {
        return biblio.size();
    }

    @Override
    public Message getItem(int arg0) {
        return biblio.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    public void update(List<Message> list) {
        biblio.clear();
        biblio.addAll(list);
    }
}
