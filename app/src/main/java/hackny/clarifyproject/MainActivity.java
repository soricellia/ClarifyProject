package hackny.clarifyproject;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clarifai.api.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.filepicker.Filepicker;
import io.filepicker.models.FPFile;


public class MainActivity extends ActionBarActivity {

    final String CLIENT_ID = "IPIouJlAdigVspYS0bKtPMQr5FAryw7fwq9s9lDl";
    final String CLIENT_SECRET = "7yRmlnqGQ-Fz4v-UewzFZYizeZON6G7yFGcPgtyj";
    final String FILEPICKER_KEY = "AtPxVCa4QTxi82DXFa3ZSz";
    final String I_HEART_HACK_NY = "i <3 hackNY";
    Context context = this;
    TextView view;
    String copyHashTags = "";
    DataManager dm;

    //this is the adapter that connects are page fragments to the view pager

    //this is the layour manager thats going to allow the user to flip back and forth between
    //tabs
    ViewPager mViewPager;

    List<HashTag> hashTags = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dm = new DataManager();
        Filepicker.setKey(FILEPICKER_KEY);
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
    public void onCheckClicked(View view){
        CheckBox cb = (CheckBox) view.findViewById(R.id.checkbox);

        for(int i =0; i < hashTags.size(); i++){
            if(hashTags.get(i).getTagName().equals(cb.getText())){
                hashTags.get(i).select(cb.isSelected());
            }
        }
    }
    public void onCopyClick(View view) {
        copyHashTags = "";
        for(int x = 0 ; x < hashTags.size() ; x++ ){
            Log.v("test123",hashTags.get(x).getTagName()+" "+ hashTags.get(x).isSelected());
            if(hashTags.get(x).isSelected()) {
                copyHashTags = copyHashTags +" "+ hashTags.get(x).getTagName();
                }
        }
        ClipboardManager clipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("simple text", copyHashTags);
        clipboard.setPrimaryClip(clip);
        Log.v("test123", copyHashTags);
        Toast.makeText(getApplicationContext(), "Hashtags copied to Clipboard",
                Toast.LENGTH_LONG).show();

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
                hashTags.add(new HashTag(tag.getName(),dm));
                //Log.v("test 123",tag.getName() + ": " + tag.getProbability());
            }
            return null;
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param integer The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            ArrayAdapter<HashTag> tagArrayAdapter =
                    new TagArrayAdapter(context, 0, hashTags);
            ListView listView = (ListView) findViewById(android.R.id.list);
            listView.setAdapter(tagArrayAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    hashTags.get(position).select(!hashTags.get(position).isSelected());
                    TextView tv = (TextView) view.findViewById(R.id.tag);
                    if(hashTags.get(position).isSelected())
                        tv.setTextColor(Color.RED);
                    else
                        tv.setTextColor(Color.BLACK);
                }
            });

        }

    }
    class TagArrayAdapter extends ArrayAdapter<HashTag> {

        Context context;
        List<HashTag> hashTags;

        public TagArrayAdapter(Context context, int resource, List<HashTag> hashTags) {
            super(context, resource, hashTags);

            this.context = context;
            this.hashTags = hashTags;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            HashTag tag = hashTags.get(position);

            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.tag_layout, null);

            TextView tv = (TextView) view.findViewById(R.id.tag);
            tv.setText(tag.getTagName());
            tv.setTextColor(Color.BLACK);
            return view;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }
    }
}
