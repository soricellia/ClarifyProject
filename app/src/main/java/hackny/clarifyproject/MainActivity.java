package hackny.clarifyproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.clarifai.api.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.filepicker.Filepicker;
import io.filepicker.models.FPFile;


public class MainActivity extends ActionBarActivity {

    final String CLIENT_ID = "IPIouJlAdigVspYS0bKtPMQr5FAryw7fwq9s9lDl";
    final String CLIENT_SECRET = "7yRmlnqGQ-Fz4v-UewzFZYizeZON6G7yFGcPgtyj";
    final String FILEPICKER_KEY = "AtPxVCa4QTxi82DXFa3ZSz";
    final String I_HEART_HACK_NY = "i <3 hackNY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Filepicker.setKey(FILEPICKER_KEY);
        Filepicker.setAppName(I_HEART_HACK_NY);

        Intent intent = new Intent(this, Filepicker.class);
        startActivityForResult(intent, Filepicker.REQUEST_CODE_GETFILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Filepicker.REQUEST_CODE_GETFILE) {
            if(resultCode == RESULT_OK) {

                // Filepicker always returns array of FPFile objects
                ArrayList<FPFile> fpFiles = data.getParcelableArrayListExtra(Filepicker.FPFILES_EXTRA);

                // Option multiple was not set so only 1 object is expected
                FPFile file = fpFiles.get(0);


                // Do something cool with the result
                ClarifyTask task = new ClarifyTask();
                task.execute(file.getUrl());

            } else {
                // Handle errors here
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public class ClarifyTask extends AsyncTask<String, Integer, Integer>{

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Integer doInBackground(String... params) {
            ClarifaiClient clarifai = new ClarifaiClient(CLIENT_ID, CLIENT_SECRET);

            List<RecognitionResult> results =
                    clarifai.recognize(new RecognitionRequest(params[0]));

            for (Tag tag : results.get(0).getTags()) {
                Log.v("test 123",tag.getName() + ": " + tag.getProbability());
            }
            return null;
        }
    }
}
