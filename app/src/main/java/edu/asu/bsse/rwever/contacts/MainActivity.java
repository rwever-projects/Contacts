package edu.asu.bsse.rwever.contacts;

/**
 * Created by:
 * @author Rudi Wever mailto: rwever@asu.edu
 * @version May 5, 2017
 *
 * Copyright © 2017 Rudi Wever.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Permission is granted to the instuctor and the University with the right
 * to build and evaluate the software package for the purpose of determining
 * grade and program assessment.
 *
 * No reproduction and distributed copies of the Work or Derivative Works
 * thereof in any medium, with or without modifications, and in Source or
 * Object form are allowed.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener{

    ImageButton back;
    ListView listView;
    SQLController dbcon;
    TextView textViewContactID, textViewContactInitials, textViewContactName, textViewContactLastName, textViewContactCompany;
    TextView appTitle, numContacts, empty,textViewColorHolder;

    String[] sortLabel={"A","Z","First Added"};
    String[] sortLabel2={"↓","↓","↓"};
    String[] sortLabel3={"Z","A","Recently Added"};
    String[] sortLabel4={"First Name","Last Name",""};
    int background_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbcon = new SQLController(this);
        dbcon.open();

        back = (ImageButton) findViewById(R.id.backButton);
        listView = (ListView) findViewById(R.id.myList);
        appTitle = (TextView) findViewById(R.id.textViewAppTitle);
        empty = (TextView) findViewById(R.id.empty);
        back.setVisibility(View.INVISIBLE);

        Cursor cursor = dbcon.readData();
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setPopupBackgroundResource(R.color.black_font);
        spin.setOnItemSelectedListener(this);

        spin.setAdapter(new MyAdapter(this, R.layout.spinner_item, sortLabel));

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                           @Override
                                           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                               switch (position){
                                                   case 0:
                                                       // Attach The Data From DataBase Into ListView Using Cursor Adapter
                                                       Cursor cursor = dbcon.readDataFirstName();

                                                       String[] from = new String[]{DBhelper.CONTACT_ID, DBhelper.CONTACT_INITIALS, DBhelper.CONTACT_NAME, DBhelper.CONTACT_LASTNAME, DBhelper.CONTACT_COMPANY, DBhelper.CONTACT_COLOR};
                                                       int[] to = new int[]{R.id.contact_id, R.id.initials, R.id.contact_name, R.id.textViewDisplayContactLastName, R.id.displayContactCompany, R.id.textViewColorHolder};

                                                       MyCursorAdapter dataAdapter = new MyCursorAdapter(
                                                               MainActivity.this, R.layout.display_contact_row, cursor, from, to, 0);
                                                       dataAdapter.notifyDataSetChanged();
                                                       listView.setAdapter(dataAdapter);
                                                       break;
                                                   case 1:
                                                       // Attach The Data From DataBase Into ListView Using Cursor Adapter
                                                       Cursor cursor1 = dbcon.readDataLastName();

                                                       String[] from1 = new String[]{DBhelper.CONTACT_ID, DBhelper.CONTACT_INITIALS, DBhelper.CONTACT_NAME, DBhelper.CONTACT_LASTNAME, DBhelper.CONTACT_COMPANY, DBhelper.CONTACT_COLOR};
                                                       int[] to1 = new int[]{R.id.contact_id, R.id.initials, R.id.contact_name, R.id.textViewDisplayContactLastName, R.id.displayContactCompany, R.id.textViewColorHolder};

                                                       MyCursorAdapter dataAdapter1 = new MyCursorAdapter(
                                                               MainActivity.this, R.layout.display_contact_row, cursor1, from1, to1, 0);
                                                       dataAdapter1.notifyDataSetChanged();
                                                       listView.setAdapter(dataAdapter1);
                                                       break;
                                                   case 2:
                                                       // Attach The Data From DataBase Into ListView Using Cursor Adapter
                                                       Cursor cursor2 = dbcon.readData();

                                                       String[] from2 = new String[]{DBhelper.CONTACT_ID, DBhelper.CONTACT_INITIALS, DBhelper.CONTACT_NAME, DBhelper.CONTACT_LASTNAME, DBhelper.CONTACT_COMPANY, DBhelper.CONTACT_COLOR};
                                                       int[] to2 = new int[]{R.id.contact_id, R.id.initials, R.id.contact_name, R.id.textViewDisplayContactLastName, R.id.displayContactCompany, R.id.textViewColorHolder};

                                                       MyCursorAdapter dataAdapter2 = new MyCursorAdapter(
                                                               MainActivity.this, R.layout.display_contact_row, cursor2, from2, to2, 0);
                                                       dataAdapter2.notifyDataSetChanged();
                                                       listView.setAdapter(dataAdapter2);
                                                       break;
                                               }

                                           }

                                           @Override
                                           public void onNothingSelected(AdapterView<?> parent) {
                                           }
                                       });

        if (cursor.getCount() == 0) {
            empty.setText(R.string.no_contacts);
        }

        numContacts = (TextView) findViewById(R.id.numberOfContacts);
        numContacts.setText("(" + cursor.getCount() + ")");

        // OnCLickListiner For List Items
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                textViewContactID = (TextView) view.findViewById(R.id.contact_id);
                textViewContactInitials = (TextView) view.findViewById(R.id.initials);
                textViewContactName = (TextView) view.findViewById(R.id.contact_name);
                textViewContactLastName = (TextView) view.findViewById(R.id.textViewDisplayContactLastName);
                textViewContactCompany = (TextView) view.findViewById(R.id.displayContactCompany);

                String contactID_value = textViewContactID.getText().toString();
                String contactInitials_value = textViewContactInitials.getText().toString();
                String contactName_value = textViewContactName.getText().toString();
                String contactLastName_value = textViewContactLastName.getText().toString();
                String contactCompany_value = textViewContactCompany.getText().toString();


                Intent modify_intent = new Intent(getApplicationContext(),
                        Edit_Contact.class);
                modify_intent.putExtra("contactID", contactID_value);
                modify_intent.putExtra("contactInitals", contactInitials_value);
                modify_intent.putExtra("contactName", contactName_value);
                modify_intent.putExtra("contactLastName", contactLastName_value);
                modify_intent.putExtra("contactCompany", contactCompany_value);
                modify_intent.putExtra("contactColor", background_color);
                startActivity(modify_intent);
            }
        });

        //tell my buttons to listen up!
        addListenerOnButton();

    } // MainActivity onCreate end

    /**
     * addListenerOnButton
     * Listens to the buttons of this activity
     */
    public void addListenerOnButton() {
        appTitle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("My Contact App");
                alertDialog.setMessage("Version 1.0\n\nASU SER 423\nAuthor: Rudi Wever\nrwever@asu.edu\nRelease: 4.28.2017");
                alertDialog.setIcon(R.drawable.launcher2);

                alertDialog.show();
            }
        });
    }

    public void addContact(View view) {
        //declare where you intend to go
        Intent add_mem = new Intent(MainActivity.this, Add_Contact.class);
        //now make it happen
        startActivity(add_mem);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("item","item: "+position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d("item","item: ");
    }

    //extend the SimpleCursorAdapter to create a custom class where we
    //can override the getView to change the row colors
    private class MyCursorAdapter extends SimpleCursorAdapter {
        // Asset References
        Typeface centuryGothic = Typeface.createFromAsset(getAssets(), "fonts/Century Gothic.ttf");

        MyCursorAdapter(MainActivity context, int layout, Cursor c,
                        String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //get reference to the row
            View view = super.getView(position, convertView, parent);
            //check for odd or even to set alternate colors to the row background
            if (position % 2 == 0) {
                view.setBackgroundColor(getColor(R.color.list_item_bg1));
            } else {
                view.setBackgroundColor(getColor(R.color.list_item_bg2));
            }

            //produce the correct color circle for contact's initials
            textViewContactName = (TextView) view.findViewById(R.id.contact_name);
            textViewContactLastName = (TextView) view.findViewById(R.id.textViewDisplayContactLastName);
            textViewContactName.setTypeface(centuryGothic);
            textViewContactLastName.setTypeface(centuryGothic);
            textViewContactInitials = (TextView) view.findViewById(R.id.initials);
            textViewColorHolder = (TextView) view.findViewById(R.id.textViewColorHolder);
            background_color = Integer.parseInt(textViewColorHolder.getText().toString());
            if (background_color == -1118482){ // light grey
                textViewContactInitials.setTextColor(getColor(R.color.black_font));
            }
            GradientDrawable drawable = (GradientDrawable) textViewContactInitials.getBackground();
            drawable.setColor(background_color);
            return view;
        }


    }

    public class MyAdapter extends ArrayAdapter<String>{

        MyAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater=getLayoutInflater();
            View row=inflater.inflate(R.layout.spinner_item, parent, false);
            TextView label=(TextView)row.findViewById(R.id.textViewSortLabel);
            label.setText(sortLabel[position]);
            TextView label2=(TextView)row.findViewById(R.id.textViewSortLabel2);
            label2.setText(sortLabel2[position]);
            TextView label3=(TextView)row.findViewById(R.id.textViewSortLabel3);
            label3.setText(sortLabel3[position]);
            TextView label4=(TextView)row.findViewById(R.id.textViewSortLabel4);
            label4.setText(sortLabel4[position]);

            return row;
        }
    }
}// class end
