package com.thesis.shakeshare;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.shakeshare.R;
import com.thesis.domain.User;
import com.thesis.extractKey.FFT;
import com.thesis.extractKey.Functions;
import com.thesis.login.ContactsActivity;
import com.thesis.util.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.content.DialogInterface.OnClickListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import weborb.util.log.LogHelper;

public class EntryActivity extends Activity {

    private String username;
    private String contactname;
    private User mUser;
    private User mContactUser;

    private SensorManager mSensorManager;
    private Sensor linear_accelerometer;
    private MySensorEventListener mAccuEventListener = new MySensorEventListener();

    private float[] linear_accelerometerValues = new float[3];
    private ArrayList<ArrayList<Float>> mAccuData = new ArrayList<ArrayList<Float>>();
    private ArrayList<Long> mTimeData = new ArrayList<Long>();
    private int mSensorNum = 1024;
    private String mAllNumber;
    private boolean mRecordTag = false;
    private StringBuffer mkeySb = new StringBuffer();

    private int mWindowLength = 512;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        Log.d(Utils.TAG,"EntryActivity onCreate");

        Intent intent = getIntent();
        mUser = intent.getParcelableExtra("user");
        mContactUser = intent.getParcelableExtra("contactUser");
        username = mUser.getName();
        contactname = mContactUser.getName();

        mAccuData.add(new ArrayList<Float>());
        mAccuData.add(new ArrayList<Float>());
        mAccuData.add(new ArrayList<Float>());
        startSensor();
    }

    private void startSensor() {

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION) != null)
            linear_accelerometer = mSensorManager
                    .getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
//		if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null)
//			linear_accelerometer = mSensorManager
//					.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        else Toast.makeText(
                EntryActivity.this,
                "Sorry, but there is no Linear Acceleration Sensor on your device",
                Toast.LENGTH_SHORT).show();

        mSensorManager.registerListener(mAccuEventListener, linear_accelerometer,
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    class MySensorEventListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {

            if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
                linear_accelerometerValues = event.values;
                record();
            }
//			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//				linear_accelerometerValues = event.values;
//				record();
//			}
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    private void record() {

        if (checkEntropySize()) {
            mSensorManager.unregisterListener(mAccuEventListener);
            StringBuffer sBuffer = new StringBuffer();

            for (Long data : mTimeData) {
                sBuffer.append(data);
                sBuffer.append("; ");
            }
            for (Float data : mAccuData.get(0)) {
                sBuffer.append(data);
                sBuffer.append("; ");
            }
            for (Float data : mAccuData.get(1)) {
                sBuffer.append(data);
                sBuffer.append("; ");
            }
            for (Float data : mAccuData.get(2)) {
                sBuffer.append(data);
                sBuffer.append("; ");
            }

            mAllNumber = sBuffer.toString();
            saveDataOnSDCard();
            generateKey();
        } else recordData();
    }

    private void recordData() {

        if (mRecordTag == false && Math.abs(linear_accelerometerValues[0]) > 10.0) {
            playNotification();
            mTimeData.add(System.currentTimeMillis());
            // System.out.println("First" + accelerometerValues[0]);
//			mAccuData.get(0).add(Math.abs(linear_accelerometerValues[0]));
            mAccuData.get(0).add(linear_accelerometerValues[0]);
//			mAccuData.get(1).add(Math.abs(linear_accelerometerValues[1]));
            mAccuData.get(1).add(linear_accelerometerValues[1]);
//			mAccuData.get(2).add(Math.abs(linear_accelerometerValues[2]));
            mAccuData.get(2).add(linear_accelerometerValues[2]);
//			mAccuData.get(0).add((float)Math.sqrt(
//								 (float)Math.pow(linear_accelerometerValues[0],2) 
//							   + (float)Math.pow(linear_accelerometerValues[1],2) 
//							   + (float)Math.pow(linear_accelerometerValues[2],2)));
            mRecordTag = true;
        } else if (mRecordTag == true) {
            //System.out.println("Started" + linear_accelerometerValues[0]);
            mTimeData.add(System.currentTimeMillis());
//			mAccuData.get(0).add(Math.abs(linear_accelerometerValues[0]));
            mAccuData.get(0).add(linear_accelerometerValues[0]);
//			mAccuData.get(1).add(Math.abs(linear_accelerometerValues[1]));
            mAccuData.get(1).add(linear_accelerometerValues[1]);
//			mAccuData.get(2).add(Math.abs(linear_accelerometerValues[2]));
            mAccuData.get(2).add(linear_accelerometerValues[2]);
//			mAccuData.get(0).add((float)Math.sqrt(
//					 (float)Math.pow(linear_accelerometerValues[0],2) 
//				   + (float)Math.pow(linear_accelerometerValues[1],2) 
//				   + (float)Math.pow(linear_accelerometerValues[2],2)));
        } else {
            // System.out.println("Not Start Yet"+accelerometerValues[0]);
        }
    }

    private boolean checkEntropySize() {

        if (mAccuData.get(0).size() >= mSensorNum) {
            return true;
        }
        return false;
    }

    protected void onResume() {
        mSensorManager.registerListener(mAccuEventListener, linear_accelerometer,
                Sensor.TYPE_LINEAR_ACCELERATION);
//		mSensorManager.registerListener(mAccuEventListener, linear_accelerometer,
//				Sensor.TYPE_ACCELEROMETER);
        super.onResume();
    }

    protected void onPause() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(mAccuEventListener);
        }
        super.onPause();
    }

    protected void onStop() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(mAccuEventListener);
        }
        super.onStop();
    }

    protected void onDestroy() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(mAccuEventListener);
        }
        super.onDestroy();
    }

    protected void saveDataOnSDCard() {
        File savedData = new File("/sdcard/Download/savedData.txt");
        try {
            FileOutputStream fos = new FileOutputStream(savedData);
            fos.write((mAllNumber).getBytes());
            fos.close();
            Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, "Not Found", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, "IO Failed", Toast.LENGTH_LONG).show();
        }
    }

    public void generateKey() {
        Builder builder = new Builder(EntryActivity.this);
        builder.setTitle("Shake Well");
        /*
        mkeySb.append("Your Key is" + '\n' + "X axis: "
				+ FFT.peakFft(mAccuData.get(0)) + '\n' + "Y axis : "
				+ FFT.peakFft(mAccuData.get(1)) + '\n' + "Z axis : "
				+ FFT.peakFft(mAccuData.get(2)));
		*/
        List<List<Float>> xSegments = Functions.equalSplitArrayList(mAccuData.get(0), mWindowLength);
        List<List<Float>> ySegments = Functions.equalSplitArrayList(mAccuData.get(1), mWindowLength);
        List<List<Float>> zSegments = Functions.equalSplitArrayList(mAccuData.get(2), mWindowLength);

        int byteLength = 3*(mSensorNum / mWindowLength);

        byte[] keyArray = new byte[byteLength];

        byteLength--;

        for (int i = 0; i < mSensorNum / mWindowLength; i++) {
            mkeySb.append('\n' + "X segment " + i + " : "
                    + FFT.peakFft(xSegments.get(i)));
            mkeySb.append('\n' + "Y segment " + i + " : "
                    + FFT.peakFft(ySegments.get(i)));
            mkeySb.append('\n' + "Z segment " + i + " : "
                    + FFT.peakFft(zSegments.get(i)));
            keyArray[byteLength] = (byte) FFT.peakFft(xSegments.get(i));
            keyArray[byteLength-1] = (byte) FFT.peakFft(ySegments.get(i));
            keyArray[byteLength-2] = (byte) FFT.peakFft(zSegments.get(i));
            byteLength = byteLength-3;
        }

        final String masterKey = Utils.bytesToHex(keyArray);

        Log.d(Utils.TAG, "masterKey:"+masterKey);


        builder.setMessage(mkeySb.toString()).setCancelable(false)
                .setPositiveButton("Confirm", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(Utils.TAG, "KEY:" + mkeySb.toString());
                        Intent returnIntent = new Intent(EntryActivity.this, ContactsActivity.class);
                        returnIntent.putExtra("masterKey", masterKey);
                        setResult(Activity.RESULT_OK, returnIntent);
                        dialog.dismiss();
                        finish();
                    }
                })
                .setNeutralButton("Redo", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(EntryActivity.this,
                                EntryActivity.class);
                        intent.putExtra("user", mUser);
                        intent.putExtra("contactUser", mContactUser);
                        startActivity(intent);
                        dialog.dismiss();
                        finish();
//						dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent returnIntent = new Intent(EntryActivity.this, ContactsActivity.class);
                        setResult(Activity.RESULT_CANCELED, returnIntent);
                        dialog.dismiss();
                        finish();
//						dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        playNotification();
    }

    public void playNotification() {
        Uri notification = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
                notification);
        r.play();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
