package com.akinci.chatter.common.extension

import android.text.TextUtils
import com.akinci.chatter.R
import com.google.android.material.textfield.TextInputEditText

/**************************** TextInputEditText validation extensions ****************************/
fun TextInputEditText.validateNotEmpty() : Boolean {
    error = if(text.isNullOrEmpty()){
        resources.getString(R.string.validation_text_input_edit_text_not_empty)
    }else{ null }

    return TextUtils.isEmpty(error) && !TextUtils.isEmpty(text)
}

fun TextInputEditText.validateMinCharacter(characterLimit : Int) : Boolean {
    // Firstly dependent validations checked..
    if(validateNotEmpty()){
        error = if(text!!.length < characterLimit){ // previously checked text is not empty or null
            resources.getString(
                    R.string.validation_text_input_edit_text_min_char_limit,
                    characterLimit)
        }else{ null }
    }

    return TextUtils.isEmpty(error) && !TextUtils.isEmpty(text)
}
/************************************************************************************************/