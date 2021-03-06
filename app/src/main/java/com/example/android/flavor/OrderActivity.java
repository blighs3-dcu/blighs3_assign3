package com.example.android.flavor;

import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.Environment;
        import android.provider.MediaStore;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.text.InputType;
        import android.util.Log;
        import android.view.View;
        import android.view.inputmethod.EditorInfo;
        import android.widget.ArrayAdapter;
        import android.widget.EditText;
        import android.widget.Spinner;
        import android.widget.Toast;

        import java.io.File;
        import java.text.SimpleDateFormat;
        import java.util.Date;



/**
 * @author      Colette Kirwan  <Colette.kirwan@dcu.ie>
 * @version     1.0   (current version number of program)
 * @since       Unknown         (the version of the package this class was first added to)
 */

public class OrderActivity extends AppCompatActivity
    //extending from AppCompactActivity allows for use of action bar
{
    Uri mPhotoURI;
    //spinner object declared
    Spinner mSpinner;
    //2 editText objects declared
    EditText mCustomerName;
    EditText meditOptional;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 2;
    private static final String TAG = "Assign3";


    /**
     * main user interface, activity_order xml file provides layout
     * arrayadapter converts arraylist of objects into options for spinner object
     * @param savedInstanceState data from previous saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        //using findViewById to make reference to editOptional editText in activity_order.xml file
        meditOptional = (EditText) findViewById(R.id.editOptional);
        //DONE keyboard option to be visible
        meditOptional.setImeOptions(EditorInfo.IME_ACTION_DONE);
        //setting input type to be text
        meditOptional.setRawInputType(InputType.TYPE_CLASS_TEXT);

        //initialise spinner using the integer array
        mSpinner = (Spinner) findViewById(R.id.spinner);
        //reference to editText in xml file
        mCustomerName = (EditText) findViewById(R.id.editCustomer);

        // Create an ArrayAdapter using the string array and a default spinner layout
        //using string array in strings.xml for values in spinner object
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ui_time_entries, R.layout.spinner_days);
        mSpinner.setAdapter(adapter);

    }

    /**
     *  method to start explicit intent to take and store photo
     *  photo is saved using specified time stamp format
        and then retrieving photo from local storage
     * @param v is this view
     */
    public void dispatchTakePictureIntent(View v)
    {
        //explicit intent to take and store photo
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //object from SimpleDateFormat class to format date and time as desired
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        //string saving timestamp to create unique file name
        String imageFileName = "my_tshirt_image_" + timeStamp + ".jpg";

        //info message for logcat
        Log.i(TAG, "imagefile");

        //retrieving saved photo from local storage
        File file = new File(Environment.getExternalStorageDirectory(), imageFileName);

        //assigning uri created from "file" to mPhotoURI
        mPhotoURI = Uri.fromFile(file);

        //logcat message showing mPhotoURI name
        Log.i(TAG, mPhotoURI.toString());

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoURI);

        //starting intent to return photo using result code parameter
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        //incase of caching if it comes from the activity stack, just a precaution
        intent.removeExtra(MediaStore.EXTRA_OUTPUT);

    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        //also can give user a message that everything went ok
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK)
        {
            //let user know that image saved
            //I have strings in strings.xml but have hardcoded here to copy/paste to students if needed
            CharSequence text = "Image Taken successfully";
            int duration = Toast.LENGTH_SHORT;

            //short toast message that uses text object above as output
            Toast toast = Toast.makeText(this, text, duration);
            toast.show();

            //or perhaps do a dialog should only use one method i.e. toast or dialog, but have both code here for demo purposes
            //also I have strings in strings.xml but have hardcoded here to copy/paste to students if needed
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Notification!").setMessage("Image saved successfully.").setPositiveButton("OK", null).show();
        }


    }

    /**
     * Returns the Email Body Message.
     * <p> Email body message is created used prescription related data inputed from user </p>
     *
     * @return Email Body Message
     */
    private String createOrderSummary()
    {

        String orderMessage = getString(R.string.customer_name) + " " + mCustomerName.getText().toString();
        orderMessage += "\n" + "\n" + getString(R.string.order_message_1);
        String optionalInstructions = meditOptional.getText().toString();

        orderMessage += "\n" + getString(R.string.order_message_collect) + ((CharSequence) mSpinner.getSelectedItem()).toString() + " days";
        orderMessage += "\n" + getString(R.string.order_message_end) + "\n" + mCustomerName.getText().toString();
        return orderMessage;

        //update screen
    }

    /**
     *  method for sending email
     * @param v is this view
     */

    public void sendEmail(View v)
    {

        //check that Name is not empty, and ask do they want to continue

        String customerName = mCustomerName.getText().toString();
        if (customerName.matches(""))
        {
            Toast.makeText(this, getString(R.string.customer_name_blank), Toast.LENGTH_SHORT).show();

            /* we can also use a dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Notification!").setMessage("Customer Name not set.").setPositiveButton("OK", null).show();
            */
        } else
        {
            //intent to populate fields of draft email
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.to_email)});
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject));
            intent.putExtra(Intent.EXTRA_STREAM, mPhotoURI);
            intent.putExtra(Intent.EXTRA_TEXT, createOrderSummary());
            if (intent.resolveActivity(getPackageManager()) != null)
            {
                startActivity(intent);
            }
        }
    }


}
