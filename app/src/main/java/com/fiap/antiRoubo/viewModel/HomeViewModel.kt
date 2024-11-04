package com.fiap.antiRoubo.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fiap.antiRoubo.model.MyService

class HomeViewModel : ViewModel() {

    val _selectedService = MutableLiveData<Int?>()
    val selectedService: MutableLiveData<Int?> = _selectedService

    val _myServices = MutableLiveData<List<MyService>>()
    val myServices: MutableLiveData<List<MyService>> = _myServices

    /**
     * Função para mudar o botão que está selecionado no home screen da lista de serviços.
     */
    fun setSelectedService(serviceId: Int?) {
        _selectedService.value = serviceId
    }

    /**
     * Função para mudar a lista de serviços.
     */
    fun setMyServices(services: List<MyService>) {
        _myServices.value = services
    }

}
