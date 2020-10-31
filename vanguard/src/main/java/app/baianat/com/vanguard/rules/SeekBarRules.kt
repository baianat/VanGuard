package app.baianat.com.vanguard.rules


class IsProgressEqual(var referenceProgress: Int) : WidgetValidationRule<Int>() {
    override fun validate(valueToCompare: Int): Boolean {
        return valueToCompare == referenceProgress
    }
}


class IsProgressNotEqual(var referenceProgress: Int) : WidgetValidationRule<Int>() {
    override fun validate(valueToCompare: Int): Boolean {
        return valueToCompare != referenceProgress
    }
}


class IsProgressAboveOrEqual(var referenceProgress: Int) : WidgetValidationRule<Int>() {
    override fun validate(valueToCompare: Int): Boolean {
        return valueToCompare > referenceProgress || valueToCompare == referenceProgress
    }
}


class IsProgressGreaterThan(var referenceProgress: Int) : WidgetValidationRule<Int>() {
    override fun validate(valueToCompare: Int): Boolean {
        return valueToCompare > referenceProgress
    }
}


class IsProgressBelowOrEqual(var referenceProgress: Int) : WidgetValidationRule<Int>() {
    override fun validate(valueToCompare: Int): Boolean {
        return valueToCompare < referenceProgress || valueToCompare == referenceProgress
    }
}


class IsProgressLessThan(var referenceProgress: Int) : WidgetValidationRule<Int>() {
    override fun validate(valueToCompare: Int): Boolean {
        return valueToCompare < referenceProgress
    }
}



