package br.com.deliciagelada.app;

import java.io.Serializable;

/**
 * Created by 16254868 on 27/11/2017.
 */

public class Produto implements Serializable {
    private int idProduto;
    private String caminhoImagem;
    private String nome;
    private String descricao;
    private Double preco;
    private String infoAdd;
    private Float media;

    public static Produto create(int idProduto, String nome, String descricao, Double preco, String caminhoImagem, String infoAdd){
        Produto p = new Produto();

        p.setIdProduto(idProduto);
        p.setNome(nome);
        p.setDescricao(descricao);
        p.setPreco(preco);
        p.setCaminhoImagem(caminhoImagem);
        p.setInfoAdd(infoAdd);

        return p;
    }

    public Float getMedia() {
        return media;
    }

    public void setMedia(Float media) {
        this.media = media;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public String getInfoAdd() {
        return infoAdd;
    }

    public void setInfoAdd(String infoAdd) {
        this.infoAdd = infoAdd;
    }

    public String getCaminhoImagem() {
        return caminhoImagem;
    }

    public void setCaminhoImagem(String caminhoImagem) {
        this.caminhoImagem = caminhoImagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }
}
