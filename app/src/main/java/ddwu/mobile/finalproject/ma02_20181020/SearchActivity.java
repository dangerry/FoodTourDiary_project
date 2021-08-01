package ddwu.mobile.finalproject.ma02_20181020;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    public static final String TAG = "SearchActivity";

    Spinner spinner;
    String apiAddress;
    ListView lvList;

    String query;

    FoodAdapter adapter;
    ArrayList<FoodDto> resultList;
    FoodXmlParser parser;
    FoodNetworkManager networkManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        lvList = findViewById(R.id.lvList);

        resultList = new ArrayList<FoodDto>();
        adapter = new FoodAdapter(this, R.layout.listview_food, resultList);
        lvList.setAdapter(adapter);

        apiAddress = getResources().getString(R.string.food_api_url);
        parser = new FoodXmlParser();
        networkManager = new FoodNetworkManager(this);

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> spinnerAdpater = ArrayAdapter.createFromResource(SearchActivity.this,
                R.array.area, android.R.layout.simple_spinner_item);
        spinnerAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdpater);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0 : query = "1"; break;
                    case 1 :  query = "2"; break;
                    case 2 : query = "3"; break;
                    case 3 : query = "4"; break;
                    case 4 : query = "5"; break;
                    case 5 : query = "6"; break;
                    case 6 : query = "7"; break;
                    case 7 : query = "8"; break;
                    case 8 : query = "31"; break;
                    case 9 : query = "32"; break;
                    case 10 : query = "33"; break;
                    case 11 :  query = "34"; break;
                    case 12 : query = "35"; break;
                    case 13 : query = "36"; break;
                    case 14 : query = "37"; break;
                    case 15 : query = "38"; break;
                    case 16 : query = "39"; break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnSearch:
                try {
                    new NetworkAsyncTask().execute(apiAddress + URLEncoder.encode(query, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnSearchOk:
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btnReviewGo:
                Intent rIntent = new Intent(SearchActivity.this, ReviewActivity.class);
                startActivity(rIntent);
                break;
        }
    }

    class NetworkAsyncTask extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDlg;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDlg = ProgressDialog.show(SearchActivity.this, "Wait", "Downloading...");
        }

        @Override
        protected String doInBackground(String... strings) {
            String address = strings[0];
            String result = null;
            result = networkManager.downloadContents(address);
            if (result == null)
                return "Error!";

            Log.d(TAG, result);
            resultList = parser.parse(result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            adapter.setList(resultList);
            progressDlg.dismiss();
        }
    }

}
