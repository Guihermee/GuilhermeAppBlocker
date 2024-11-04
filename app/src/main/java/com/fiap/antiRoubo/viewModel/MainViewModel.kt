package com.fiap.antiRoubo.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fiap.antiRoubo.model.NavItem

class MainViewModel : ViewModel() {

    val _selectedItemList = MutableLiveData<Int>()
    val selectedItemList: LiveData<Int> = _selectedItemList

    /**
     * Função para mudar qual é o item selecionado do BottomBar.
     */
    fun setSelectedItemList(value: Int) {
        _selectedItemList.value = value
    }

}
