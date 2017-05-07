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
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
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

import java.util.Random;

import static android.R.color.black;
import static android.R.color.white;

public class Add_Contact extends Activity {

    Button saveButton;
    EditText editTextFirstName, editTextLastName, editTextCompany;
    RelativeLayout activity, contactCard;
    ImageButton back, undobutton1, undobutton2, undobutton3;
    SQLController dbcon;
    TextView nameCaption, lastnameCaption, companyCaption;
    TextView initials;

    String contactInitials;
    String contactFirstName;
    String contactLastName;
    String contactCompany;

    boolean EditText1Contents = false;
    boolean EditText2Contents = false;
    boolean EditText3Contents = false;
    boolean AllEditTextHaveContent = false;
    boolean nameCaptionHasBeenSet = false;
    boolean lastnameCaptionHasBeenSet = false;
    boolean companyCaptionHasBeenSet = false;
    boolean undobutton1AnimationHasBeenSet = false;
    boolean undobutton2AnimationHasBeenSet = false;
    boolean undobutton3AnimationHasBeenSet = false;
    boolean saveButtonAnimationHasBeenSet = false;

    Animation myFadeInAnimation;
    Animation myFadeOutAnimation;
    Toast toast;
    TextView text;

    Handler mHandler, mHandler1, mHandler2;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.add_contact);

        //GUI References
        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        editTextCompany = (EditText) findViewById(R.id.editTextCompany);
        undobutton1 = (ImageButton) findViewById(R.id.undoButton1);
        undobutton2 = (ImageButton) findViewById(R.id.undoButton2);
        undobutton3 = (ImageButton) findViewById(R.id.undoButton3);
        nameCaption = (TextView) findViewById(R.id.textViewFirstNameCaption);
        lastnameCaption = (TextView) findViewById(R.id.textViewLastNameCaption);
        companyCaption = (TextView) findViewById(R.id.textViewCompanyCaption);
        saveButton = (Button) findViewById(R.id.saveButton);
        back = (ImageButton) findViewById(R.id.backButton);
        activity = (RelativeLayout) findViewById(R.id.activity_add_contact);
        contactCard = (RelativeLayout) findViewById(R.id.contactCard);

        // Asset References
        Typeface centuryGothic = Typeface.createFromAsset(getAssets(), "fonts/Century Gothic.ttf");

        //GUI Attributes
        //hide undo buttons at activty startup
        nameCaption.setVisibility(View.INVISIBLE);
        lastnameCaption.setVisibility(View.INVISIBLE);
        companyCaption.setVisibility(View.INVISIBLE);
        saveButton.setVisibility(View.INVISIBLE);
        undobutton1.setVisibility(View.INVISIBLE);
        undobutton2.setVisibility(View.INVISIBLE);
        undobutton3.setVisibility(View.INVISIBLE);

        //load in animations
        myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        myFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fadeout);

        // Initialize custom TOAST
        toast = Toast.makeText(getApplicationContext(), " ✓ Contact Saved as ", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        View toastView = toast.getView();
        toastView.setBackgroundResource(R.drawable.toast_saved_confirmation);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_saved_layout,
                (ViewGroup) findViewById(R.id.custom_toast_container));
        text = (TextView) layout.findViewById(R.id.toastText);
        initials = (TextView) layout.findViewById(R.id.initials);
        initials.setTypeface(centuryGothic);
        toast.setView(layout);

        //Listen for text on editTextFirstName input field
        editTextFirstName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                contactFirstName = editTextFirstName.getText().toString().trim();

                if (contactFirstName.length() > 0) {
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
                    isFieldsSet();
                }
                if (contactFirstName.length() == 0) {
                    //there is text in the first name text field
                    undobutton1.setVisibility(View.INVISIBLE);
                    if (AllEditTextHaveContent) {
                        saveButton.startAnimation(myFadeOutAnimation); //Set animation to ImageButton
                        AllEditTextHaveContent = false;
                    }
                    saveButtonAnimationHasBeenSet = false;
                    undobutton1AnimationHasBeenSet = false;
                    EditText1Contents = false;
                    saveButton.setVisibility(View.INVISIBLE); //field IS empty
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

                contactLastName = editTextLastName.getText().toString().trim();

                if (contactLastName.length() > 0) {
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
                    isFieldsSet();
                }
                if (contactLastName.length() == 0) {
                    //there is text in the first name text field
                    undobutton2.setVisibility(View.INVISIBLE);
                    if (AllEditTextHaveContent) {
                        saveButton.startAnimation(myFadeOutAnimation); //Set animation to ImageButton
                        AllEditTextHaveContent = false;
                    }
                    saveButtonAnimationHasBeenSet = false;
                    undobutton2AnimationHasBeenSet = false;
                    EditText2Contents = false;
                    saveButton.setVisibility(View.INVISIBLE); //field IS empty
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

                contactCompany = editTextCompany.getText().toString().trim();

                if (contactCompany.length() > 0) {
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
                    isFieldsSet();
                }
                if (contactCompany.length() == 0) {
                    //there is text in the first name text field
                    undobutton3.setVisibility(View.INVISIBLE);
                    if (AllEditTextHaveContent) {
                        saveButton.startAnimation(myFadeOutAnimation); //Set animation to ImageButton
                        AllEditTextHaveContent = false;
                    }
                    saveButtonAnimationHasBeenSet = false;
                    undobutton3AnimationHasBeenSet = false;
                    EditText3Contents = false;
                    saveButton.setVisibility(View.INVISIBLE); //field IS empty
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

        dbcon = new SQLController(this);
        dbcon.open();

        //tell my buttons to listen up!
        addListenerOnButton();
    }

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
                    saveButton.startAnimation(myFadeOutAnimation); //Set animation to ImageButton
                    AllEditTextHaveContent = false;
                    saveButtonAnimationHasBeenSet = false;
                }
                saveButton.setVisibility(View.INVISIBLE);
            }
        });
        undobutton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                editTextLastName.setText("");
                undobutton2.setVisibility(View.INVISIBLE);
                if (AllEditTextHaveContent) {
                    saveButton.startAnimation(myFadeOutAnimation); //Set animation to ImageButton
                    AllEditTextHaveContent = false;
                    saveButtonAnimationHasBeenSet = false;
                }
                saveButton.setVisibility(View.INVISIBLE);
            }
        });
        undobutton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                editTextCompany.setText("");
                undobutton3.setVisibility(View.INVISIBLE);
                if (AllEditTextHaveContent) {
                    saveButton.startAnimation(myFadeOutAnimation); //Set animation to ImageButton
                    AllEditTextHaveContent = false;
                    saveButtonAnimationHasBeenSet = false;
                }
                saveButton.setVisibility(View.INVISIBLE);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                saveButton.setVisibility(View.INVISIBLE);

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
                initials.setText(contactInitials);

                undobutton1.setVisibility(View.INVISIBLE);
                undobutton2.setVisibility(View.INVISIBLE);
                undobutton3.setVisibility(View.INVISIBLE);

                editTextFirstName.startAnimation(myFadeOutAnimation);
                editTextLastName.startAnimation(myFadeOutAnimation);
                editTextCompany.startAnimation(myFadeOutAnimation);
                mHandler = new Handler();
                mHandler.postDelayed(clearEditTextDelay, 300); // Delay clearing editText fields

                newContact();

                contactCard.setVisibility(View.GONE);

                // Delay display toast
                mHandler1 = new Handler();
                mHandler1.postDelayed(customToastDelay, 450); // Delay clearing editText fields
                // Delay next activity to display toast
                mHandler2 = new Handler();
                mHandler2.postDelayed(nextActivityDelay, 4200); // Delay clearing editText fields
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                goBackToMainActivity();
            }
        });
    }


    /**
     * areBothFieldsSet
     * Switches signup button background if username field is set
     */
    public void isFieldsSet() {
        if (EditText1Contents) {
            if (EditText2Contents) {
                if (EditText3Contents) {
                    if (!saveButtonAnimationHasBeenSet) {
                        saveButton.startAnimation(myFadeInAnimation); //Set animation to ImageButton
                        saveButtonAnimationHasBeenSet = true;
                    }
                    saveButton.setVisibility(View.VISIBLE);  //field is NOT empty
                    AllEditTextHaveContent = true;
                }
            }
        } else {
            saveButton.setVisibility(View.INVISIBLE); //field IS empty
        }
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

    private Runnable customToastDelay = new Runnable() {
        @Override
        public void run() {
            /* do what you need to do */
            // Show custom TOAST
            toast.show();

        }
    };

    private Runnable nextActivityDelay = new Runnable() {
        @Override
        public void run() {
            /* do what you need to do */
            goBackToMainActivity();
        }
    };

    public void newContact() {
        int[] androidColors = getResources().getIntArray(R.array.circle_background_array);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];

        if (randomAndroidColor == -1118482) { //light grey
            initials.setTextColor(getColor(black));
        }
        if (randomAndroidColor != -1118482) { // NOT light grey
            initials.setTextColor(getColor(white));
        }

        initials.setBackgroundResource(R.drawable.roundborder);

        GradientDrawable drawable = (GradientDrawable) initials.getBackground();
        drawable.setColor(randomAndroidColor);
        String name = editTextFirstName.getText().toString();
        String lastname = editTextLastName.getText().toString();
        String company = editTextCompany.getText().toString();
        dbcon.insertData(contactInitials, name, lastname, company, randomAndroidColor);
    }

    private void goBackToMainActivity() {
        Intent main = new Intent(Add_Contact.this, MainActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(main);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
