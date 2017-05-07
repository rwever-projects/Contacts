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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DBhelper
 * DB Creation & Maintenance
 */
class DBhelper extends SQLiteOpenHelper {

    // TABLE INFORMATTION
    static final String TABLE_MEMBER = "contact";
    static final String CONTACT_ID = "_id";
    static final String CONTACT_INITIALS = "initials";
    static final String CONTACT_NAME = "name";
    static final String CONTACT_LASTNAME = "lastname";
    static final String CONTACT_COMPANY = "company";
    static final String CONTACT_COLOR = "color";

    // DATABASE INFORMATION
    private static final String DB_NAME = "CONTACT.DB";
    private static final int DB_VERSION = 1;

    // TABLE CREATION
    private static final String CREATE_TABLE = "create table "
            + TABLE_MEMBER + "(" + CONTACT_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CONTACT_INITIALS + " TEXT NOT NULL, "
            + CONTACT_NAME + " TEXT NOT NULL, "
            + CONTACT_LASTNAME + " TEXT NOT NULL, "
            + CONTACT_COMPANY + " TEXT NOT NULL, "
            + CONTACT_COLOR + " INT NOT NULL);";

    DBhelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBER);
        onCreate(db);
    }
}