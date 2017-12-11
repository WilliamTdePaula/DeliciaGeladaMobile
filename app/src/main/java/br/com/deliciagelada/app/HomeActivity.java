package br.com.deliciagelada.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    ListView list_view;
    ProdutoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list_view = (ListView) findViewById(R.id.list_view);

        //criar adapter
        adapter = new ProdutoAdapter(this, new ArrayList<Produto>());

        //definir adapter na lista
        list_view.setAdapter(adapter);

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Produto item = adapter.getItem(i);

                Intent intent = new Intent(getApplicationContext(), DetalhesActivity.class);

                intent.putExtra("item", item);

                startActivity(intent);

            }
        });
        new AsyncTask<Void, Void, Void>(){

            ArrayList<Produto> lstProdutos = new ArrayList<Produto>();

            @Override
            protected Void doInBackground(Void... voids) {

                String retornoJson = Http.get("http://10.0.2.2/inf3m/turmaB/Projeto%20Delicia%20Gelada/selecionar.php");

                Log.d("TAG", retornoJson);

                try {
                    JSONArray jsonArray = new JSONArray(retornoJson);

                    for(int i =0; i < jsonArray.length(); i++){

                        JSONObject item = jsonArray.getJSONObject(i);

                        Produto c = Produto.create(
                                item.getInt("idProduto"),
                                item.getString("nome"),
                                item.getString("descricao"),
                                item.getDouble("preco"),
                                item.getString("caminhoImagem"),
                                item.getString("infoAdd"));

                        lstProdutos.add(c);
                    }

                    Log.d("TAG", lstProdutos.size()+"");
                }catch (Exception ex){
                    Log.e("Erro: ", ex.getMessage());
                }

                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.addAll(lstProdutos);
            }
        }.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.lojas){
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
