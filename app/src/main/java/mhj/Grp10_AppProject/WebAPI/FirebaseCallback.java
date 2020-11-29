package mhj.Grp10_AppProject.WebAPI;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import mhj.Grp10_AppProject.Model.PrivateMessage;

public interface FirebaseCallback {
 void OnFirebaseCallback(MutableLiveData<List<PrivateMessage>> PrivateMessages);
}
