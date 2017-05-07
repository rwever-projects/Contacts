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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
//import android.util.Log;  // ********* Needed for debug line

/**
 * SQLController
 * CRUD Operations
 */
class SQLController {

    private Context ourcontext;
    private SQLiteDatabase database;

    SQLController(Context c) {
        ourcontext = c;
    }

    public SQLController open() throws SQLException {
        DBhelper dbhelper = new DBhelper(ourcontext);
        database = dbhelper.getWritableDatabase();
        return this;

    }

	/*public void close() {
        dbhelper.close();
	}*/

    void insertData(String initials, String name, String lastname, String company, int color) {
        ContentValues cv = new ContentValues();
        cv.put(DBhelper.CONTACT_INITIALS, initials);
        cv.put(DBhelper.CONTACT_NAME, name);
        cv.put(DBhelper.CONTACT_LASTNAME, lastname);
        cv.put(DBhelper.CONTACT_COMPANY, company);
        cv.put(DBhelper.CONTACT_COLOR, color);
        database.insert(DBhelper.TABLE_MEMBER, null, cv);
    }

    Cursor readData() {
        String[] allColumns = new String[]{DBhelper.CONTACT_ID, DBhelper.CONTACT_INITIALS,
                DBhelper.CONTACT_NAME, DBhelper.CONTACT_LASTNAME, DBhelper.CONTACT_COMPANY, DBhelper.CONTACT_COLOR};

        Cursor c = database.query(DBhelper.TABLE_MEMBER, allColumns, null,
                null, null, null, null);
        if (c != null) {
            c.moveToFirst();
            while(!c.isAfterLast()){
                // ******** FOR DEBUG *********
//                int contactColor = c.getInt(5);
//                Log.d("SQLController","Color in readData is: " + contactColor);
                c.moveToNext();
            }
            c.moveToFirst();
        }
        return c;
    }

    Cursor readDataLastName() {
        String[] allColumns = new String[]{DBhelper.CONTACT_ID, DBhelper.CONTACT_INITIALS,
                DBhelper.CONTACT_NAME, DBhelper.CONTACT_LASTNAME, DBhelper.CONTACT_COMPANY, DBhelper.CONTACT_COLOR};

        Cursor c = database.query(DBhelper.TABLE_MEMBER, null, null, null, null, null, DBhelper.CONTACT_LASTNAME +" ASC");
        if (c != null) {
            c.moveToFirst();
            while(!c.isAfterLast()){
                c.moveToNext();
            }
            c.moveToFirst();
        }
        return c;
    }

    Cursor readDataFirstName() {
        String[] allColumns = new String[]{DBhelper.CONTACT_ID, DBhelper.CONTACT_INITIALS,
                DBhelper.CONTACT_NAME, DBhelper.CONTACT_LASTNAME, DBhelper.CONTACT_COMPANY, DBhelper.CONTACT_COLOR};

        Cursor c = database.query(DBhelper.TABLE_MEMBER, null, null, null, null, null, DBhelper.CONTACT_NAME +" ASC");
        if (c != null) {
            c.moveToFirst();
            while(!c.isAfterLast()){
                c.moveToNext();
            }
            c.moveToFirst();
        }
        return c;
    }

    int updateData(long memberID, String contactInitials, String contactName, String contactLastName, String contactCompany, int contactColor) {
        ContentValues cvUpdate = new ContentValues();
        cvUpdate.put(DBhelper.CONTACT_INITIALS, contactInitials);
        cvUpdate.put(DBhelper.CONTACT_NAME, contactName);
        cvUpdate.put(DBhelper.CONTACT_LASTNAME, contactLastName);
        cvUpdate.put(DBhelper.CONTACT_COMPANY, contactCompany);
        cvUpdate.put(DBhelper.CONTACT_COLOR, contactColor);
        return database.update(DBhelper.TABLE_MEMBER, cvUpdate, DBhelper.CONTACT_ID + " = " + memberID, null);
    }

    void deleteData(long memberID) {
        database.delete(DBhelper.TABLE_MEMBER, DBhelper.CONTACT_ID + "="
                + memberID, null);
    }

}
