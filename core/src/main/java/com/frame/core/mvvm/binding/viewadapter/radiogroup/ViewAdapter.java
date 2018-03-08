package com.frame.core.mvvm.binding.viewadapter.radiogroup;

import android.databinding.BindingAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.frame.core.mvvm.binding.command.BindingCommand;


/**
 * Created by goldze on 2017/6/18.
 */
public class ViewAdapter {
    @BindingAdapter(value = {"onCheckedChangedCommand"}, requireAll = false)
    public static void onCheckedChangedCommand(final RadioGroup radioGroup, final BindingCommand<String> bindingCommand) {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
            bindingCommand.execute(radioButton.getText().toString());
        });
    }
}
