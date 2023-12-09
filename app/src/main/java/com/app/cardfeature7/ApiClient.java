package com.app.cardfeature7;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ApiClient extends AsyncTask<File, Void, String> {
    private final String url;
    final Context context;
    final File file;
    private ProgressDialog progressDialog;
    private final String childUID;
//    private final String motherUID;

    public ApiClient(String url, Context context,File file, String childUID) {
        this.url = url;
        this.context=context;
        this.file=file;
        this.childUID= childUID;
//        this.motherUID = motherUID;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading..."); // Set the message you want to display
        progressDialog.setIndeterminate(true);
    }
    @Override
    protected String doInBackground(File... files) {
        try {
            File imageFile = files[0];

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", imageFile.getName(),
                            RequestBody.create(MediaType.parse("image/*"), imageFile))
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            OkHttpClient okHttpClient = new OkHttpClient();
            Call call = okHttpClient.newCall(request);

            Response response = call.execute(); // Synchronous call

            if (response.isSuccessful()) {
                String responseString = response.body().string();
                return responseString;
            } else {
                return response.message();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
    @Override
    protected void onPreExecute() {
        progressDialog.show();
    }
    @Override
    protected void onPostExecute(String response) {
        progressDialog.dismiss();
        try {
            JSONObject jsonResponse = new JSONObject(response);
            String emotion = jsonResponse.optString("emotion", "Emotion not found");

            // Pass both child and mother UIDs to the uploadData method
//            new FirebaseServices(context).uploadData(emotion, childUID);
            // Rest of your code...
            showResultDialog(emotion);
        } catch (JSONException e) {
            e.printStackTrace();
            showErrorMessageDialog("Something went wrong! try again", context);
        }
    }
    private void showResultDialog(String response) {


        new FirebaseServices(context).uploadData(response,childUID);
        AlertDialog.Builder builder = new AlertDialog.Builder(context); // Replace 'context' with your actual context
        builder.setTitle("");
        // Create a custom layout for the dialog
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogLayout = inflater.inflate(R.layout.result_dialog, null);
        builder.setView(dialogLayout);
        // Find and update the ImageView in the custom layout
        ImageView resultImageView = dialogLayout.findViewById(R.id.resultImageView);
        Bitmap resultBitmap = BitmapFactory.decodeFile(file.getAbsolutePath()); // Replace with the actual image path
        resultImageView.setImageBitmap(resultBitmap);
        // Find and update the TextView in the custom layout with the response
//        TextView resultTextView = dialogLayout.findViewById(R.id.resultTextView);


//
//        Typeface customFont = Typeface.createFromFile("assets/Anton-Regular.ttf");
//        resultTextView.setTypeface(customFont);




//        String res=response.equals("Angry")?"You'r Angry Now":response.equals("Happy")?"You'r Happy Now":
//                response.equals("Natural")?"Natural Emotions"    :   response.equals("Sad")? "You'r Sad Now": "Can't detect Emotions"
//                ;


        ImageView emoji = dialogLayout.findViewById(R.id.emoji);

        emoji.setImageResource(response.equals("Angry")?R.drawable.angry12:response.equals("Happy")?R.drawable.happiness:
                response.equals("Natural")?R.drawable.neutral:response.equals("Sad")? R.drawable.sad:R.drawable.error);

//        resultTextView.setText(res);



        ImageView resImage = dialogLayout.findViewById(R.id.resText);
        resImage.setImageResource(response.equals("Angry")?R.drawable.ang:response.equals("Happy")?R.drawable.hap:
                response.equals("Natural")?R.drawable.nut:response.equals("Sad")? R.drawable.sadd:R.drawable.err);

        AppCompatButton button=dialogLayout.findViewById(R.id.ok_button);

        Toast.makeText(context,response,Toast.LENGTH_SHORT).show();

        // Add a button to dismiss the dialog
        builder.setPositiveButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();


//        builder.show();
    }
    static void showErrorMessageDialog(String errorMessage,Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context); // Replace 'context' with your actual context
        builder.setTitle("Error");
        builder.setMessage(errorMessage);
        builder.setPositiveButton("OK", null);
        builder.show();
    }


}
