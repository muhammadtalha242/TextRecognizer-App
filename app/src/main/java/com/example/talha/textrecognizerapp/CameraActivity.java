package com.example.talha.textrecognizerapp;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talha.textrecognizerapp.Interface.Strategy;
import com.example.talha.textrecognizerapp.classes.FileContext;
import com.example.talha.textrecognizerapp.classes.FileWrite;
import com.example.talha.textrecognizerapp.classes.fileHandler;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.Locale;

import static com.example.talha.textrecognizerapp.classes.fileHandler.verifyStoragePermissions;

public class CameraActivity extends AppCompatActivity {


    private static final String TAG = "Inside Camera Activity";
    TextRecognizer textRecognizer;
    TextView mTextView;
    String textDisplay;
    FileContext fileContext = new FileContext();
    Strategy strategy;
    private TextToSpeech tts;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mTextView = findViewById(R.id.text_view);

        textRecognizer = getTextRecognizer();
        startCameraSource(textRecognizer);
        textProcessing(textRecognizer);


        // Set up the Text To Speech engine.
        TextToSpeech.OnInitListener listener =
                new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(final int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            Log.d("OnInitListener", "Text to speech engine started successfully.");
                            System.out.println("Text to speech engine started successfully.");
                            tts.setLanguage(Locale.US);
                        } else {
                            Log.d("OnInitListener", "Error starting the text to speech engine.");
                            System.out.println("Error starting the text to speech engine.");

                        }
                    }
                };
        tts = new TextToSpeech(this.getApplicationContext(), listener);

    }

    private TextRecognizer getTextRecognizer(){
        if (textRecognizer == null){
            return new TextRecognizer.Builder(getApplicationContext()).build();
        }
        else{
            return textRecognizer;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "Inside onResume()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "Inside onDestroy()");
    }

    private void startCameraSource(TextRecognizer textRecognizer) {

        //Create the TextRecognizer

        if (!textRecognizer.isOperational()) {
            Log.w(TAG, "Detector dependencies not loaded yet");
        } else {

            //Initialize cameraSource to use high resolution and set AutoFocus on.
            Log.w(TAG, "TextRecognizer is working.");
            final CameraSource mCameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();


            /*
             * Add call back to SurfaceView and check if camera permission is granted.
             * If permission is granted we can start our cameraSource and pass it to surfaceView
             */

            final SurfaceView mCameraView = findViewById(R.id.surfaceView);
            mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(CameraActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    2);
                            return;
                        }
                        mCameraSource.start(mCameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                /**
                 * Release resources for cameraSource
                 */
                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mCameraSource.stop();
                }
            });


        }
    }
    private void textProcessing( TextRecognizer textRecognizer){
        //Set the TextRecognizer's Processor.
        textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
            @Override
            public void release() {
            }

            /**
             * Detect all the text from camera using TextBlock and the values into a stringBuilder
             * which will then be set to the textView.
             * */
            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections) {
                final SparseArray<TextBlock> items = detections.getDetectedItems();
                if (items.size() != 0) {


                    mTextView.post(new Runnable() {
                        @Override
                        public void run() {
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 0; i < items.size(); i++) {
                                TextBlock item = items.valueAt(i);
                                stringBuilder.append(item.getValue());
                                stringBuilder.append("\n");
                                textDisplay= stringBuilder.toString();
                            }
                        }
                    });
                }
            }
        });
    }





    public void displayMessage(View view) {
        System.out.println("Inside displayMessage(): CameraActivity: textDisplay: " + textDisplay);

        mTextView.setText(textDisplay);


    }


    public void saveText(View view) {

        strategy = new FileWrite();
        fileContext.setStrategy(strategy);


        verifyStoragePermissions(this);
        if (fileHandler.saveToFile(mTextView.getText().toString())) {
            Toast.makeText(CameraActivity.this, "Saved to file", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(CameraActivity.this, "Error save file!!!", Toast.LENGTH_SHORT).show();
        }

    }


    public void readImage(View view) {
        mTextView.setText(textDisplay);

    }


    public void launchPDFViewer(View view) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse(fileHandler.getFilepath());
            intent.setDataAndType(uri, "application/pdf");
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast toast = Toast.makeText(this, "No viewer on this device", Toast.LENGTH_SHORT);
            toast.show();
        }
    }




}
    /**
     * Read file data in the app
     *
     * public void readFile(View view) {
     *
     *         strategy= new FileRead();
     *         fileContext.setStrategy(strategy);
     *
     *         Intent intent = new Intent(this, FileDisplay.class);
     *         startActivity(intent);
     *
     *     }
     */

    /**
     * Read file in Text Editor
     *
     *  public void launchTextEditor(View view) {
     *         try {
     *             Intent intent = new Intent(Intent.ACTION_EDIT);
     *             Uri uri = Uri.parse(fileHandler.getFilepath());
     *             intent.setDataAndType(uri, "text/plain");
     *             startActivity(intent);
     *         } catch (ActivityNotFoundException e) {
     *             Toast toast = Toast.makeText(this, "No editor on this device", Toast.LENGTH_SHORT);
     *             toast.show();
     *         }
     *     }
     *
     */

