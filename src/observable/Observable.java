package observable;

import java.util.List;
import java.util.ArrayList;

// We will use composition instead of inheritance, because the implementation of the Observer is identical across all classes
public class Observable {
    private final List<Observer> observers = new ArrayList<>();
    public void attach(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}
