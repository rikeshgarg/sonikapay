package com.codunite.commonutility.customfont;

import android.content.Context;
import android.graphics.Typeface;

import com.codunite.sonikapay.R;
import com.codunite.commonutility.GlobalVariables;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.Toolbar;

import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;


public class FontUtils {
    public FontUtils() {}

    public static void setFont(ViewGroup group, Typeface font) {
        Context context = group.getContext();
        Typeface fontBold = Typeface.createFromAsset(context.getAssets(), GlobalVariables.CUSTOMFONTNAMEBOLD);
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof EditText || v instanceof Button) {
                if (((TextView) v).getTypeface().getStyle() == Typeface.BOLD ) {
                    ((TextView) v).setTypeface(fontBold);
                }else {
                    ((TextView) v).setTypeface(font);
                }
            } else if (v instanceof EditText || v instanceof Button) {
                if (((EditText) v).getTypeface().getStyle() == Typeface.BOLD ) {
                    ((EditText) v).setTypeface(fontBold);
                }else{
                    ((EditText) v).setTypeface(font);
                }
            } else if (v instanceof Button) {
                if (((Button) v).getTypeface().getStyle() == Typeface.BOLD ) {
                    ((Button) v).setTypeface(fontBold);
                }else{
                    ((Button) v).setTypeface(font);
                }
            } else if (v instanceof ViewGroup)
                setFont((ViewGroup) v, font);
        }
    }

    public static void applyFontToToolbar(Toolbar toolbar, Typeface typeface) {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                tv.setTypeface(typeface);
                break;
            }
        }
    }

    public void applyFontToMenu(Menu menu, Typeface typeface) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            SubMenu subMenu = menuItem.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem, typeface);
                }
            }
            applyFontToMenuItem(menuItem, typeface);
        }
    }

    public void applyFontToSubMenu(SubMenu menu, Typeface typeface) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            SubMenu subMenu = menuItem.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem, typeface);
                }
            }
            applyFontToMenuItem(menuItem, typeface);
        }
    }

    private void applyFontToMenuItem(MenuItem mi, Typeface typeface) {
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new MenuItemTypefaceSpan("", typeface),
                0, mNewTitle.length(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    public void applyFontToNavigationView(NavigationView navigationView, Typeface typeface) {
        applyFontToMenu(navigationView.getMenu(), typeface);
    }

    public void applyFontToView(View view, Typeface typeface) {
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setTypeface(typeface);
        }
    }

    public void applyFontToRadioGroup(RadioGroup radioGroup, Typeface typeface) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View tabViewChild = radioGroup.getChildAt(i);
            if (tabViewChild instanceof TextView) {
                applyFontToView((TextView) tabViewChild, typeface);
            }
        }
    }

    public void applyFontToTabLayout(TabLayout tabLayout, Typeface typeface) {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();

        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);

            int tabChildsCount = vgTab.getChildCount();

            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    TextView tv = (TextView) tabViewChild;
                    tv.setTypeface(typeface);
                }
            }
        }
    }

    public static void setThemeColor(ViewGroup group, Context svContext, boolean isDarkTheme) {
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof EditText || v instanceof Button) {
                if (isDarkTheme) {
//                    ((TextView) v).setBackgroundResource(R.drawable.back_textviewdark);
                    ((TextView) v).setTextColor(svContext.getResources().getColor(R.color.dark_fontcolortextview));
                } else {
//                    ((TextView) v).setBackgroundResource(R.drawable.back_textviewlight);
                    ((TextView) v).setTextColor(svContext.getResources().getColor(R.color.fontcolortextview));
                }
            } else if (v instanceof EditText || v instanceof Button) {
                if (isDarkTheme) {
//                    ((EditText) v).setBackgroundResource(R.drawable.back_edittextl_dark);
                    ((EditText) v).setTextColor(svContext.getResources().getColor(R.color.dark_fontcoloreditext));
                } else {
//                    ((EditText) v).setBackgroundResource(R.drawable.back_edittext_light);
                    ((EditText) v).setTextColor(svContext.getResources().getColor(R.color.fontcoloreditext));
                }
            } else if (v instanceof Button) {
                if (isDarkTheme) {
                    ((EditText) v).setBackgroundResource(R.drawable.back_button_dark);
                    ((EditText) v).setTextColor(svContext.getResources().getColor(R.color.dark_fontcolorbutton));
                } else {
                    ((EditText) v).setBackgroundResource(R.drawable.back_button_light);
                    ((EditText) v).setTextColor(svContext.getResources().getColor(R.color.fontcolorbutton));
                }
            } else if (v instanceof ViewGroup)
                setThemeColor((ViewGroup) v, svContext, isDarkTheme);
        }
        if (isDarkTheme) {
//            group.setBackgroundResource(R.drawable.back_lay_dark);
            setTheme(svContext, true);
        } else {
//            group.setBackgroundResource(R.drawable.back_lay_light);
            setTheme(svContext, false);
        }
    }

    public static void setTheme(Context con, boolean isDarkTheme) {
        if (isDarkTheme) {
            con.setTheme(R.style.DarkTheme);
        } else {
            con.setTheme(R.style.LightTheme);
        }
    }

}
