package br.com.deliciagelada.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by 16254868 on 27/11/2017.
 */

public class ProdutoAdapter extends ArrayAdapter<Produto> {
    public ProdutoAdapter(Context ctx, ArrayList<Produto> lstProduto){
        super(ctx, 0, lstProduto);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if(v == null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.list_view_item, null);
        }

        Produto item = getItem(position);
        TextView txtNome = v.findViewById(R.id.nomeProduto);
        TextView txtDesc = v.findViewById(R.id.descProduto);
        TextView txtValor = v.findViewById(R.id.valorProduto);
        ImageView img_foto = v.findViewById(R.id.imgProduto);

        NumberFormat formato = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        txtNome.setText(item.getNome());
        txtDesc.setText(item.getDescricao());
        txtValor.setText(formato.format(item.getPreco()));

        Picasso.with(getContext())
                .load("http://10.0.2.2/inf3m/turmaB/Projeto%20Delicia%20Gelada/CMS/"+ item.getCaminhoImagem())
                .into(img_foto);

        return v;
    }
}
