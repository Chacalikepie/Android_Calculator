package com.example.simplecalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.view.*
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.ArithmeticException

class MainActivity : AppCompatActivity() {

    private lateinit var textViewScreen: TextView

    // Last key pressed is number?
    private var lastNumeric: Boolean = false
    // Error state
    private var stateError: Boolean = false
    // Dot button pressed?
    private var lastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textViewScreen = findViewById(R.id.textView_screen)
    }

    //Show numbers on click
    fun onDigit(view: View) {
        if (stateError) { //If error, display error message
            textViewScreen.text = (view as Button).text
            stateError = false
        }
        else { //If no error, append expression
            textViewScreen.append((view as Button).text)
        }

        //Set flag as true
        lastNumeric = true
    }

    //Show decimal on click
    fun onDecimal(view: View) {
        if (lastNumeric && !stateError && !lastDot) {
            textViewScreen.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    //Show operator on click
    fun onOperator(view: View) {
        if (lastNumeric && !stateError) {
            textViewScreen.append((view as Button).text)
            lastNumeric = false
            lastDot = false
        }
    }

    //Clear view on click
    fun onClear(view: View) {
        textViewScreen.text = ""
        lastDot = false
        lastNumeric = false
        stateError = false
    }

    //Calculate and display on click
    fun onEqual(view: View) {
        if (lastNumeric && !stateError) {
            //Read expression
            val txt = textViewScreen.text.toString()
            //Create an expression
            val expression = ExpressionBuilder(txt).build()
            try {
                //Calculate result and display
                val result = expression.evaluate()
                textViewScreen.text = result.toString()
                lastDot = true //Result contains a dot
            }
            catch (ex: ArithmeticException) {
                //Display Error
                textViewScreen.text = "Error"
                stateError = true
                lastNumeric = false
            }
        }
    }

    fun onDel(view: View) {
        val tempText = textViewScreen.text
        if (!stateError && tempText.isNotEmpty()){
            textViewScreen.text = tempText.dropLast(1)
            lastNumeric = true
            lastDot = false
        }
    }
}
