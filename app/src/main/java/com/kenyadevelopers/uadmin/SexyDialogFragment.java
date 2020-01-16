package com.kenyadevelopers.uadmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class SexyDialogFragment extends DialogFragment
{
    private EditText mEditTextMediaTitle;
    public static final String EXTRA_EDIT_ENTRY="extra_edit_entry";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        mEditTextMediaTitle=new EditText(getContext());
        mEditTextMediaTitle.setInputType(InputType.TYPE_CLASS_TEXT);
        mEditTextMediaTitle.setHint("ENTER MEDIA NAME");
        AlertDialog alertDialog=new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.media_title))
                .setView(mEditTextMediaTitle)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                      getTargetFragment().onActivityResult(getTargetRequestCode(),Activity.RESULT_OK,new Intent()
                              .putExtra(EXTRA_EDIT_ENTRY,mEditTextMediaTitle.getText().toString().trim()));
                    }
                }).create();
        return alertDialog;
    }
}
