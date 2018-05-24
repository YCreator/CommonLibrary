package com.frame.core.rx.bus;

import com.frame.core.mvvm.binding.command.BindingAction;
import com.frame.core.mvvm.binding.command.BindingConsumer;

import java.lang.ref.WeakReference;


/**
 *
 */
public class WeakAction<T> {
    private BindingAction action;
    private BindingConsumer<T> consumer;
    private boolean isLive;
    private Object target;
    private WeakReference reference;

    public WeakAction(Object target, BindingAction action) {
        reference = new WeakReference<>(target);
        this.action = action;
    }

    public WeakAction(Object target, BindingConsumer<T> consumer) {
        reference = new WeakReference<>(target);
        this.consumer = consumer;
    }

    public void execute() {
        if (action != null && isLive()) {
            action.call();
        }
    }

    public void execute(T parameter) {
        if (consumer != null && isLive()) {
            consumer.call(parameter);
        }
    }

    public void markForDeletion() {
        reference.clear();
        reference = null;
        action = null;
        consumer = null;
    }

    public BindingAction getBindingAction() {
        return action;
    }

    public BindingConsumer getBindingConsumer() {
        return consumer;
    }

    public boolean isLive() {
        return reference != null && reference.get() != null;
    }

    public Object getTarget() {
        return reference != null ? reference.get() : null;
    }
}
