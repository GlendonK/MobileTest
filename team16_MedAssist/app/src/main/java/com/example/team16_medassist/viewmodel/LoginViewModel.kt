package com.example.team16_medassist.viewmodel

import android.view.View
import androidx.lifecycle.*
import com.example.team16_medassist.activity.AuthListener
import com.example.team16_medassist.model.CaseModel
import com.example.team16_medassist.repository.CaseRepository
import com.example.team16_medassist.repository.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel(private val userRepository: UserRepository,
                     private val caseRepository: CaseRepository) : ViewModel() {


    // email and password for the input
    var email: String? = null
    var password: String? = null
    //auth listener
    var authListener: AuthListener? = null

    // lazy initializations
    val user by lazy {
        userRepository.currentUser()
    }

    fun getUsers() = userRepository.getUser()

    //function to perform login
    fun login(view : View) {
        //validating email and password
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authListener?.onFailure("Invalid email or password")
            return
        }

        //authentication started
        authListener?.onStarted()

        //calling login from repository to perform the actual authentication
        val disposable = userRepository.login(email!!, password!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    //sending a success callback
                    authListener?.onSuccess()
                    userRepository.queryUser(userRepository.currentUser().uid)
                    caseRepository.queryCase(userRepository.currentUser().uid)
                }, {
                    //sending a failure callback
                    authListener?.onFailure(it.message!!)
                })
        disposables.add(disposable)
    }
    //disposable to dispose the Completable
    private val disposables = CompositeDisposable()

    //disposing the disposables
    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }


    fun getCases() = caseRepository.getCase()

    fun getCaseReportCount() = caseRepository.getCaseReportCount()

    fun updateCase(case: CaseModel) = caseRepository.updateCase(case)

    private val mCaseData =caseRepository.mCaseData

    fun getCaseLiveData() : LiveData<ArrayList<CaseModel>>{
        return mCaseData
    }

}
@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val userRepository: UserRepository, private val caseRepository: CaseRepository)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(userRepository, caseRepository) as T
    }
}
