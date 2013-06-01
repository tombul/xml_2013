package fu.berlin.de.webdatabrowser.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import fu.berlin.de.webdatabrowser.R;

public class MenuItem extends RelativeLayout {
    private View view;

    public MenuItem(Context context) {
        this(context, null);
    }

    public MenuItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        view = LayoutInflater.from(context).inflate(R.layout.menuitem, this, true);
        TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.MenuItem);

        for(int i = 0; i < styledAttrs.getIndexCount(); ++i) {
            int attr = styledAttrs.getIndex(i);

            switch(attr) {
                case R.styleable.MenuItem_text:
                    ((TextView) view.findViewById(R.id.menuitem_textview)).setText(styledAttrs.getString(attr));
                    break;

                case R.styleable.MenuItem_drawable:
                    ((ImageView) view.findViewById(R.id.menuitem_imageview)).setImageDrawable(styledAttrs.getDrawable(attr));
                    break;
            }
        }

        styledAttrs.recycle();
    }

    public void disable() {
        ((TextView) view.findViewById(R.id.menuitem_textview)).setText(null);
        ((ImageView) view.findViewById(R.id.menuitem_imageview)).setImageDrawable(null);
        view.setEnabled(false);
    }

    public void setHighlighted(boolean highlighted) {
        view.setBackgroundColor(getResources().getColor(highlighted ?
                R.color.background_menuitem_highlighted : android.R.color.transparent));
    }
}
