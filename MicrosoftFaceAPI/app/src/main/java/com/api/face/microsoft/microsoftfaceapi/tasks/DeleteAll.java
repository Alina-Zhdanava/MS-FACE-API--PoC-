package com.api.face.microsoft.microsoftfaceapi.tasks;

import com.microsoft.projectoxford.face.contract.PersonGroup;
import com.microsoft.projectoxford.face.rest.ClientException;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Created by Alina_Zhdanava on 10/13/2016.
 */

public class DeleteAll implements Callable<Boolean> {

    public DeleteAll() {
    }

    @Override
    public Boolean call() throws Exception {
        PersonGroup[] groups = new PersonGroup[0];
        try {
            groups = ImageHelper.faceServiceClient.getPersonGroups();
            for (PersonGroup group : groups) {
                ImageHelper.faceServiceClient.deletePersonGroup(group.personGroupId);
                return true;
            }
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
