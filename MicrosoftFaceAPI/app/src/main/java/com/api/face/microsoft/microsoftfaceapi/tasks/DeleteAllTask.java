package com.api.face.microsoft.microsoftfaceapi.tasks;

/**
 * Created by Alina_Zhdanava on 10/14/2016.
 */

public class DeleteAllTask implements Runnable {

    private DeleteAllTaskListener listener;

    public DeleteAllTask(DeleteAllTaskListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        boolean flag = false;// perfromLongRunningOperation();
        notifyListener(flag);
    }

    private void notifyListener(boolean flag) {
        if (listener != null) {
            listener.onFinish(flag);
        }
    }
}
