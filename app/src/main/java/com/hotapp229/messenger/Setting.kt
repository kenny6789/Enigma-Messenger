package com.hotapp229.messenger

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.setting.*

class Setting : AppCompatActivity() {
    private val enigma = SuperMagic()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting)
        rotorsSetup()
        testSave()
        testRead()
    }
    private fun rotorsSetup(){
        //rotor 1
        rotorDecrease(button_left_rotor1, button_rotor1)
        rotorIncrease(button_right_rotor1, button_rotor1)

        //rotor 2
        rotorDecrease(button_left_rotor2, button_rotor2)
        rotorIncrease(button_right_rotor2, button_rotor2)

        //rotor 3
        rotorDecrease(button_left_rotor3, button_rotor3)
        rotorIncrease(button_right_rotor3, button_rotor3)

        //rotor 1 position
        rotorDecrease(button_left_position_rotor1,button_position_rotor1)
        rotorIncrease(button_right_position_rotor1,button_position_rotor1)

        //rotor 2 position
        rotorDecrease(button_left_position_rotor2,button_position_rotor2)
        rotorIncrease(button_right_position_rotor2,button_position_rotor2)

        //rotor 3 position
        rotorDecrease(button_left_position_rotor3,button_position_rotor3)
        rotorIncrease(button_right_position_rotor3,button_position_rotor3)
    }
    private fun rotorDecrease(button: Button, view : Button){
        button.setOnClickListener {
            when(view.text){
                "26" -> {view.text = "25"}
                "25" -> {view.text = "24"}
                "24" -> {view.text = "23"}
                "23" -> {view.text = "22"}
                "22" -> {view.text = "21"}
                "21" -> {view.text = "20"}
                "20" -> {view.text = "19"}
                "19" -> {view.text = "18"}
                "18" -> {view.text = "17"}
                "17" -> {view.text = "16"}
                "16" -> {view.text = "15"}
                "15" -> {view.text = "14"}
                "14" -> {view.text = "13"}
                "13" -> {view.text = "12"}
                "12" -> {view.text = "11"}
                "11" -> {view.text = "10"}
                "10" -> {view.text = "9"}
                "9" -> {view.text = "8"}
                "8" -> {view.text = "7"}
                "7" -> {view.text = "6"}
                "6" -> {view.text = "5"}
                "5" -> {view.text = "4"}
                "4" -> {view.text = "3"}
                "3" -> {view.text = "2"}
                "2" -> {view.text = "1"}
            }
        }
    }
    private fun rotorIncrease(button: Button, view : Button){
        button.setOnClickListener {
            when(view.text){
                "1" -> {view.text = "2"}
                "2" -> {view.text = "3"}
                "3" -> {view.text = "4"}
                "4" -> {view.text = "5"}
                "5" -> {view.text = "6"}
                "6" -> {view.text = "7"}
                "7" -> {view.text = "8"}
                "8" -> {view.text = "9"}
                "9" -> {view.text = "10"}
                "10" -> {view.text = "11"}
                "11" -> {view.text = "12"}
                "12" -> {view.text = "13"}
                "13" -> {view.text = "14"}
                "14" -> {view.text = "15"}
                "15" -> {view.text = "16"}
                "16" -> {view.text = "17"}
                "17" -> {view.text = "18"}
                "18" -> {view.text = "19"}
                "19" -> {view.text = "20"}
                "20" -> {view.text = "21"}
                "21" -> {view.text = "22"}
                "22" -> {view.text = "23"}
                "23" -> {view.text = "24"}
                "24" -> {view.text = "25"}
                "25" -> {view.text = "26"}
            }
        }
    }
    private fun testSave(){
        btnOK.setOnClickListener {
            //var rotorsetup = RotorSetup(this, button_rotor1.text.toString(), button_rotor2.text.toString(), button_rotor3.text.toString(), button_position_rotor1.text.toString(), button_position_rotor2.text.toString(), button_position_rotor3.text.toString())
            var plugboardSetup = 0
            var checkedID = rgPlugboard.checkedRadioButtonId
            when(checkedID) {
                R.id.rbOption1 -> plugboardSetup = 1
                R.id.rbOption2 -> plugboardSetup = 2
                R.id.rbOption3 -> plugboardSetup = 3
                R.id.rbOption4 -> plugboardSetup = 4
            }
            enigma.saveRotorSetting(this, button_rotor1.text.toString(), button_rotor2.text.toString(), button_rotor3.text.toString(), button_position_rotor1.text.toString(), button_position_rotor2.text.toString(),button_position_rotor3.text.toString(), plugboardSetup.toString())
        }
    }
    private fun testRead() {
        btnClearPlugboard.setOnClickListener {
           // enigma.readRotorSetup(this)
            enigma.startEncrypting("The ministry will now consider if there are grounds for an investigation. An investigation could take up to 270 days but provisional measure can be imposed earlier if necessary. \"We are right now negotiating a free trade agreement with the EU and the EU may not like it, so it could be a complication in terms of that negotiation,” said Charles Finny. Potatoes New Zealand has filed a complaint of dumping with the Ministry of Business, Innovation and Employment. It’s also applied for tariffs to be imposed on the imports.", this)
            enigma.startEncrypting("Kcv nlcmccyt bdux fdg aacytjpd yb mgojc wdc uviglpz lpp jg gksdxlaiiyhlw. Rr wgaoogczuirmi fuqsa apbc au ie 270 nqzz stu ydqxmfyqefz eqeqxmb rov un lxhdrhw thurbkj bn qaemlklxq. \"Tu mfw vmmqf tet qbsbbslosac o izsy hhkfs uhgcbzczm rgoo qsi ZR idx gls OF cnc cid dyyp rf, ll nw dfexh jk w xudaiqdbidie lm cnzhp un pblp ptyaodwarnr,” vods Kpunxzl Qgfgq. Ozmnzazb Jcy Ipmshes kww lhrke o kavfxsvbr wo izlzqxl xmkj hsa Dgsjezxm se Zjktbdom, Ydgcqqvfzm nsu Vwbythbcti. Ej’i kwgs bsontfi xfu nfyqvhz wi ij wnvanhr xk ama nvrxtrv.", this)


            //DBShowRecentMessage.deleteMessage("221989229")
        }
}


}
class RotorSetup(val context: Context ,val rotor1: String, val rotor2: String, val rotor3: String, val rotor1Pos: String, val rotor2Pos: String, val rotor3Pos: String)