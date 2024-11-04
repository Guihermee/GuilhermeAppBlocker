package com.fiap.antiRoubo.repository

import android.content.Context
import android.content.SharedPreferences
import com.fiap.antiRoubo.MainActivity
import com.fiap.antiRoubo.R

class SharedPrefs(context: Context) {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    val editor: SharedPreferences.Editor = sharedPrefs.edit()
    val blockStatePhone = "BLOCK_STATE_PHONE"

    /**
     * Função para salvar o estado do bloqueio do celular do usuário
     * @param state Boolean - Estado do bloqueio do celular do usuário
     */
    fun saveBlockPhoneState(state: Boolean) {
        editor.putBoolean(blockStatePhone, state)
        editor.apply()
    }

    /**
     * Função para pegar o estado do bloqueio do celular do usuário
     * @return Boolean - Estado do bloqueio do celular do usuário
     */
    fun getBlockPhoneState(): Boolean {
        return sharedPrefs.getBoolean(blockStatePhone, false)
    }

}