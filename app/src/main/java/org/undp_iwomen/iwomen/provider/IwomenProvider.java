package org.undp_iwomen.iwomen.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import org.undp_iwomen.iwomen.CommonConfig;
import org.undp_iwomen.iwomen.database.SqliteHelper;
import org.undp_iwomen.iwomen.database.TableAndColumnsName;
import org.undp_iwomen.iwomen.utils.SelectionBuilder;


/**
 * Created by thitoo on 5/14/15.
 */
public class IwomenProvider extends ContentProvider {

    private SqliteHelper sqliteHelper;

    private final int USER = 1000;
    private final int POST = 2000;
    private final int POST_BYID = 2001;

    private final int COMMENT = 3000;

    private UriMatcher matcher = buildMatcher();


    @Override
    public boolean onCreate() {

        sqliteHelper = new SqliteHelper(getContext());

        return false;
    }

    @Override
    public String getType(Uri uri) {

        final int match = matcher.match(uri);
        switch (match) {
            case USER:
                return IwomenProviderData.UserProvider.CONTENT_TYPE;

            case POST:
                return IwomenProviderData.PostProvider.CONTENT_TYPE;

            case COMMENT:
                return IwomenProviderData.CommentProvider.CONTENT_TYPE;

            default:
                throw new UnsupportedOperationException(" = = = Unknow Uri : " + uri);

        }


    }

    @Override
    public Cursor query(Uri uri, String[] projections, String selection, String[] selectionargs, String sortorder) {
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();

        final int matcher = buildMatcher().match(uri);


        SelectionBuilder sb = buildSelection(uri, matcher);
        Cursor cursor = sb.where(selection, selectionargs).query(db, projections, sortorder);
        checkCursor(cursor);

        return cursor;

    }

    public static void checkCursor(Cursor cursor) {

        System.out.println("================================ cursor Count > " + cursor.getCount());

        if (cursor != null && cursor.moveToFirst()) {
            do {
                StringBuffer strBuf = new StringBuffer();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    strBuf.append(cursor.getColumnName(i) + " = " + cursor.getString(i) + " , ");
                }
                Log.d("Provider : ", "= = = = = : " + strBuf.toString());

            } while (cursor.moveToNext());

        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Log.e("insert : ", " >>> " + uri + " , " + contentValues.toString());
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        final int match = matcher.match(uri);
        long id = 0;
        switch (match) {
            case POST:
                id = db.insert(TableAndColumnsName.TableNames.POST, null, contentValues);
                if (id > 0) {
                    Log.e(uri.toString(), " complete insert :>  " + id);
                }
                return IwomenProviderData.PostProvider.buildContentUri(contentValues.getAsString(TableAndColumnsName.PostUtil.POST_OBJ_ID));
            case USER:
                id = db.insert(TableAndColumnsName.TableNames.USER, null, contentValues);
                if (id > 0) {
                    Log.e(uri.toString(), " complete insert :>  " + id);
                }
                return IwomenProviderData.UserProvider.buildContentUri(contentValues.getAsString(TableAndColumnsName.UserUtil.USER_OBJ_ID));

            case COMMENT:
                id = db.insert(TableAndColumnsName.TableNames.COMMENT, null, contentValues);
                if (id > 0) {
                    Log.e(uri.toString(), " complete insert :>  " + id);
                }
                return IwomenProviderData.CommentProvider.buildContentUri(contentValues.getAsString(TableAndColumnsName.CommentUtil.COMMENT_OBJ_ID));

            default:
                throw new UnsupportedOperationException("Unknown Uri : " + uri);


        }


    }



    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionargs) {
        SQLiteDatabase db = sqliteHelper.getWritableDatabase();
        int match = matcher.match(uri);

        switch (match){
            case USER:
                return db.update(TableAndColumnsName.TableNames.USER,contentValues,selection,selectionargs);

            case POST:
                return db.update(TableAndColumnsName.TableNames.POST,contentValues,selection,selectionargs);
            default:
                throw new UnsupportedOperationException("Unknown uri : "+uri);
        }

    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    private UriMatcher buildMatcher() {

        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(CommonConfig.AUTHORITY, "user", USER);

        matcher.addURI(CommonConfig.AUTHORITY, "post", POST);
        matcher.addURI(CommonConfig.AUTHORITY, "post/#", POST_BYID);

        matcher.addURI(CommonConfig.AUTHORITY, "comment", COMMENT);

        return matcher;
    }

    private SelectionBuilder buildSelection(Uri uri, int match) {
        SelectionBuilder sb = new SelectionBuilder();
        switch (match) {


            case USER:
                return sb.table(TableAndColumnsName.TableNames.USER);
            case POST:
                return sb.table(TableAndColumnsName.TableNames.POST);

            case COMMENT:
                return sb.table(TableAndColumnsName.TableNames.COMMENT);


            default:
                throw new UnsupportedOperationException("Unsupported Uri : " + uri);
        }

    }

}
