package observable;

import models.Notification;

public interface Observer {
    void update(Notification notification);
}
