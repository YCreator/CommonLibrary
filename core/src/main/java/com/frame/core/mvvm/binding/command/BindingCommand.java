package com.frame.core.mvvm.binding.command;


import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * ReplyCommand
 * 执行的命令回调, 用于ViewModel与xml之间的数据绑定
 */
public class BindingCommand<T> {
    private Action execute0;
    private Consumer<T> execute1;
    private Function<T, Boolean> canExecute0;

    public BindingCommand(Action execute) {
        this.execute0 = execute;
    }

    /**
     * @param execute 带泛型参数的命令绑定
     */
    public BindingCommand(Consumer<T> execute) {
        this.execute1 = execute;
    }

    /**
     * @param execute     触发命令
     * @param canExecute0 true则执行,反之不执行
     */
    public BindingCommand(Action execute, Function<T, Boolean> canExecute0) {
        this.execute0 = execute;
        this.canExecute0 = canExecute0;
    }

    /**
     * @param execute     带泛型参数触发命令
     * @param canExecute0 true则执行,反之不执行
     */
    public BindingCommand(Consumer<T> execute, Function<T, Boolean> canExecute0) {
        this.execute1 = execute;
        this.canExecute0 = canExecute0;
    }

    /**
     * 执行Action命令
     */
    public void execute() {
        if (execute0 != null && canExecute0(null)) {
            try {
                execute0.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 执行带泛型参数的命令
     *
     * @param parameter 泛型参数
     */
    public void execute(T parameter) {
        if (execute1 != null && canExecute0(parameter)) {
            try {
                execute1.accept(parameter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 是否需要执行
     *
     * @return true则执行, 反之不执行
     */
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


}
