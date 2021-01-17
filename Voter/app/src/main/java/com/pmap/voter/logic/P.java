package com.pmap.voter.logic;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.telephony.SmsManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.pmap.voter.R;

import java.io.ByteArrayOutputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.DateFormatSymbols;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Response;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public class P {

        public static String URL = "http://192.168.43.206/ovub/index.php/";
        public static String BASE_URL = "http://192.168.43.206/ovub/";
        public static String TESTIMONIAL_VIDEOS_PATH =BASE_URL +"testimonial_videos/";
        public static String TESTIMONIAL_IMAGE_PATH =BASE_URL +"testimonial_profile_images/";
        public static String Gallery_PATH=BASE_URL+"image/";

        public static void LogD(String msg) {
            Log.d("PMAPACADEMIC", "-----------------------------------------------------------------");
            Log.d("PMAPACADEMIC", msg);
            Log.d("PMAPACADEMIC", "-----------------------------------------------------------------");
        }

        public static void LogE(String msg) {
            Log.d("PMAPACADEMIC", "-----------------------------------------------------------------");
            Log.d("PMAPACADEMIC", msg);
            Log.d("PMAPACADEMIC", "-----------------------------------------------------------------");
        }

        public static boolean analyseResponse(Response<?> response) {
            if (response == null) {
                LogE(" <<<<<< Response: NULL >>>>>>");
                return false;
            }

            if (response.body() == null) {
                //Toast.makeText(MainApplication.getContext(), response.message(), Toast.LENGTH_SHORT).show();
                LogE(" <<<<<< Response: BODY NULL >>>>>>");
                return false;
            }

            return true;
        }

        public static void displayNetworkErrorMessage(Context context, View view, Throwable t) {

            if (view == null) {


                if (t instanceof ConnectException)
                    Toast.makeText(context, "Connection Cannot Be Establish To The Server.", Toast.LENGTH_SHORT).show();
                else if (t instanceof SocketTimeoutException)
                    Toast.makeText(context, "Server Timed Out", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            } else {

                if (t instanceof ConnectException)
                    Toast.makeText(context, "Connection Cannot Be Establish To The Server.", Toast.LENGTH_LONG).show();

                else if (t instanceof SocketTimeoutException)
                    Toast.makeText(context, "Server Timed Out", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();

            }


        }

        //TO SEND SMS
        public static void sendSMS(String number, String msg) {
            LogD("number : " + number + " msg : " + msg);
            if(number ==null || number.trim().isEmpty())return;
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, msg, null, null);
        }

        //Convert byte array to bitmap
        public static Bitmap getBitmapFromByteArray(byte[] byteArray) {
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            return bmp;
        }

        public static String BitmapToString(Bitmap bitmap) {
            if(bitmap==null)return "";
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
            byte[] byte_arr = stream.toByteArray();
            return Base64.encodeBytes(byte_arr);
        /*ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 10, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);*/
        }

        //Convert bitmap to byte array
        public static byte[] getByteArrayFromBitmap(Bitmap bmp) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            return byteArray;
        }

        //RESIZE THEE IMAGE
        public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
            int width = image.getWidth();
            int height = image.getHeight();

            float bitmapRatio = (float) width / (float) height;
            if (bitmapRatio > 1) {
                width = maxSize;
                height = (int) (width / bitmapRatio);
            } else {
                height = maxSize;
                width = (int) (height * bitmapRatio);
            }

            return Bitmap.createScaledBitmap(image, width, height, true);
        }

        public static String getDate() {
            String str = "";
            Calendar c = Calendar.getInstance();
            str = prefixZeroIfSingleDigit(c.get(Calendar.DATE)) + "/" + prefixZeroIfSingleDigit((c.get(Calendar.MONTH) + 1)) + "/" + c.get(Calendar.YEAR);
            return str;
        }

        public static String getTime() {
            String str = "";
            Calendar c = Calendar.getInstance();
            str = prefixZeroIfSingleDigit(c.get(Calendar.HOUR_OF_DAY)) + ":" + prefixZeroIfSingleDigit((c.get(Calendar.MINUTE)));
            return str;
        }

        public static String convertToCustomDateFormat(int y, int monthMinusOneValue, int d) {
            return prefixZeroIfSingleDigit(y) + "-" + prefixZeroIfSingleDigit(monthMinusOneValue + 1) + "-" + prefixZeroIfSingleDigit(d);
        }

        public static String prefixZeroIfSingleDigit(int intVal) {
            Integer i = new Integer(intVal);
            String val = i.toString();
            return val.length() == 1 ? "0" + val : val;
        }

        /* //SET SPINNER ADAPTER
         public static void setSpinnerAdapter(Context context, Spinner sp, List<String> list) {
             ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(context, R.layout.ao_simple_spinner_item, list);
             adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
             sp.setAdapter(adapter1);
         }*/

  //  SET SPINNER ADAPTER
    public static void setSpinnerAdapter(Context context, Spinner sp, String[] list) {
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(context, R.layout.voters, list);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter1);
    }
        public static SweetAlertDialog showBufferDialog(Context context, final String tittle){
            SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText(tittle);
            pDialog.setCancelable(false);
            pDialog.show();

            return pDialog;
        }


        public static void ShowSuccessDialog(final Context context, final String tittle, final String msg) {
            SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
            pDialog.setTitleText(tittle);
            pDialog.setContentText(msg);
            pDialog.setCancelable(false);
            pDialog.show();
            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    if(sweetAlertDialog.isShowing())sweetAlertDialog.dismiss();
                    ((AppCompatActivity) context).finish();
                }
            });
        }
    public static void ShowSuccessDialogAndBeHere(final Context context, final String tittle, final String msg) {
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(tittle);
        pDialog.setContentText(msg);
        pDialog.setCancelable(false);
        pDialog.show();
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                if(sweetAlertDialog.isShowing())sweetAlertDialog.dismiss();
            }
        });
    }

        public static void ShowErrorDialogAndExit(final Context context, final String tittle, final String msg) {
            SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
            pDialog.setTitleText(tittle);
            pDialog.setContentText(msg);
            pDialog.setCancelable(false);
            pDialog.show();
            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    if(sweetAlertDialog.isShowing())sweetAlertDialog.dismiss();
                    ((AppCompatActivity) context).finish();
                }
            });
        }

        public static void ShowErrorDialogAndBeHere(final Context context, final String tittle, final String msg) {
            SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE);
            pDialog.setTitleText(tittle);
            pDialog.setContentText(msg);
            pDialog.setCancelable(false);
            pDialog.show();
            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    if(sweetAlertDialog.isShowing())sweetAlertDialog.dismiss();
                }
            });
        }


        public static boolean isValidEditText(EditText editText, String errorMessage) {
            if (editText.getText().toString().trim().isEmpty()) {
                editText.setError("Please Enter " + errorMessage);
                return false;
            }
            return true;
        }

        public static boolean isValidMobileNumber(EditText editText, String errorMessage) {
            if (editText.getText().toString().trim().isEmpty()) {
                editText.setError("Please Enter Mobile Number");
                return false;
            }
            if(editText.getText().toString().trim().length()!=10){
                editText.setError("Please Enter Valid Mobile Number");
                return false;
            }
            return true;
        }

        public static boolean isValidGSTN(EditText editText, String errorMessage) {
            if (editText.getText().toString().trim().isEmpty()) {
                editText.setError("Please Enter GSTN");
                return false;
            }
            if(editText.getText().toString().trim().length()!=15){
                editText.setError("Please Enter Valid GSTN");
                return false;
            }
            return true;
        }

  /*  public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                return "";
            }
            return telephonyManager.getDeviceId() + "";
        }
        return "";
    }*/

        public static Bitmap StringToBitmap(String input) {
            Bitmap bitmap = null;
            try {
                byte[] decodedByte = Base64.decode(input);

                bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
            } catch (Exception e) {

            }
            return bitmap;
        }
        @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
        public static void setDateDefault(Context context, final EditText txt_date, int day, boolean canSetMax, boolean canSetMin) {
            final Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, day);
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            if (txt_date.getText().toString().trim().length() > 0) {
                String a[] = txt_date.getText().toString().trim().split("-");
                mYear = Integer.parseInt(a[0]);
                mMonth = Integer.parseInt(a[1])-1;
                mDay = Integer.parseInt(a[2]);
            }
            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            //c.set(year, month, day);
                            P.LogD(year + " " + month + " " + day);
                            String fromDate = P.convertToCustomDateFormat(year, month, day);
                            txt_date.setText(fromDate);
                            txt_date.setError(null);


                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.updateDate(mYear, mMonth, mDay);
            if (canSetMax) {
                datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            } else if (canSetMin) {
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            }


            datePickerDialog.show();

        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public static void setDateDefault(Context context, final TextView txt_date, int day, int maxDays, boolean canSetMax, boolean canSetMin) {
            final Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, day);
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            if (txt_date.getText().toString().trim().length() > 0) {
                String a[] = txt_date.getText().toString().trim().split("-");
                mYear = Integer.parseInt(a[0]);
                mMonth = Integer.parseInt(a[1]) - 1;
                mDay = Integer.parseInt(a[2]);
            }
            DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int day) {
                            //c.set(year, month, day);
                            P.LogD(year + " " + month + " " + day);
                            String fromDate = P.convertToCustomDateFormat(year, month, day);
                            txt_date.setText(fromDate);
                            txt_date.setError(null);


                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.updateDate(mYear, mMonth, mDay);
            final Calendar cMax = Calendar.getInstance();
            cMax.add(Calendar.DATE, maxDays);
            if (canSetMax) {
                Toast.makeText(context, "maxDays", Toast.LENGTH_LONG).show();
                datePickerDialog.getDatePicker().setMaxDate(cMax.getTimeInMillis());
            }
            if (canSetMin) {
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            }

            datePickerDialog.show();

        }

        public static String getDateYYMMDD() {
            String str = "";
            Calendar c = Calendar.getInstance();
            str = prefixZeroIfSingleDigit(c.get(Calendar.YEAR)) + "-" + prefixZeroIfSingleDigit((c.get(Calendar.MONTH) + 1)) + "-" + prefixZeroIfSingleDigit((c.get(Calendar.DATE)));
            return str;
        }




        public static Calendar getInstance(TextView txt_date1) {
            final Calendar c = Calendar.getInstance();
            if (txt_date1.getText().toString().trim().length() > 0) {
                String a[] = txt_date1.getText().toString().trim().split("-");
                c.set(Calendar.YEAR, Integer.parseInt(a[0]));
                c.set(Calendar.MONTH, Integer.parseInt(a[1]) - 1);
                c.set(Calendar.DATE, Integer.parseInt(a[2]));
            }
            return c;
        }


        public static String prefixZeroIfSingleDigit(String val) {
            return val.length() == 1 ? "0" + val : val;
        }
        public static boolean isValidEmail(String email) {
            return email.matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9_-]+\\.+[a-zA-Z0-9_-]+");
        }
        public static int DATE_POS = 2;
        public static int MONTH_POS = 1;
        public static int YEAR_POS = 0;
        public static String[] getDMY(String str) {
            P.LogD("DATE : " + str);
            String[] dmy = {"", "", ""};
            if (str == null) {
                return dmy;
            }
            if (str.trim().isEmpty()) {
                return dmy;
            }
       /* if(str.trim().length()<11){
            return dmy;
        }*/
            try {
                if (str.contains("-")) {
                    dmy[0] = P.prefixZeroIfSingleDigit(str.split("-")[P.DATE_POS]);
                    dmy[1] = P.getMonthName(Integer.parseInt(str.split("-")[P.MONTH_POS])).substring(0, 3).toUpperCase();
                    dmy[2] = str.split("-")[P.YEAR_POS].substring(0, 4);
                    return dmy;
                } else if (str.contains("/")) {
                    dmy[0] = P.prefixZeroIfSingleDigit(str.split("/")[P.DATE_POS]);
                    dmy[1] = P.getMonthName(Integer.parseInt(str.split("/")[P.MONTH_POS])).substring(0, 3).toUpperCase();
                    dmy[2] = str.split("/")[P.YEAR_POS].substring(0, 4);
                    return dmy;
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                return dmy;
            }
            return dmy;

        }

        public static String[] getDDMM(String str) {
            P.LogD("DATE : " + str);
            String[] dmy = {"", "", ""};
            if (str == null) {
                return dmy;
            }
            if (str.trim().isEmpty()) {
                return dmy;
            }
       /* if (str.trim().length() < 11) {
            return dmy;
        }*/
            try {
                if (str.contains("-")) {
                    dmy[0] = P.prefixZeroIfSingleDigit(str.split("-")[P.DATE_POS]);
                    dmy[1] = P.prefixZeroIfSingleDigit(str.split("-")[P.MONTH_POS]);
                    dmy[2] = str.split("-")[2].substring(0, 4);
                    return dmy;
                } else if (str.contains("/")) {
                    dmy[0] = P.prefixZeroIfSingleDigit(str.split("/")[P.DATE_POS]);
                    dmy[1] = P.prefixZeroIfSingleDigit(str.split("/")[P.MONTH_POS]);
                    dmy[2] = str.split("/")[P.YEAR_POS].substring(0, 4);
                    return dmy;
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                return dmy;
            }
            return dmy;

        }
        public static void hideSoftKeyboard(Activity activity) {
            try {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null)
                    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static String getMonthName(int month) {
            return new DateFormatSymbols().getMonths()[month - 1];
        }

        public static void ShowUnderstandingAlert(Context context, String msg) {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setMessage(msg);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "I Got It",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

    /*public static void setFirstLetterAsImage(CircleImageView circleImageView, ImageView imageView, String customerName) {
        if (customerName == null || customerName.trim().isEmpty()) return;
        TextDrawable drawable = TextDrawable.builder()
                .buildRound("" + customerName.charAt(0), ColorGenerator.MATERIAL.getRandomColor());
        imageView.setImageDrawable(drawable);
        imageView.setVisibility(View.VISIBLE);
        circleImageView.setVisibility(View.GONE);

    }*/

        public static void hasOnlyLetters(final EditText editText) {
            editText.setFilters(new InputFilter[]{
                    new InputFilter() {
                        @Override
                        public CharSequence filter(CharSequence cs, int start,
                                                   int end, Spanned spanned, int dStart, int dEnd) {
                            // TODO Auto-generated method stub
                            if (cs.equals("")) { // for backspace
                                return cs;
                            }
                            if (cs.toString().matches("[a-zA-Z ]+")) {
                                return cs;
                            }
                            return "";
                        }
                    }
            });
        }

        public static void hasOnlyAlphNumeric(final EditText editText) {
            editText.setFilters(new InputFilter[]{
                    new InputFilter() {
                        @Override
                        public CharSequence filter(CharSequence cs, int start,
                                                   int end, Spanned spanned, int dStart, int dEnd) {
                            // TODO Auto-generated method stub
                            if (cs.equals("")) { // for backspace
                                return cs;
                            }
                            if (cs.toString().matches("[a-zA-Z0-9. ]+")) {
                                return cs;
                            }
                            return "";
                        }
                    }
            });
        }

        public static void hasOnlyAlphNumericWithSpecialChars(final EditText editText) {
            editText.setFilters(new InputFilter[]{
                    new InputFilter() {
                        @Override
                        public CharSequence filter(CharSequence cs, int start,
                                                   int end, Spanned spanned, int dStart, int dEnd) {
                            // TODO Auto-generated method stub
                            if (cs.equals("")) { // for backspace
                                return cs;
                            }
                            if (cs.toString().matches("[a-zA-Z0-9.-@^%!`~#$%^&*()+/<> ]+")) {
                                return cs;
                            }
                            return "";
                        }
                    }
            });
        }
    }


