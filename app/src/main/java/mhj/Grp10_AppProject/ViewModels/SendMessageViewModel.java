package mhj.Grp10_AppProject.ViewModels;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import mhj.Grp10_AppProject.Model.Repository;

public class SendMessageViewModel extends ViewModel {
    private final Repository repository;

    public SendMessageViewModel(Context context) {
        repository = Repository.getInstance(context);
    }

    private void sendMessage(String message) {
        //repository.sendMessage(message)
    }
}
