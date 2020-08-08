package com.hotapp229.messenger

import android.content.Context
import android.widget.RadioGroup
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.lang.Exception


class SuperMagic {

    private val ALPHABET = arrayListOf<String>("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")
    private val FIRSTROTOR = arrayListOf<String>("A1", "J4", "M5", "O7", "L11", "P6", "U9", "E3", "Q13", "V10", "F12", "S8", "W2", "D12", "X2", "K4", "Y7", "G6", "N3", "I13", "C5", "T8", "H1", "R10", "Z11", "B9")
    private val SECONDROTOR =arrayListOf<String>("D5", "M9", "O6", "B12", "I7", "L13", "H3", "R1", "A11", "J4", "V8", "N10", "F2", "U1", "Z13", "C8", "E12", "Y5", "G9", "S2", "K6", "W11", "Q4", "X7", "T10", "P3")
    private val THIRDROTOR = arrayListOf<String>("B13", "I8", "C5", "D9", "H12", "Z4", "J2", "A7", "P3", "N10", "U1", "F6", "T11", "M1", "W3", "E2", "Q5", "Y8", "L12", "X4", "V9", "O11", "R6", "S13", "K10", "G7")
    private val REFLECTOR = arrayListOf<String>("A1", "J1", "F1", "B1", "H1", "D1", "C1", "E1", "L1", "G1", "M1", "K1", "I1", "F2", "C2", "L2", "K2", "D2", "I2", "M2", "A2", "G2", "J2", "B2", "E2", "H2")
    private val PLUGBOARD1 = arrayListOf<String>("KZ","BF","MX","IA","NC","HQ","DY","TW","JE","PS","LR","GV","OU")
    private val PLUGBOARD2 = arrayListOf<String>("ZA","OY","EX","IM","RL","FN","CG","US","WD","BQ","KT","HV","JP")
    private val PLUGBOARD3 = arrayListOf<String>("PZ","AY","RI","JL","DB","OS","HV","GX","CQ","MF","UE","KT","NW")
    private val PLUGBOARD4 = arrayListOf<String>("YC","EA","RB","JS","IX","OW","DK","ZV","HU","PT","FQ","GN","LM")

    private val maxCount = 26
    private val newline = "\n".toByteArray()
    private val regexAlphabet = "^[a-zA-Z]*$".toRegex()
    private val ROTOR_SETTING = "rotor_setting.txt"
    var rotor1Setup: String? = null
    var rotor2Setup: String? = null
    var rotor3Setup: String? = null
    var rotor1Pos: String? = null
    var rotor2Pos: String? = null
    var rotor3Pos: String? = null
    var plugboard: String? = null

    fun startEncrypting(input : String, context: Context): String{

        val firstRotor = ArrayList<String>(FIRSTROTOR.map{it})
        val secondRotor = ArrayList<String>(SECONDROTOR.map {it})
        val thirdRotor = ArrayList<String>(THIRDROTOR.map {it})

        readRotorSetup(context)
        rearrangeArray(firstRotor, rotor1Pos!!.toInt())
        rearrangeArray(secondRotor, rotor2Pos!!.toInt())
        rearrangeArray(thirdRotor, rotor3Pos!!.toInt())

        val swappedInput = builtinSwapChar(input,plugboard!!)
        var rotor1Counter = 0
        var rotor2Counter = 0
        var result = ""

        for(swappedIndex in swappedInput) {
            var count = 0
            if (!swappedIndex.toString().matches(regexAlphabet)) {
                result += swappedIndex
            } else {
                ALPHABET.forEach { alphabetIndex ->
                    count++
                    if (alphabetIndex.equals(swappedIndex.toString(), ignoreCase = true)) {
                        val rotorIndex = validateIndex(count)
                        val rotor1Char1 = firstRotor[rotorIndex]
                        var getRotor1Index = 0
                        for (i in firstRotor) {
                            getRotor1Index++
                            val temp = i.substring(1, i.length)
                            if (rotor1Char1.substring(1, rotor1Char1.length) == temp) {
                                if (rotor1Char1 != i) {
                                    break
                                }
                            }
                        }
                        val getRotor2Index = getIndex(secondRotor, getRotor1Index)
                        val getRotor3Index = getIndex(thirdRotor, getRotor2Index)
                        val reflector = REFLECTOR[validateIndex(getRotor3Index)]
                        var getReflectedIndex = 0
                        for (i in REFLECTOR) {
                            getReflectedIndex++
                            val temp = i.substring(0, 1)
                            if (reflector.substring(0, 1) == temp) {
                                if (reflector != i) {
                                    break
                                }
                            }
                        }

                        val getReflectedRotor3Index = getIndex(thirdRotor, getReflectedIndex)
                        val getReflectedRotor2Index = getIndex(secondRotor, getReflectedRotor3Index)
                        val getReflectedRotor1Index = getIndex(firstRotor, getReflectedRotor2Index)
                        var tempChar = ALPHABET[validateIndex(getReflectedRotor1Index)]

                        result += if (!swappedIndex.isLowerCase()) {
                            builtinSwapChar(tempChar, plugboard!!)

                        } else {
                            //tempChar = swapChar(tempChar.toLowerCase())
                            builtinSwapChar(tempChar.toLowerCase(), plugboard!!)
                        }
                        rotor1Counter++
                        rearrangeArray(firstRotor, 1)
                        if (rotor1Counter == maxCount) {
                            rearrangeArray(secondRotor, 1)
                            rotor2Counter++
                            rotor1Counter = 0
                            if (rotor2Counter == maxCount) {
                                rearrangeArray(thirdRotor, 1)
                                rotor2Counter = 0
                            }
                        }
                    }
                }
            }
        }
        //println(result)
        return swappedInput
    }

    private fun builtinSwapChar(txt : String, plugboard: String) : String{
        var msg : String = ""
        var checkedId = plugboard
        var plugboardSelected = arrayListOf<String>()
        when(checkedId){
            "1" -> plugboardSelected = PLUGBOARD1
            "2" -> plugboardSelected = PLUGBOARD2
            "3" -> plugboardSelected = PLUGBOARD3
            "4" -> plugboardSelected = PLUGBOARD4
        }
        txt.forEach {
            var charInput = it.toString()
            if(charInput.matches(regexAlphabet)) {
                for(item in plugboardSelected){
                    if(item.contains(charInput, ignoreCase = true)){
                        val char1 = item[0] .toString()
                        val char2 = item[1].toString()
                        if(charInput.contains(char1, ignoreCase = true)){
                            charInput = char2
                            break
                        }
                        else{
                            charInput = char1
                            break
                        }
                    }
                }
                if(it.isLowerCase()){
                    charInput = charInput.toLowerCase()
                }
                msg += charInput
            }
            else{
                msg += charInput
            }
        }
        return msg
    }

    fun readRotorSetup(context: Context){
        var fis: FileInputStream? = null
        fis = context.openFileInput(ROTOR_SETTING)
        var isr: InputStreamReader = InputStreamReader(fis)
        val br: BufferedReader = BufferedReader(isr)
        var text: String? = null
        var savedData = arrayListOf<String>()
        while ({ text = br.readLine(); text }() != null) {
            text?.let { savedData.add(it) }

        }
        rotor1Setup = savedData[0]
        rotor2Setup = savedData[1]
        rotor3Setup = savedData[2]
        rotor1Pos = savedData[3]
        rotor2Pos = savedData[4]
        rotor3Pos = savedData[5]
        plugboard = savedData[6]
    }

    fun saveRotorSetting(context: Context, rotor1Pos: String, rotor2Pos: String, rotor3Pos: String, rotor1Setup: String, rotor2Setup: String, rotor3Setup: String, plugboard: String){
        val file: String = ROTOR_SETTING
        var fos: FileOutputStream
        try{
            fos = context.openFileOutput(file, Context.MODE_PRIVATE)
            fos.write(rotor1Pos.toByteArray() + newline)
            fos.write(rotor2Pos.toByteArray() + newline)
            fos.write(rotor3Pos.toByteArray() + newline)
            fos.write(rotor1Setup.toByteArray() + newline)
            fos.write(rotor2Setup.toByteArray() + newline)
            fos.write(rotor3Setup.toByteArray() + newline)
            fos.write(plugboard.toByteArray() + newline)
        }catch(e : Exception){
            e.printStackTrace()
        }
    }

    private fun rearrangeArray(myArr: ArrayList<String>, noOfRearrange: Int) {
        repeat(noOfRearrange) {
            var temp = myArr[0]
            myArr.removeAt(0)
            myArr.add(temp)
        }
    }

    private fun validateIndex(index: Int) : Int{
        var result = index - 1
        if(result < 0) result = 25
        return result
    }

    private fun getIndex(arrayList: ArrayList<String>, index : Int) : Int{
        val rotorChar = arrayList[validateIndex(index)]
        var getIndex = 0
        for(i in arrayList){
            getIndex++
            val temp = i.substring(1,i.length)
            if(rotorChar.substring(1,rotorChar.length) == temp){
                if(rotorChar != i){
                    break
                }
            }
        }
        return getIndex
    }
}
