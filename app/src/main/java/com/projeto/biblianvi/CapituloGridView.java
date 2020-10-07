package com.projeto.biblianvi;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.projeto.biblianvi.biblianvi.R;

/**
 * Created by Ezequiel on 06/05/2017.
 */

public class CapituloGridView extends BaseAdapter {

    private Context mContext;
    private int capMax = 0;
    private Button button;
    private Biblia biblia;
    private ViewGroup viewGroupParent;
    private TextView textViewGridView;
    private BibliaBancoDadosHelper bibliaHelp;
    private Activity activity;

    public CapituloGridView(Context c, Activity activity, BibliaBancoDadosHelper bibliaHelp, int capMax, Biblia b, TextView t) {
        mContext = c;
        this.capMax = capMax;
        biblia = b;
        textViewGridView = t;
        this.bibliaHelp = bibliaHelp;
        this.activity = activity;

    }

    public int getCount() {
        return capMax;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {

        viewGroupParent = parent;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes


        } else {

            button = (Button) convertView;


        }


        button = new Button(mContext);
        button.setTextSize(18);
        button.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        button.setBackgroundColor(mContext.getResources().getColor(R.color.barrasuperiorescuro));
        button.setTextColor(mContext.getResources().getColor(R.color.white));
        //  button.setBackground(mContext.getResources().getDrawable(R.drawable.appthemebutton_btn_default_normal_holo_dark));
        button.setPadding(3, 3, 3, 3);

        if (position < capMax)
            button.setText(Integer.toString(position + 1));

        if (bibliaHelp.getIsChapterLido(biblia.getBooksName(), Integer.toString(position + 1)))
            button.setTextColor(mContext.getResources().getColor(R.color.green));

        /*
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
*/

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button b = (Button) v;

                biblia.setChapter(b.getText().toString());

                int versiculos = bibliaHelp.getQuantidadeVersos(biblia.getBooksName(), biblia.getChapter());

                GridView gridViewVersiculos = (GridView) viewGroupParent;
                gridViewVersiculos.setAdapter(new VersiculoGridView(mContext, bibliaHelp, versiculos, biblia));
                gridViewVersiculos.deferNotifyDataSetChanged();

                textViewGridView.setText(R.string.versiculo);

            }
        });

        return button;
    }


}
