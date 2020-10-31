package app.baianat.com.demoApp

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
   // @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

 //   @Test
    fun testForLoop() {
        var inte = 1
        var counter = 0

        for (i in 1..inte){
            counter ++
        }

        assertEquals(1, counter)
    }

    @Test
    fun sillyTest(){
        var i = 0
        val c = 4

        when{
            c != 4 && c!= 5 ->{
                i = 8
            }
        }

        assertEquals(0, i)


    }
}
