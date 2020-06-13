package com.hfad.afrigas.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class DataContract {
    private DataContract() {}

    public static final String CONTENT_AUTHORITY = "com.hfad.afrigas";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_ORDERS = "orders";

    public static final class OrderEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ORDERS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ORDERS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ORDERS;

        public final static String TABLE_NAME = "orders";

        public final static String _ID = BaseColumns._ID;
        //public final static String COLUMN_DATE = "columnDate";
        public final static String COLUMN_ORDER_ID = "orderId";
        public final static String COLUMN_ORDER_TIME = "time";
        public final static String COLUMN_ORDER_QUANTITY = "quantity";
        public final static String COLUMN_ORDER_NAME = "name";
        public final static String COLUMN_ORDER_HOSTEL = "hostel";
        public final static String COLUMN_ORDER_BLOCK = "block";
        public final static String COLUMN_ORDER_ROOM = "room";
        public final static String COLUMN_ORDER_PHONE_NO = "phoneNo";

    }
}
