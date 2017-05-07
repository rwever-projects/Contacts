package edu.asu.bsse.rwever.contacts;

/**
 * Created by:
 * @author Rudi Wever mailto: rwever@asu.edu
 * @version April 28, 2017
 *
 * Copyright © 2017 Rudi Wever.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Edit_Contact extends Activity {

    EditText editTextFirstName, editTextLastName, editTextCompany;
    Button updateButton, deleteButton;
    ImageButton back, undobutton1, undobutton2, undobutton3;
    RelativeLayout deleteBar, contactCard;
    TextView nameCaption, lastnameCaption, companyCaption;

    int contactColor;
    long contact_id;

    String contactInitials;
    String etContactFirstName;
    String etContactLastName;
    String etContactCompany;

    boolean EditText1Contents = true;
    boolean EditText2Contents = true;
    boolean EditText3Contents = true;
    boolean AllEditTextHaveContent = true;
    boolean nameCaptionHasBeenSet = true;
    boolean lastnameCaptionHasBeenSet = true;
    boolean companyCaptionHasBeenSet = true;
    boolean undobutton1AnimationHasBeenSet = false;
    boolean undobutton2AnimationHasBeenSet = false;
    boolean undobutton3AnimationHasBeenSet = false;
    boolean saveButtonAnimationHasBeenSet = false;

    Animation myFadeInAnimation;
    Animation myFadeOutAnimation;
    Toast updateToast, deleteToast;
    TextView text;

    Handler mHandler, mHandler1, mHandler2, mHandler3;

    SQLController dbcon;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.edit_contact);

        dbcon = new SQLController(this);
        dbcon.open();
        mHandler1 = new Handler();
        mHandler2 = new Handler();
        mHandler3 = new Handler();

        //GUI References
        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        editTextCompany = (EditText) findViewById(R.id.editTextCompany);
        undobutton1 = (ImageButton) findViewById(R.id.undoButton1);
        undobutton2 = (ImageButton) findViewById(R.id.undoButton2);
        undobutton3 = (ImageButton) findViewById(R.id.undoButton3);
        updateButton = (Button) findViewById(R.id.updateButton);
        deleteButton = (Button) findViewById(R.id.deleteContactButton);
        back = (ImageButton) findViewById(R.id.backButton);
        nameCaption = (TextView) findViewById(R.id.textViewFirstNameCaption);
        lastnameCaption = (TextView) findViewById(R.id.textViewLastNameCaption);
        companyCaption = (TextView) findViewById(R.id.textViewCompanyCaption);
        deleteBar = (RelativeLayout) findViewById(R.id.deleteBar);
        contactCard = (RelativeLayout) findViewById(R.id.contactCard);

        Intent i = getIntent();
        String contactID = i.getStringExtra("contactID");
        String contactName = i.getStringExtra("contactName");
        String contactLastName = i.getStringExtra("contactLastName");
//        String contactInitals = i.getStringExtra("contactInitals");
        String contactCompany = i.getStringExtra("contactCompany");
        contactColor = i.getIntExtra("contactColor", 0);

        contact_id = Long.parseLong(contactID);

        // Asset References
        Typeface centuryGothic = Typeface.createFromAsset(getAssets(), "fonts/Century Gothic.ttf");

        //GUI Attributes
        editTextFirstName.setText(contactName);
        editTextLastName.setText(contactLastName);
        editTextCompany.setText(contactCompany);

        //load in animations
        myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        myFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fadeout);

        // Initialize custom TOAST
        updateToast = Toast.makeText(getApplicationContext(), " ✓ Contact Saved as ", Toast.LENGTH_LONG);
        updateToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        View toastView = updateToast.getView();
        toastView.setBackgroundResource(R.drawable.toast_saved_confirmation);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_updated_layout,
                (ViewGroup) findViewById(R.id.custom_toast_container));
        text = (TextView) layout.findViewById(R.id.toastText);
        updateToast.setView(layout);

        //Listen for text on editTextFirstName input field
        editTextFirstName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                etContactFirstName = editTextFirstName.getText().toString().trim();

                if (etContactFirstName.length() > 0) {
                    //there is text in the first name text field
                    //so we display the undo button
                    if (!undobutton1AnimationHasBeenSet) {
                        undobutton1.startAnimation(myFadeInAnimation); //Set animation to ImageButton
                        undobutton1AnimationHasBeenSet = true;
                    }
                    undobutton1.setVisibility(View.VISIBLE);
                    EditText1Contents = true;
                    if (!nameCaptionHasBeenSet) {
                        nameCaption.startAnimation(myFadeInAnimation);
                        nameCaption.setVisibility(View.VISIBLE);
                        nameCaptionHasBeenSet = true;
                    }
                    updateButton.setVisibility(View.VISIBLE);
                    isFieldsSet();
                }
                if (etContactFirstName.length() == 0) {
                    //there is text in the first name text field
                    undobutton1.setVisibility(View.INVISIBLE);
                    if (AllEditTextHaveContent) {
                        AllEditTextHaveContent = false;
                    }
                    saveButtonAnimationHasBeenSet = false;
                    undobutton1AnimationHasBeenSet = false;
                    EditText1Contents = false;
                    nameCaption.startAnimation(myFadeOutAnimation);
                    nameCaption.setVisibility(View.INVISIBLE);
                    nameCaptionHasBeenSet = false;
                    isFieldsSet();
                }
            }
        });

        //Listen for text on editTextLastName input field
        editTextLastName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                etContactLastName = editTextLastName.getText().toString().trim();

                if (etContactLastName.length() > 0) {
                    //there is text in the first name text field
                    //so we display the undo button
                    if (!undobutton2AnimationHasBeenSet) {
                        undobutton2.startAnimation(myFadeInAnimation); //Set animation to ImageButton
                        undobutton2AnimationHasBeenSet = true;
                    }
                    undobutton2.setVisibility(View.VISIBLE);
                    EditText2Contents = true;
                    if (!lastnameCaptionHasBeenSet) {
                        lastnameCaption.startAnimation(myFadeInAnimation);
                        lastnameCaption.setVisibility(View.VISIBLE);
                        lastnameCaptionHasBeenSet = true;
                    }
                    updateButton.setVisibility(View.VISIBLE);
                    isFieldsSet();
                }
                if (etContactLastName.length() == 0) {
                    //there is text in the first name text field
                    undobutton2.setVisibility(View.INVISIBLE);
                    if (AllEditTextHaveContent) {
                        AllEditTextHaveContent = false;
                    }
                    saveButtonAnimationHasBeenSet = false;
                    undobutton2AnimationHasBeenSet = false;
                    EditText2Contents = false;
                    lastnameCaption.startAnimation(myFadeOutAnimation);
                    lastnameCaption.setVisibility(View.INVISIBLE);
                    lastnameCaptionHasBeenSet = false;
                    isFieldsSet();
                }
            }
        });

        //Listen for text on editTextCompany input field
        editTextCompany.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                etContactCompany = editTextCompany.getText().toString().trim();

                if (etContactCompany.length() > 0) {
                    //there is text in the first name text field
                    //so we display the undo button
                    if (!undobutton3AnimationHasBeenSet) {
                        undobutton3.startAnimation(myFadeInAnimation); //Set animation to ImageButton
                        undobutton3AnimationHasBeenSet = true;
                    }
                    undobutton3.setVisibility(View.VISIBLE);
                    EditText3Contents = true;
                    if (!companyCaptionHasBeenSet) {
                        companyCaption.startAnimation(myFadeInAnimation);
                        companyCaption.setVisibility(View.VISIBLE);
                        companyCaptionHasBeenSet = true;
                    }
                    updateButton.setVisibility(View.VISIBLE);
                    isFieldsSet();
                }
                if (etContactCompany.length() == 0) {
                    //there is text in the first name text field
                    undobutton3.setVisibility(View.INVISIBLE);
                    if (AllEditTextHaveContent) {
                        AllEditTextHaveContent = false;
                    }
                    saveButtonAnimationHasBeenSet = false;
                    undobutton3AnimationHasBeenSet = false;
                    EditText3Contents = false;
                    companyCaption.startAnimation(myFadeOutAnimation);
                    companyCaption.setVisibility(View.INVISIBLE);
                    companyCaptionHasBeenSet = false;
                    isFieldsSet();
                }
            }
        });

        //Check to see if 'Done' button was pressed on the softkeyboard
        //remove focus from current last name text field (focus actually passes back to parent RelativeLayout)
        //hide undo text button for last name text field
        editTextCompany.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // When 'done' button on softkeyboar is pressed, remove undo button on last name input field
                    undobutton1.setVisibility(View.INVISIBLE);
                    undobutton2.setVisibility(View.INVISIBLE);
                    undobutton3.setVisibility(View.INVISIBLE);
                    editTextCompany.clearFocus();

                    View view = findViewById(R.id.companyLayout);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });

        //tell my buttons to listen up!
        addListenerOnButton();
    }//end of onCreate

    /**
     * addListenerOnButton
     * Listens to the buttons of this activity
     */
    public void addListenerOnButton() {
        undobutton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                editTextFirstName.setText("");
                undobutton1.setVisibility(View.INVISIBLE);
                if (AllEditTextHaveContent) {
                    AllEditTextHaveContent = false;
                    saveButtonAnimationHasBeenSet = false;
                }
            }
        });
        undobutton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                editTextLastName.setText("");
                undobutton2.setVisibility(View.INVISIBLE);
                if (AllEditTextHaveContent) {
                    AllEditTextHaveContent = false;
                    saveButtonAnimationHasBeenSet = false;
                }
            }
        });
        undobutton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                editTextCompany.setText("");
                undobutton3.setVisibility(View.INVISIBLE);
                if (AllEditTextHaveContent) {
                    AllEditTextHaveContent = false;
                    saveButtonAnimationHasBeenSet = false;
                }
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                updateButton.setVisibility(View.INVISIBLE);
                deleteBar.setVisibility(View.INVISIBLE);

                // Check for empty editText fields
                if (!AllEditTextHaveContent) {
                    if (!EditText1Contents && !EditText2Contents && !EditText3Contents) {
                        Toast.makeText(getApplicationContext(), "Kindly fill out all fields.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (!EditText1Contents && !EditText2Contents) {
                        Toast.makeText(getApplicationContext(), "Please include first and last names.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (!EditText1Contents && !EditText3Contents) {
                        Toast.makeText(getApplicationContext(), "Please include first name\nand company name.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (!EditText2Contents && !EditText3Contents) {
                        Toast.makeText(getApplicationContext(), "Please include last name\nand company name.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (!EditText1Contents) {
                        Toast.makeText(getApplicationContext(), "Please include first name.", Toast.LENGTH_LONG).show();
                    }
                    if (!EditText2Contents) {
                        Toast.makeText(getApplicationContext(), "Please include last name.", Toast.LENGTH_LONG).show();
                    }
                    if (!EditText3Contents) {
                        Toast.makeText(getApplicationContext(), "Please include company name.", Toast.LENGTH_LONG).show();
                    }
                    return;
                }

                //disable back button
                back.setEnabled(false);

                editTextFirstName.clearFocus();
                editTextLastName.clearFocus();
                editTextCompany.clearFocus();

                // Hide softkeyboard
                View parent = findViewById(R.id.activity_add_contact);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(parent.getWindowToken(), 0);

                // Get contact's first and last initial
                contactInitials = (editTextFirstName.getText().toString().trim().substring(0, 1) + editTextLastName.getText().toString().trim().substring(0, 1));

                undobutton1.setVisibility(View.INVISIBLE);
                undobutton2.setVisibility(View.INVISIBLE);
                undobutton3.setVisibility(View.INVISIBLE);

                editTextFirstName.startAnimation(myFadeOutAnimation);
                editTextLastName.startAnimation(myFadeOutAnimation);
                editTextCompany.startAnimation(myFadeOutAnimation);

                mHandler = new Handler();
                mHandler.postDelayed(clearEditTextDelay, 300); // Delay clearing editText fields

                contactCard.setVisibility(View.GONE);

                mHandler1.postDelayed(customUpdateToastDelay, 450); // Delay toast display
                mHandler2.postDelayed(nextActivityDelay, 4200); // Delay next activity

                String contactInitials_updated = (editTextFirstName.getText().toString().trim().substring(0, 1) + editTextLastName.getText().toString().trim().substring(0, 1));
                String contactFirstName_updated = editTextFirstName.getText().toString();
                String contactLastName_updated = editTextLastName.getText().toString();
                String contactCompany_updated = editTextCompany.getText().toString();
                dbcon.updateData(contact_id, contactInitials_updated, contactFirstName_updated, contactLastName_updated, contactCompany_updated, contactColor);

            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Initialize custom TOAST
                deleteToast = Toast.makeText(getApplicationContext(), " ✓ Contact Deleted ", Toast.LENGTH_LONG);
                deleteToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                View toastView = deleteToast.getView();
                toastView.setBackgroundResource(R.drawable.toast_saved_confirmation);
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toast_deleted_layout,
                        (ViewGroup) findViewById(R.id.custom_toast_container));
                deleteToast.setView(layout);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Edit_Contact.this);
                alertDialog.setTitle("Confirm Delete...");
                alertDialog.setMessage("Are you sure you want delete this contact?");
                alertDialog.setIcon(R.drawable.trashcan);

                // YES event
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        updateButton.setVisibility(View.INVISIBLE);
                        contactCard.setVisibility(View.GONE);
                        deleteBar.setVisibility(View.INVISIBLE);
                        back.setEnabled(false); //disable back button
                        dbcon.deleteData(contact_id);
                        mHandler3.postDelayed(customDeleteToastDelay, 450); // Delay toast display
                        mHandler2.postDelayed(nextActivityDelay, 4200); // Delay next activity
                    }
                });

                // NO event
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Good thing I checked!!", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                // Show Alert
                alertDialog.show();

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                goBackToMainActivity();
            }
        });
    }


    private Runnable clearEditTextDelay = new Runnable() {
        @Override
        public void run() {
            /* do what you need to do */
            editTextFirstName.setText("");
            editTextLastName.setText("");
            editTextCompany.setText("");

        }
    };

    private Runnable customUpdateToastDelay = new Runnable() {
        @Override
        public void run() {
            /* do what you need to do */
            // Show custom TOAST
            updateToast.show();

        }
    };

    private Runnable customDeleteToastDelay = new Runnable() {
        @Override
        public void run() {
            /* do what you need to do */
            // Show custom TOAST
            deleteToast.show();

        }
    };

    private Runnable nextActivityDelay = new Runnable() {
        @Override
        public void run() {
            /* do what you need to do */
            goBackToMainActivity();
        }
    };

    /**
     * areBothFieldsSet
     * Switches signup button background if username field is set
     */
    public void isFieldsSet() {
        if (EditText1Contents) {
            if (EditText2Contents) {
                if (EditText3Contents) {
                    if (!saveButtonAnimationHasBeenSet) {
                        saveButtonAnimationHasBeenSet = true;
                    }
                    AllEditTextHaveContent = true;
                }
            }
        } else {
            AllEditTextHaveContent = false;

        }
    }

    private void goBackToMainActivity() {
        Intent main = new Intent(Edit_Contact.this, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(main);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
