package bitwyze.nytimesreader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Filter;

import bitwyze.nytimesreader.models.Article;
import bitwyze.nytimesreader.adapters.ArticleArrayAdapter;
import bitwyze.nytimesreader.fragments.FilterFragment;
import bitwyze.nytimesreader.R;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity  implements FilterFragment.FilterDialogListener  {
    Button btnSearch;
    EditText etQuery;
    GridView gvResults;
    ArticleArrayAdapter adapter;
    ArrayList<Article> articles;
    private Date sortDate;
    private String sortCriteria;
    private String category = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
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
            FragmentManager fm = getSupportFragmentManager();
            FilterFragment filterFragment= FilterFragment.newInstance("Filter Settings",category,sortCriteria);
            filterFragment.show(fm, "fragment_edit_name");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setupViews() {
        btnSearch = (Button)findViewById(R.id.searchButton);
        etQuery = (EditText)findViewById(R.id.editText);
        gvResults = (GridView)findViewById(R.id.gridView);
        articles = new ArrayList<Article>();
        articles.clear();
        adapter = new ArticleArrayAdapter(this,articles);
        gvResults.setAdapter(adapter);

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);

                Article article = articles.get(position);
                intent.putExtra("article", article);
                startActivity(intent);
            }
        });
    }

    public void onSearchArticles(View view) {
        adapter.clear();
        String query = etQuery.getText().toString();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();
        params.put("api-key", "87583283703463b149317caa7e61eb77:14:60732484");
        params.put("page",0);
        if (query != null && query.length()>0) {
            params.put("q", query);
        }
        if (this.sortCriteria != null && this.sortCriteria.length() > 0) {
            params.put("sort",sortCriteria);
        }
        if (this.category != null && this.category.length() > 0 && this.category != "All") {
            String filteredQuery = "section_name(\"" + this.category + "\")";
            params.put("fq", filteredQuery);
        }
        Log.d("MainActivity", "params = " + params.toString());
        client.get(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        JSONArray articleResults = null;
                        try {
                            articleResults = response.getJSONObject("response").getJSONArray("docs");
                            Log.d("MainActivity", articleResults.toString());
                            adapter.addAll(Article.fromJSONArray(articleResults));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("DEBUG", "ERROR: " + errorResponse.toString());
                    }
                }
        );
    }


    public void onFinishFilterDialog(String category,String sortCriteria,Date startDate) {
        this.sortCriteria = sortCriteria;
        this.sortDate = startDate;
        this.category = category;
    }


}
