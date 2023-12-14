package com.example.home_training.ui.notifications;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.home_training.R;
import com.example.home_training.ui.login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NotificationPointShop extends AppCompatActivity {

    private String serverUrl = "http://52.79.239.35:4000/";
    private OkHttpClient client = new OkHttpClient();

    private TextView back;
    private ListView items;
    private ArrayList<String> itemTitles;
    private ArrayList<String> itemContent;
    private TextView myPoint;
    private TextView totalPoint;
    private Button buyButton;

    private ArrayList<Product> productList;
    private ArrayList<String> buyList;
    private String MyId;
    private int totalPrice=0;
    private int userP;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_notifications_eventshop);

        back = findViewById(R.id.back);
        items = findViewById(R.id.items);
        myPoint = findViewById(R.id.myPoints);
        totalPoint = findViewById(R.id.totalPoints);
        buyButton = findViewById(R.id.buyButton);

        itemTitles = new ArrayList<>();
        itemContent = new ArrayList<>();

        //뒤로가기
        back.setOnClickListener(v -> onBackPressed());


        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyItems();
            }
        });
        MyId = getIntent().getStringExtra("userId");
        FormBody formBody = new FormBody.Builder()
                .add("userId", MyId)
                .build();

        Request request = new Request.Builder()
                .url(serverUrl+"user/mypage")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();

                try{
                    JSONObject jsonResponse = new JSONObject(responseData);
                    String userNickname = jsonResponse.getString("name");
                    int userPoint = jsonResponse.getInt("point");
                    String uP = String.valueOf(userPoint);
                    String userLevel = jsonResponse.getString("level");
                    userP = userPoint;

                    Log.i(ContentValues.TAG, MyId);
                    Log.i(ContentValues.TAG, userNickname);
                    Log.i(ContentValues.TAG, uP);
                    Log.i(ContentValues.TAG, userLevel);

                    Handler handler = new Handler(Looper.getMainLooper());

                    handler.post(new Runnable() {
                        @SuppressLint("RestrictedApi")
                        @Override
                        public void run() {
                            myPoint.setText(uP);
                            Log.i(TAG, "point받아오기 완료");
                        }
                    });

                }catch (JSONException e){
                    Log.i(TAG, "userPoint 받아오기 실패");
                    e.printStackTrace();
                }
            }
        });
        // 서버에서 데이터 가져오기
        Fetch();

        // 상품 리스트 초기화
        productList = new ArrayList<>();

        // 어댑터 초기화
        productAdapter = new ProductAdapter(this, R.layout.list_item, productList);

        items.setAdapter(productAdapter);


    }

    private static class Product {
        String name;
        String price;
        String productId;

        int quantity;

        @SuppressLint("RestrictedApi")
        Product(String name, String price, String productId, int quantity) {
            this.name = name;
            this.price = price;
            this.productId = productId;
            this.quantity = quantity;
            Log.i(TAG, "생성자 생성완료");
        }
    }

    private class ProductAdapter extends ArrayAdapter<Product>{

        ProductAdapter(Context context, int resource, ArrayList<Product> products) {
            super(context, resource, products);
        }

        @SuppressLint("RestrictedApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_item, null);
                Log.i(TAG, "View is NULL");
            }

            // 리스트뷰의 각 항목에 대한 정보 가져오기
            final Product product = getItem(position);

            // 상품 이름 설정
            TextView productName = view.findViewById(R.id.productName);
            productName.setText(product.name);

            // 상품 가격 설정
            TextView itemPrice = view.findViewById(R.id.productPrice);
            itemPrice.setText(product.price);

            // 상품 수량 설정
            final TextView quantityTextView = view.findViewById(R.id.quantityTextView);
            quantityTextView.setText(String.valueOf(product.quantity));

            // + 버튼 클릭 이벤트 처리
            Button plusButton = view.findViewById(R.id.plusButton);
            plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 수량 증가
                    product.quantity++;
                    // 변경된 수량을 TextView에 업데이트
                    quantityTextView.setText(String.valueOf(product.quantity));
                    totalPrice += Integer.parseInt(product.price);
                    updateTotalPrice();
                }
            });

            // - 버튼 클릭 이벤트 처리
            Button minusButton = view.findViewById(R.id.minusButton);
            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 수량 감소 (음수는 허용하지 않음)
                    if (product.quantity > 0) {
                        product.quantity--;
                        // 변경된 수량을 TextView에 업데이트
                        quantityTextView.setText(String.valueOf(product.quantity));
                        totalPrice -= Integer.parseInt(product.price);
                        updateTotalPrice();
                    }
                }
            });

            return view;
        }
    }

    private void updateTotalPrice(){
        totalPoint.setText(String.valueOf(totalPrice));
    }

    @SuppressLint("RestrictedApi")
    private void Fetch(){
        OkHttpClient client = new OkHttpClient();
        Log.i(TAG, "Fetch 시작");

        Request request = new Request.Builder()
                .url(serverUrl+"item/inquiry") // 실제 API의 엔드포인트로 변경
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "통신 실패");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    try {
                        String responseBody = response.body().string();
                        // JSON 배열 파싱
                        JSONArray jsonArray = new JSONArray(responseBody);
                        Log.i(TAG, "onResponse 작동");

                        // JSONArray에서 title, id, content 추출
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name = jsonObject.getString("name");
                            int id = jsonObject.getInt("itemid");
                            String Id = String.valueOf(id);
                            int price = jsonObject.getInt("price");
                            String Price = String.valueOf(price);
                            itemTitles.add(name + "\n" + price);
                            itemContent.add(String.valueOf(id));
                            itemContent.add(name);
                            itemContent.add(String.valueOf(price));
                            Log.i(TAG, name + ", " + Id + ", " + Price);

                            int quantity = 0;
                            Product product = new Product(name, Price, Id, quantity);
                            productList.add(product);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    productAdapter.notifyDataSetChanged();
                                }
                            });

                            Log.i(TAG, "productList 사이즈 : " + String.valueOf(productList.size()));
                        }
                    }catch (JSONException e) {
                        Log.e(TAG, "onResponse는 작동 되나 배열 파싱 실패");
                        e.printStackTrace();
                    }
                }
            }
        });
    }





    @SuppressLint("RestrictedApi")
    private void BuyItems(){
        if(totalPrice!= 0 && totalPrice <= userP){
            JSONArray selectedItemsArray = new JSONArray();

            for (Product product : productList) {
                if(product.quantity > 0){
                    selectedItemsArray.put(product.name);

                }

            }

            JSONObject requestBodyObject = new JSONObject();
            try {
                requestBodyObject.put("name", selectedItemsArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.i(TAG, requestBodyObject.toString());

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), requestBodyObject.toString());


            int pointleft = userP - totalPrice;

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(serverUrl + "item/pay")
                    .header("userid", MyId)
                    .header("point", String.valueOf(pointleft))
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @SuppressLint("RestrictedApi")
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()){
                        Log.i(TAG, "등록 성공");
                        Intent intent = new Intent(NotificationPointShop.this, NotificationPointShopComplete.class);
                        intent.putExtra("userId", MyId);
                        startActivity(intent);
                    }
                }
            });
        }
    }
}
