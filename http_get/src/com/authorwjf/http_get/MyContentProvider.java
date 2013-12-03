package com.authorwjf.http_get;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

public class MyContentProvider extends ContentProvider {

	// database
	private MySQLiteHelper database;

	// Used for the UriMacher
	private static final int MAP = 10;
	private static final int SOMETHING_ELSE = 20;

	private static final String AUTHORITY = "com.authorwjf.http_get";
	private static final String BASE_PATH = "data";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/contacts";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/contacts";

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, MAP);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", SOMETHING_ELSE);
	}

	@Override
	synchronized public boolean onCreate() {
		database = new MySQLiteHelper(getContext());
		return false;
	}

	@Override
	synchronized public int delete(Uri uri, String selection,
			String[] selectionArgs) {
		return -1;
	}

	@Override
	synchronized public String getType(Uri uri) {
		return null;
	}

	@Override
	synchronized public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		long id = 0;
		switch (uriType) {
		case MAP:
			id = sqlDB.insert(MySQLiteHelper.TABLE_DATA, null,
					values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(CONTENT_URI + "/" + id);
	}

	@Override
	synchronized public int update(Uri uri, ContentValues values,
			String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriType) {
		case MAP:
			rowsUpdated = sqlDB.update(MySQLiteHelper.TABLE_DATA,
					values, selection, selectionArgs);
			break;
		case SOMETHING_ELSE:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(
						MySQLiteHelper.TABLE_DATA, values,
						MySQLiteHelper.COLUMN_ID + "=" + id, null);
			} else {
				rowsUpdated = sqlDB.update(
						MySQLiteHelper.TABLE_DATA, values,
						MySQLiteHelper.COLUMN_ID + "=" + id + " and "
								+ selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	@Override
	synchronized public Cursor query(Uri uri, String[] projection,
			String selection, String[] selectionArgs, String sortOrder) {
		return null;
	}
}
