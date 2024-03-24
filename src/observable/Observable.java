package observable;

import models.Notification;

public interface Observable {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers(Notification notification);
}
