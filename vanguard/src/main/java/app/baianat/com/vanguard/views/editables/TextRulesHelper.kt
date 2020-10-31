package app.baianat.com.vanguard.views.editables

import app.baianat.com.vanguard.rules.TextValidationRule
import java.lang.StringBuilder

class TextRulesHelper(var textRules: MutableList<TextValidationRule>) {


   private fun unValidRules(textToValidate: String): MutableList<TextValidationRule> {

        (textRules).let {rules->

            val unValidRules= mutableListOf<TextValidationRule>()

            rules.forEach{rule->
                if (!rule.validate(textToValidate)){
                    unValidRules.add(rule)
                }
            }

            return unValidRules
        }

    }

    fun errorMsg(textToValidate: String):String {

        val errorMsgBuilder = StringBuilder()

        unValidRules(textToValidate).forEach {ruleToValidate->

            ruleToValidate.getErrorMessage()
                ?.let { errorMsg ->
                    if (!errorMsgBuilder.contains(errorMsg)) {
                        if (errorMsgBuilder.isEmpty()) {
                            errorMsgBuilder.append(errorMsg)
                        } else {
                            errorMsgBuilder.append("\n")
                            errorMsgBuilder.append(errorMsg)
                        }
                    }
                }
        }

        return errorMsgBuilder.toString()
    }

    fun helperMsg(textToValidate: String):String {
        val helperMsgBuilder = StringBuilder()

        unValidRules(textToValidate).forEach {rule->

            rule.getHelperMessage()
                ?.let { helperMsg ->
                    if (!helperMsgBuilder.contains(helperMsg)) {
                        if (helperMsgBuilder.isEmpty()) {
                            helperMsgBuilder.append(helperMsg)
                        } else {
                            helperMsgBuilder.append("\n")
                            helperMsgBuilder.append(helperMsg)
                        }
                    }
                }

        }
        return helperMsgBuilder.toString()
    }


}