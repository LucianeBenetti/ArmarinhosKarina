package com.example.armarinhoskarina.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.armarinhoskarina.R;
import com.example.armarinhoskarina.controller.ProdutoControl;

public class MainActivity extends AppCompatActivity {
private ProdutoControl control;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        control = new ProdutoControl(this);
    }


    public void salvarproduto(View view) {
        control.salvarProdutoAction();
    }
}
