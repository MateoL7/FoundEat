package com.example.foundeat.ui.client.filtro;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foundeat.R;

public class FiltrosView extends RecyclerView.ViewHolder {

    private OnCategorySelected listener;

    private TextView categoryFiltros;

    public FiltrosView(@NonNull View itemView) {
        super(itemView);

        categoryFiltros= itemView.findViewById(R.id.categoryFiltros);
        categoryFiltros.setOnClickListener(this::sendData);
    }

    private void sendData(View view) {
        categoryFiltros.setTextColor(Color.rgb(134,78,255));
        sendCategorySelected();
    }

    public void setListener(OnCategorySelected listener) {
        this.listener = listener;
    }

    public TextView getCategoryFiltros() {
        return categoryFiltros;
    }

    public void setCategoryFiltros(TextView categoryFiltros) {
        this.categoryFiltros = categoryFiltros;
    }

    public void sendCategorySelected(){
        listener.onDataResult(categoryFiltros.getText().toString());
    }
    public interface  OnCategorySelected{
        public void onDataResult(String categoryFiltros);
    }
}
