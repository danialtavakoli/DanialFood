package com.danialtavakoli.danialfood.utils

interface BasePresenter<T> {
    fun onAttach(view: T)
    fun onDetach()
}

interface BaseView {

}