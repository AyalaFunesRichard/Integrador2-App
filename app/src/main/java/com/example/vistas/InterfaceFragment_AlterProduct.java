package com.example.vistas;

import androidx.fragment.app.Fragment;

import com.example.vistas.DTOs.Producto;

public interface InterfaceFragment_AlterProduct {
    void openFragment(Fragment nextFragment, String fragmentFor, Producto producto);
}
