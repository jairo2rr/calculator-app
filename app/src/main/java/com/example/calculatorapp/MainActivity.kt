package com.example.calculatorapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.calculatorapp.databinding.ActivityMainBinding
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var listNumbers:MutableList<Double> = mutableListOf()
    var listOperations:MutableList<String> = mutableListOf()
    var result:Double = 0.0
    var lastEqual = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("ExampleX","${23.5 % 1} result")
        /* Only for numbers. */
        binding.btn0.setOnClickListener {
            setNumber("0")
        }
        binding.btn1.setOnClickListener {
            setNumber("1")
        }
        binding.btn2.setOnClickListener {
            setNumber("2")
        }
        binding.btn3.setOnClickListener {
            setNumber("3")
        }
        binding.btn4.setOnClickListener {
            setNumber("4")
        }
        binding.btn5.setOnClickListener {
            setNumber("5")
        }
        binding.btn6.setOnClickListener {
            setNumber("6")
        }
        binding.btn7.setOnClickListener {
            setNumber("7")
        }
        binding.btn8.setOnClickListener {
            setNumber("8")
        }
        binding.btn9.setOnClickListener {
            setNumber("9")
        }
        binding.btnAt.setOnClickListener {
            setNumber(".")
        }
        /* Now for the actions. */
        binding.btnClear.setOnClickListener {
            setOperation("C")
        }
        binding.btnErase.setOnClickListener {
            setOperation("erase")
        }
        binding.btnAdd.setOnClickListener {
            if(listNumbers.isNotEmpty()){

            }
            setOperation("add")
            listOperations.add("add")
        }
        binding.btnMinus.setOnClickListener {
            setOperation("minus")
            listOperations.add("minus")
        }
        binding.btnMulti.setOnClickListener {
            setOperation("multi")
            listOperations.add("multi")
        }
        binding.btnDivisor.setOnClickListener {
            setOperation("divide")
            listOperations.add("divide")
        }
        binding.btnEquals.setOnClickListener {
            setOperation("equals")
        }
    }

    private fun setNumber(character: String) {

        if(lastEqual){
            refreshPreview()
            lastEqual = false
            binding.tvResults.text = character
        }else{
            val lastChain = binding.tvResults.text.toString()
            if(lastChain == "0"){
                binding.tvResults.text = character
            }else{
                binding.tvResults.text = lastChain + character
            }

        }
    }

    private fun setOperation(operation:String){
        if(lastEqual){
            refreshPreview()
        }
        val lastChain = binding.tvResults.text.toString()
        val lastPreview = binding.tvPreview.text.toString()
        var numberDouble = lastChain.toDouble()

        when(operation){
            "C" -> clearTotality()
            "erase" -> binding.tvResults.text = if(lastChain.length>1) lastChain.dropLast(1) else "0"
            "add" -> {
                if(lastEqual) lastEqual = false
                listNumbers.add(numberDouble)
                binding.tvPreview.text = if(lastPreview == "0") {
                    if(numberDouble%1!=0.0) "$numberDouble +" else "${numberDouble.toInt()} +"
                } else {
                    if(numberDouble%1!=0.0) "$lastPreview $numberDouble +" else "$lastPreview ${numberDouble.toInt()} +"
                }
                clearDisplay()
            }
            "minus" -> {
                if(lastEqual) lastEqual = false
                listNumbers.add(numberDouble)
                binding.tvPreview.text = if(lastPreview == "0") {
                    if(numberDouble%1!=0.0) "$numberDouble -" else "${numberDouble.toInt()} -"
                } else {
                    if(numberDouble%1!=0.0) "$lastPreview $numberDouble -" else "$lastPreview ${numberDouble.toInt()} -"
                }
                clearDisplay()
            }
            "multi" -> {
                if(lastEqual) lastEqual = false
                listNumbers.add(numberDouble)
                binding.tvPreview.text = if(lastPreview == "0") {
                    if(numberDouble%1!=0.0) "$numberDouble ×" else "${numberDouble.toInt()} ×"
                } else {
                    if(numberDouble%1!=0.0) "$lastPreview $numberDouble ×" else "$lastPreview ${numberDouble.toInt()} ×"
                }
                clearDisplay()
            }
            "divide" -> {
                if(lastEqual) lastEqual = false
                listNumbers.add(numberDouble)
                binding.tvPreview.text = if(lastPreview == "0") {
                    if(numberDouble%1!=0.0) "$numberDouble /" else "${numberDouble.toInt()} /"
                } else {
                    if(numberDouble%1!=0.0) "$lastPreview $numberDouble /" else "$lastPreview ${numberDouble.toInt()} /"
                }
                clearDisplay()
            }
            "equals" -> {
                if(!lastEqual ){
                    if(listOperations.size>0){
                        listNumbers.add(numberDouble)
                        binding.tvPreview.text = if(numberDouble!=0.0) {
                            lastEqual = true
                            if(numberDouble%1!=0.0) "$lastPreview $numberDouble" else "$lastPreview ${numberDouble.toInt()}"
                        }else {
                            lastPreview
                        }
                        binding.tvResults.text=if(getResult()) result.toString() else result.toInt().toString()
                    }else{
                        Toast.makeText(this,"Debe ingresar una operacion",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else->Log.d("Error","Eso")
        }
    }

    private fun getResult():Boolean {
        result = listNumbers[0]
        for(pos in 1 until listNumbers.size){
            if(pos-1 < listOperations.size){
                when(listOperations[pos-1]){
                    "add"-> result += listNumbers[pos]
                    "minus"-> result -= listNumbers[pos]
                    "multi"->result *= listNumbers[pos]
                    "divide"->result /= listNumbers[pos]
                    else-> result = Double.NaN
                }
            }
        }
        val actualSizeDecimal = getBigSizeDecimal()
        result = convertOwnDecimal(result,actualSizeDecimal)
        Log.d("array",listNumbers.toString())
        return result%1!=0.0
    }

    private fun clearDisplay() {
        binding.tvResults.text = "0"
    }

    private fun clearTotality(){
        binding.tvResults.text = "0"
        binding.tvPreview.text = "0"
        listNumbers.clear()
        listOperations.clear()
    }
    private fun refreshPreview(){
        binding.tvPreview.text = "0"
        listNumbers.clear()
        listOperations.clear()
    }
    private fun getBigSizeDecimal():Int{
        var highnumber:Int = 0
        for(number in listNumbers){
            val actualSizeDecimal = getSizeDecimal(number)
            if(actualSizeDecimal>highnumber) highnumber = actualSizeDecimal
        }
        return highnumber
    }

    private fun getSizeDecimal(number:Double):Int{
        val numberString = number.toString()
        return numberString.length+1 - numberString.indexOf(".")
    }
    private fun convertOwnDecimal(number: Double, sizeDecimal:Int):Double{
        /*Limitar numero de decimales*/
        val bd = BigDecimal(number)
        val newnumber = bd.setScale(sizeDecimal,RoundingMode.HALF_UP).toDouble()

        return  newnumber
    }
}


