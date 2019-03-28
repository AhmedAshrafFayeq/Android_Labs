package com.example.josn_with_image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    ImageView img;
    Button next,pre;
    TextView rank,population,country;
    static int index=0;
    List<CountryModel>listOfCountries=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img=findViewById(R.id.myImage);
        next=findViewById(R.id.nextBtn);
        pre=findViewById(R.id.previousBtn);
        rank=findViewById(R.id.rankTxt);
        population=findViewById(R.id.popTxt);
        country=findViewById(R.id.countryTxt);

        new MyJSONReader().execute("http://www.androidbegin.com/tutorial/jsonparsetutorial.txt");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index==9)
                    index=0;
                else
                    index++;
                showJson(index);
                //Toast.makeText(MainActivity.this, String.valueOf(index), Toast.LENGTH_SHORT).show();
            }
        });
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index==0)
                    index=9;
                else
                    index--;
                showJson(index);
                //Toast.makeText(MainActivity.this, String.valueOf(index), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public class MyJSONReader  extends AsyncTask<String , Void ,List<CountryModel>>
    {

        HttpURLConnection connection=null;
        BufferedReader reader;
        @Override
        protected List<CountryModel> doInBackground(String... strings) {

            try {
                URL url=new URL(strings[0]);

                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream is=connection.getInputStream();
                reader=new BufferedReader(new InputStreamReader(is));
                StringBuffer buffer=new StringBuffer();
                String line="";
                while((line=reader.readLine())!=null)
                {
                    buffer.append(line);
                }
                String allJSONFile=buffer.toString();

                if(allJSONFile!=null) {
                    JSONObject jsonObject = new JSONObject(allJSONFile);
                    JSONArray jsonArray=jsonObject.getJSONArray("worldpopulation");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        CountryModel obj=new CountryModel();
                        obj.setRank(c.getInt("rank"));
                        obj.setCountry(c.getString("country"));
                        obj.setPop(c.getString("population"));
                        obj.setCountryImage(c.getString("flag"));

                        listOfCountries.add(obj);
                    }
                }
                return listOfCountries;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if(connection!=null)
                    connection.disconnect();
                if(reader!=null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }




        @Override
        protected void onPostExecute(List<CountryModel> countryModel) {
            super.onPostExecute(countryModel);
            showJson(index);

        }
    }
    private void showJson(int index) {

        rank.setText(String.valueOf(listOfCountries.get(index).getRank()));
        population.setText(String.valueOf(listOfCountries.get(index).getPop()));
        country.setText(String.valueOf(listOfCountries.get(index).getCountry()));

        String text1 = listOfCountries.get(index).getCountryImage();
        String text2 = text1.toString().replace("http", "https");
        new DownloadImageTask().execute(text2);
    }

    public class DownloadImageTask extends AsyncTask<String , Void, Bitmap> {
        public DownloadImageTask(){

        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap myImage=download(urls[0]);
            return myImage;
        }

        private Bitmap download(String url) {
            Bitmap result=null;
            URL myURL;
            HttpsURLConnection httpConnection;
            InputStream inputStream=null;

            try {
                myURL=new URL(url);
                httpConnection=(HttpsURLConnection)myURL.openConnection();
                httpConnection.connect();
                inputStream=httpConnection.getInputStream();
                result= BitmapFactory.decodeStream(inputStream);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(inputStream!=null)
                {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            return result;
        }
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            img.setImageBitmap(bitmap);
            //Toast.makeText(MainActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
        }


    }






}
