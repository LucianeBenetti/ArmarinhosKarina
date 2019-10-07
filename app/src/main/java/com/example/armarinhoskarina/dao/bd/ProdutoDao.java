package com.example.armarinhoskarina.dao.bd;

import android.content.Context;

import com.example.armarinhoskarina.dao.helpers.DaoHelper;
import com.example.armarinhoskarina.model.Produto;

public class ProdutoDao extends DaoHelper<Produto> {

    public ProdutoDao(Context c) {
        super(c, Produto.class);
    }
}
