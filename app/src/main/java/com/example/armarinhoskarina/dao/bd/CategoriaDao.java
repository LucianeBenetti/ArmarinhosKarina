package com.example.armarinhoskarina.dao.bd;


import android.content.Context;

import com.example.armarinhoskarina.dao.helpers.DaoHelper;
import com.example.armarinhoskarina.model.Categoria;


public class CategoriaDao extends DaoHelper<Categoria> {

    public CategoriaDao(Context c) {
        super(c, Categoria.class);
    }

}
