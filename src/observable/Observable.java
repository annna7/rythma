package observable;

import models.Notification;

import java.util.List;
import java.util.ArrayList;

public interface Observable {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers(Notification notification);
}
