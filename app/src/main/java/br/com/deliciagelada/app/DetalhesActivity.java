package br.com.deliciagelada.app;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class DetalhesActivity extends AppCompatActivity {

    ImageView imgDetail;
    TextView nomeProdutoDetail, descProdutoDetail, infoAddDetail, valorProdutoDetail, tltDetalhes;
    RatingBar avaliacao;
    String numAval, idProduto;
    Context ctx;
    TextView ligar;
    Button btnAvaliacao;

    private static final String AVALIADO = "avaliado";
    private static final String PRODUTO = "produto";

    Produto item;
    String telefone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        imgDetail = (ImageView) findViewById(R.id.imgProdutoDetail);
        tltDetalhes = (TextView) findViewById(R.id.tltDetalhes);
        nomeProdutoDetail = (TextView) findViewById(R.id.nomeProdutoDetail);
        descProdutoDetail = (TextView) findViewById(R.id.descProdutoDetail);
        infoAddDetail = (TextView) findViewById(R.id.infoAddDetail);
        valorProdutoDetail = (TextView) findViewById(R.id.valorProdutoDetail);
        avaliacao = (RatingBar) findViewById(R.id.avaliacao);
        ligar = (TextView) findViewById(R.id.ligarLoja);
        btnAvaliacao = (Button)findViewById(R.id.btnAvaliacao);

        Intent intent = getIntent();

        item = (Produto) intent.getSerializableExtra("item");

        idProduto = item.getIdProduto()+"";

        retornaAv(Integer.parseInt(idProduto));

        SharedPreferences preferencias = getSharedPreferences(item.getNome(), MODE_PRIVATE);
        boolean avaliado = preferencias.getBoolean(AVALIADO, false);
        int idProd = preferencias.getInt(PRODUTO, 0);

        if (avaliado == true && idProd == Integer.parseInt(idProduto)){
                btnAvaliacao.setVisibility(View.GONE);
                avaliacao.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        return true;
                    }
            });
        }



        ctx = this;

        avaliacao.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                numAval = v+"";
                Toast.makeText(getApplicationContext(), v+"", Toast.LENGTH_SHORT).show();
            }
        });



        Toast.makeText(DetalhesActivity.this, item.getMedia()+"", Toast.LENGTH_SHORT).show();

        Picasso.with(getApplicationContext())
                .load("http://10.0.2.2/inf3m/turmaB/Projeto%20Delicia%20Gelada/CMS/"+ item.getCaminhoImagem())
                .into(imgDetail);

        NumberFormat formato = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        tltDetalhes.setText("Detalhes do Suco " + item.getNome());
        nomeProdutoDetail.setText(item.getNome());
        descProdutoDetail.setText(item.getDescricao());

        if(item.getInfoAdd().equals("null")){
            infoAddDetail.setText("Este suco não tem informações adicionais");
        }else{
            infoAddDetail.setText(item.getInfoAdd());
        }
        valorProdutoDetail.setText(formato.format(item.getPreco()));

        new AsyncTask<Void, Void, Void>(){

            ArrayList<String> lstTelefone = new ArrayList<String>();

            @Override
            protected Void doInBackground(Void... voids) {

                String retornoJson = Http.get("http://10.0.2.2/inf3m/turmaB/Projeto%20Delicia%20Gelada/localSelect.php");

                Log.d("TAG", retornoJson);

                try {
                    JSONArray jsonArray = new JSONArray(retornoJson);

                    for(int i =0; i < jsonArray.length(); i++){

                        JSONObject item = jsonArray.getJSONObject(i);

                        telefone = item.getString("telefone");

                        lstTelefone.add(telefone);
                    }

                    Log.d("TAG", lstTelefone.size()+"");
                }catch (Exception ex){
                    Log.e("Erro: ", ex.getMessage());
                }

                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute();

        ligar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{

                    Uri uri = Uri.parse("tel:" + telefone);
                    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                    startActivity(intent);

                }catch (ActivityNotFoundException act){
                    Log.e("Exemplo de chamada", "falha", act);
                }

            }
        });

    }


    public void avaliarProduto(View view) {

        SharedPreferences preferencias = getPreferences(MODE_PRIVATE);

        SharedPreferences.Editor editor = preferencias.edit();


        final String url = "http://10.0.2.2/inf3m/turmaB/Projeto%20Delicia%20Gelada/inserir.php";

        final HashMap<String, String> valores = new HashMap<>();
        valores.put("idProduto", idProduto);
        valores.put("avaliacao", numAval);

        new AsyncTask<Void, Void, Void>(){

            boolean sucesso = false;
            String msgm = null;

            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
               /* btnInserir.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);*/
            }

            @Override
            protected Void doInBackground(Void... voids) {

                String resultado = Http.post(url, valores);

                try{
                    JSONObject jsonObject = new JSONObject(resultado);
                    sucesso = jsonObject.getBoolean("sucesso");
                    msgm = jsonObject.getString("mensagem");
                }catch (Exception ex){
                    ex.printStackTrace();
                    sucesso = false;
                    msgm = "Erro ao inserir";
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                /*btnInserir.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);*/

                Toast.makeText(ctx, msgm, Toast.LENGTH_SHORT).show();
                if (sucesso){
                    //startActivity(new Intent(ctx, DetalhesActivity.class));
                }

            }
        }.execute();

        preferencias = getSharedPreferences(nomeProdutoDetail.getText().toString(),MODE_PRIVATE);

        editor = preferencias.edit();
        editor.putBoolean(AVALIADO, true);
        editor.putInt(PRODUTO, Integer.parseInt(idProduto));
        editor.commit();

        finish();
    }

    private void retornaAv(int idProduto){

        final String url = "http://10.0.2.2/inf3m/turmaB/Projeto%20Delicia%20Gelada/slcAval.php?idProduto="+idProduto;

        new AsyncTask<Void, Void, Void>(){
            //Roda em 2nd plano
            @Override
            protected Void doInBackground(Void... voids) {
                String resultado = Http.get(url);
                Log.d("TAG", resultado);
                try{
                    //Transforma Json em objeto
                    JSONObject item1 = new JSONObject(resultado);

                    item.setMedia((float) item1.getDouble("media"));


                }catch (Exception e){
                    Log.e("ERROU:", e.getMessage());
                }
                return null;
            }
        }.execute();
    }
}
