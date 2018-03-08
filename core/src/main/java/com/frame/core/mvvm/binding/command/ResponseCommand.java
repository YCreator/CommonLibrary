package com.frame.core.mvvm.binding.command;


import io.reactivex.functions.Function;

/**
 * About : kelin的ResponseCommand
 * 执行的命令事件转换
 */


public class ResponseCommand<T, R> {

    private Function<T, R> execute1;

    private Function<T, Boolean> canExecute0;

    /**
     * like {@link BindingCommand},but ResponseCommand can return result when command has executed!
     *
     * @param execute function to execute when event occur.
     *
     */

    public ResponseCommand(Function<T, R> execute) {
        this.execute1 = execute;
    }

    public ResponseCommand(Function<T, R> execute, Function<T, Boolean> canExecute0) {
        this.execute1 = execute;
        this.canExecute0 = canExecute0;
    }

    private boolean canExecute0(T parameter) {
        if (canExecute0 == null) {
            return true;
        }
        try {
            return canExecute0.apply(parameter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    public R execute(T parameter) {
        if (execute1 != null && canExecute0(parameter)) {
            try {
                return execute1.apply(parameter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
