package com.example.colorfulweather.WeatherArea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.colorfulweather.R;
import com.example.colorfulweather.Resource.CityBean;
import com.example.colorfulweather.Resource.InternetResource;
import com.example.colorfulweather.WeatherInfo.MainActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChooseAreaActivity extends AppCompatActivity {
    private static final String KEY = "57017d02ec384ceaaafbb0d4a8406866";
    private static final String CITY_SERVICE = "https://geoapi.qweather.com/v2/city/lookup?number=20&key="+ KEY +"&location=";
    private static final String HOT_SERVICE = "https://geoapi.qweather.com/v2/city/top?key="+ KEY +"&number=12";
    private static final int UPDATE_LIST = 0;
    private static final int UPDATE_VIEW = 1;
    private static final int UPDATE_HOT_CITY = 2;

    interface MyListener{
        void onClick(int pos);
    }

    private RecyclerView hotCity;
    private RecyclerView cityCard;
    private List<CityBean> hotCityList;
    private List<CityBean> cityCardList;
    private HotCityAdapter hotCityAdapter;
    private CityCardAdapter cityCardAdapter;
    private EditText searchBar;
    private ListView listView;
    private List<CityBean> searchList;
    private DynamicPromptAdapter dynamicPromptAdapter;
    private LinearLayout linearLayout;
    private ViewHandler viewHandler = new ViewHandler();

    class ViewHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch(msg.what) {
                case UPDATE_LIST:
                    linearLayout.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    dynamicPromptAdapter.notifyDataSetChanged();
                    break;
                case UPDATE_VIEW:
                    linearLayout.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    break;
                case UPDATE_HOT_CITY:
                    hotCityAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_area);
        Toolbar toolbar = findViewById(R.id.choose_toolBar);
        toolbar.setTitle("切换或添加一个城市");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        InternetResource.sendHttpRequest(HOT_SERVICE, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    try {
                        JSONObject rootObj = new JSONObject(response.body().string());
                        JSONArray rootArr = rootObj.getJSONArray("topCityList");
                        JSONObject cityObj = null;
                        for (int i = 0; i < rootArr.length(); i ++) {
                            cityObj = rootArr.getJSONObject(i);
                            hotCityList.add(new CityBean(cityObj.getString("id"), cityObj.getString("country"), cityObj.getString("adm1"), cityObj.getString("adm2"), cityObj.getString("name")));
                        }
                        viewHandler.sendEmptyMessage(UPDATE_HOT_CITY);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        linearLayout = findViewById(R.id.view);
        searchBar = findViewById(R.id.searchBar);
        listView = findViewById(R.id.search_city_list);
        hotCity = findViewById(R.id.hotCity);
        cityCard = findViewById(R.id.savedCity);
        hotCityList = new ArrayList<>();
        hotCityAdapter = new HotCityAdapter(hotCityList, pos -> {
            goBack(hotCityList.get(pos));
        });
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        hotCity.setLayoutManager(layoutManager);
        hotCity.setAdapter(hotCityAdapter);
        cityCardList = new ArrayList<>();
        cityCardAdapter = new CityCardAdapter(cityCardList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        cityCard.setLayoutManager(manager);
        //cityCard.setAdapter(cityCardAdapter);
        searchList = new ArrayList<>();
        dynamicPromptAdapter = new DynamicPromptAdapter(this, R.layout.item_choose_area_prompt_layout, searchList);
        listView.setAdapter(dynamicPromptAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            goBack(searchList.get(position));
        });
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() < 1 || s.length() > 10) {
                    viewHandler.sendEmptyMessage(UPDATE_VIEW);
                    return;
                }
                InternetResource.sendHttpRequest(CITY_SERVICE + s, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        if(!response.isSuccessful()) {
                            return;
                        }
                        try {
                            JSONObject rootObj = new JSONObject(response.body().string());
                            if(rootObj.getString("code").equals("200")){
                                JSONArray rootArr = rootObj.getJSONArray("location");
                                JSONObject object = null;
                                searchList.clear();
                                for (int i = 0; i < rootArr.length(); i ++){
                                    object = rootArr.getJSONObject(i);
                                    searchList.add(new CityBean(object.getString("id"), object.getString("country"), object.getString("adm1"), object.getString("adm2"), object.getString("name")));
                                }
                                if(searchList.size() > 0) {
                                    viewHandler.sendEmptyMessage(UPDATE_LIST);
                                    return;
                                }
                            }
                            viewHandler.sendEmptyMessage(UPDATE_VIEW);
                        } catch (JSONException e) {
                        }
                    }
                });
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void goBack(CityBean cityBean) {
        Intent intent = new Intent();
        intent.putExtra("city", cityBean);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}