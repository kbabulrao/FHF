package com.fhf.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.fhf.R;

/**
 * Created by santosh on 3/2/2017.
 */

public class Utils {

    public static void showSnackBarWithoutAction(Context context, View snackbarCoordinatorLayout, String message) {
        Log.d("in snack bar", message);
        final Snackbar snackbar = Snackbar
                .make(snackbarCoordinatorLayout, message, Snackbar.LENGTH_SHORT);
        snackbar.setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.setActionTextColor(ContextCompat.getColor(context, android.R.color.white));
        ViewGroup group = (ViewGroup) snackbar.getView();
        group.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

        snackbar.show();
    }

    public static void showSnackBarWithoutAction(Context context, View snackbarCoordinatorLayout, VolleyError error) {
        String message = "";

        if (error instanceof TimeoutError) {
            message = "Timeout ";
        } else if (error instanceof NoConnectionError) {
            message = context.getResources().getString(R.string.no_internet_connection);

        } else if (error instanceof AuthFailureError) {
            message = "Authentication failure";

        } else if (error instanceof ServerError) {
            message = "ServerError";

        } else if (error instanceof NetworkError) {
            message = "NetworkError";

        } else if (error instanceof ParseError) {

            message = "ParseError";
        } else {
            message = "";
        }

        Log.d("in snack bar", message);
        final Snackbar snackbar = Snackbar
                .make(snackbarCoordinatorLayout, message, Snackbar.LENGTH_SHORT);
        snackbar.setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.setActionTextColor(ContextCompat.getColor(context, android.R.color.white));
        ViewGroup group = (ViewGroup) snackbar.getView();
        group.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

        snackbar.show();
    }

    public static final boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private OnItemClickListener mListener;

        public interface OnItemClickListener {
            void onItemClick(View view, int position);
        }

        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, view.getChildPosition(childView));
                return true;
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

}
