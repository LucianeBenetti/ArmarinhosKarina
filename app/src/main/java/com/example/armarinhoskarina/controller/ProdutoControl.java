package com.example.armarinhoskarina.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.armarinhoskarina.R;
import com.example.armarinhoskarina.dao.bd.CategoriaDao;
import com.example.armarinhoskarina.dao.bd.ProdutoDao;
import com.example.armarinhoskarina.model.Categoria;
import com.example.armarinhoskarina.model.Produto;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoControl {

    private Activity activity;
    private Spinner spCategorias;
    private EditText editTextNomeProduto;
    private EditText editTextPrecoProduto;
    private ListView lvItensProduto;
    private Produto produto;
    private ProdutoDao produtoDao;
    private List<Categoria> listCategoria;
    private ArrayAdapter<Categoria> adapterCategoria;
    private List<Produto> listProdutos;
    private ArrayAdapter<Produto> adapterProduto;



    public ProdutoControl(Activity activity) {
        this.activity = activity;
        produtoDao = new ProdutoDao(activity);
        produto = new Produto();
        initComponents();

    }

    private void initComponents() {
        editTextNomeProduto = activity.findViewById(R.id.editTextNomeProduto);
        editTextPrecoProduto = activity.findViewById(R.id.editTextPrecoProduto);
        spCategorias = activity.findViewById(R.id.spCategorias);
        lvItensProduto = activity.findViewById(R.id.lvProdutos);
        configSpinner();
        configListView();
    }

    private void configSpinner() {

        listCategoria = new ArrayList<>();
        listCategoria.add(new Categoria(1, "Agulha"));
        listCategoria.add(new Categoria(2, "Linha"));
        listCategoria.add(new Categoria(3, "Lã"));
        listCategoria.add(new Categoria(4, "Botão"));

        adapterCategoria = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, listCategoria);
        spCategorias.setAdapter(adapterCategoria);
    }

    private void configListView() {


        try {
            listProdutos = produtoDao.getDao().queryForAll();
        } catch (SQLException e) {
            listProdutos = new ArrayList<>();
        }
        adapterProduto = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, listProdutos);
        lvItensProduto.setAdapter(adapterProduto);
        cliqueCurto();
        cliqueLongo();

    }
    private void cliqueLongo() {
        lvItensProduto.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                produto = adapterProduto.getItem(i);
                dialogExcluirProduto(produto);
                return true; //executa somente clique longo
            }
        });
    }


    private void cliqueCurto() {
        lvItensProduto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                produto = adapterProduto.getItem(i);

                dialogAdicionarProduto(produto);
            }
        });
    }

    private void dialogAdicionarProduto(Produto p) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(activity);
        alerta.setTitle("Mostrando dados");
        alerta.setMessage(produto.toString());
        alerta.setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                produto = null;
            }
        });
        alerta.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                carregarForm(produto);
            }
        });
        alerta.show();
    }

    private void carregarForm(Produto produto) {
        editTextNomeProduto.setText(produto.getNome());
        editTextPrecoProduto.setText(produto.getValor().toString());

    }

    private void dialogExcluirProduto(final Produto p) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(activity);
        alerta.setTitle("Excluir Item");
        alerta.setIcon(android.R.drawable.ic_menu_delete);
        alerta.setMessage(p.toString());
        alerta.setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                produto = null;
            }
        });
        alerta.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                try {
                    if (produtoDao.getDao().delete(produto) > 0) {
                        excluirProdutoLv(produto);

                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                produto = null;
            }
        });
        alerta.show();

    }

    private void excluirProdutoLv(Produto p) {
        adapterProduto.remove(p);

    }
    private Produto getDadosForm() {
        Produto p = new Produto();
        p.setNome(editTextNomeProduto.getText().toString());
        p.setValor(editTextPrecoProduto.getText().toString());
        p.setCategoria((Categoria) spCategorias.getSelectedItem());
        return p;
    }

    public void salvarProdutoAction() {
        if (produto == null) {
            produto = getDadosForm();
        } else {
            Produto p = getDadosForm();
            produto.setNome(p.getNome());
            produto.setValor(p.getValor());
        }
        Dao.CreateOrUpdateStatus res;
        try {
            res = produtoDao.getDao().createOrUpdate(produto);

            if (res.isCreated()) {
                addProdutoLv(produto);
            } else if (res.isUpdated()) {

                atualizarProduto(produto);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        produto = null;
        configSpinner();
    }

    private void addProdutoLv(Produto p) {
        adapterProduto.add(p);
    }

    private void atualizarProduto(Produto p) {
        produto.setNome(p.getNome());
        produto.setValor(p.getValor());
        adapterProduto.notifyDataSetChanged();
    }
}