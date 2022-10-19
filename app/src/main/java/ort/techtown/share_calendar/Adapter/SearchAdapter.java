package ort.techtown.share_calendar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ort.techtown.share_calendar.R;

public class SearchAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    private LayoutInflater inflate;
    private ViewHolder viewHolder;

    public SearchAdapter(List<String> list, Context context){
        this.list = list;
        this.context = context;
        this.inflate = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if(list.size() > 7) return 7;
        return list.size();
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null) {
            convertView = inflate.inflate(R.layout.searchview,null);
            viewHolder = new ViewHolder();
            viewHolder.search_item = (TextView) convertView.findViewById(R.id.search_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.search_item.setText(list.get(position));

        return convertView;
    }

    class ViewHolder {
        public TextView search_item;
    }
}
