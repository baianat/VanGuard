# VanGuard
An easy-to-use, real-time UI form validation library for Android.

<p align="center">
  <img src="https://github.com/O-labib/VanGuard/blob/master/app/src/main/res/drawable/gif_sample.gif">
</p>
 
## Installation
 TO:DO
 
 
## Quick Guide
**Define** a set of  validation cases, each of which must include:
  * A validaion rule based on which the validation status of the view can be determined as *true or false*.
  * The view on which the validaion rule is tested.

  ``` kotlin
        val emailValidationCase = CasesFactory.ForEditable()
            .watch(emailEditText)
            .forRule(IsEmail(errorMsg = "Please enter a valid e-mail!"))
            .create()
            
        val passwordValidationCase = CasesFactory.ForEditable()
            .watch(passwordEditText)
            .forRule(IsLengthGreaterThan(length = 5,errorMsg = "Password can't be less than 6 chars!"))
            .create()
            
        val notificationValidationCase = CasesFactory.ForCheckable()
            .watch(notificationsCheckBox)
            .forRule(IsChecked())
            .create()
 ```     
**Initialize** VanGuard in the *Activity or Frament of interest*, and link it to the validation cases.

  ``` kotlin
        VanGuard.of(this)
            ?.validate(emailValidationCase, passwordValidationCase, notificationValidationCase)
 ``` 

**Listen** to the Callback for form status changes between being true and false *(optional)*.
  ``` kotlin
        VanGuard.of(this)
            ....
            .doOnFormStatusChange {isFormValid->
                // isFormValid : true or false, based on the state of the form!
            }
 ```

 ## Detailed Guide
 For a detailed guide on how to use VanGuard, refer to the [**Wiki**](https://github.com/O-labib/VanGuard/wiki). 
 
