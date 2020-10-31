package app.baianat.com.vanguard.rules

/**
 * Rule to be used on a Spinner to check if selected position equals a reference position.
 */
class IsPositionEqual(private var referencePosition: Int) :
    WidgetValidationRule<Int>() {
    override fun validate(valueToCompare: Int): Boolean {

        return referencePosition == valueToCompare

    }
}

/**
 * Rule to be used on a Spinner to check if selected position doesn't equal a reference position.
 */
class IsPositionNotEqual(private var referencePosition: Int) :
    WidgetValidationRule<Int>() {
    override fun validate(valueToCompare: Int): Boolean {

        return referencePosition != valueToCompare

    }
}

/**
 * Rule to be used on a Spinner to match the selected position to a a set of positions.
 */
class IsPositionIncludedIn(var validPositionsSet: Array<Int>) :
    WidgetValidationRule<Int>() {
    override fun validate(valueToCompare: Int): Boolean {
        return validPositionsSet.contains(valueToCompare)
    }
}