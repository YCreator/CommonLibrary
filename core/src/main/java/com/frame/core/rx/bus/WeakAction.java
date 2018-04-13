package com.frame.core.rx.bus;

import java.lang.ref.WeakReference;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


/**
 *
 */
public class WeakAction<T> {
    private Action action;
    private Consumer<T> action1;
    private boolean isLive;
    private Object target;
    private WeakReference reference;

    public WeakAction(Object target, Action action) {
        reference = new WeakReference<>(target);
        this.action = action;

    }

    public WeakAction(Object target, Consumer<T> action1) {
        reference = new WeakReference<>(target);
        this.action1 = action1;
    }

    public void execute() {
        if (action != null && isLive()) {
            try {
                action.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void execute(T parameter) {
        if (action1 != null
                && isLive()) {
            try {
                action1.accept(parameter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void markForDeletion() {
        reference.clear();
        reference = null;
        action = null;
        action1 = null;
    }

    public Action getAction() {
        return action;
    }

    public Consumer getAction1() {
        return action1;
    }

    public boolean isLive() {
        if (reference == null) {
            return false;
        }
        if (reference.get() == null) {
            return false;
        }
        return true;
    }


    public Object getTarget() {
        if (reference != null) {
            return reference.get();
        }
        return null;
    }
}
