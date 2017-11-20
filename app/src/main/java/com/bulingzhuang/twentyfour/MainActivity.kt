package com.bulingzhuang.twentyfour

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.support.transition.Fade
import android.support.transition.TransitionManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val mRandom = Random()
    private val mCardListA = arrayOf(Pair(R.mipmap.card_a_0, R.mipmap.card_a_0_r),
            Pair(R.mipmap.card_a_1, R.mipmap.card_a_1_r),
            Pair(R.mipmap.card_a_2, R.mipmap.card_a_2_r),
            Pair(R.mipmap.card_a_3, R.mipmap.card_a_3_r))
    private val mCardListJ = arrayOf(Pair(R.mipmap.card_j_0, R.mipmap.card_j_0_r),
            Pair(R.mipmap.card_j_1, R.mipmap.card_j_1_r),
            Pair(R.mipmap.card_j_2, R.mipmap.card_j_2_r),
            Pair(R.mipmap.card_j_3, R.mipmap.card_j_3_r))
    private val mCardListQ = arrayOf(Pair(R.mipmap.card_q_0, R.mipmap.card_q_0_r),
            Pair(R.mipmap.card_q_1, R.mipmap.card_q_1_r),
            Pair(R.mipmap.card_q_2, R.mipmap.card_q_2_r),
            Pair(R.mipmap.card_q_3, R.mipmap.card_q_3_r))
    private val mCardListK = arrayOf(Pair(R.mipmap.card_k_0, R.mipmap.card_k_0_r),
            Pair(R.mipmap.card_k_1, R.mipmap.card_k_1_r),
            Pair(R.mipmap.card_k_2, R.mipmap.card_k_2_r),
            Pair(R.mipmap.card_k_3, R.mipmap.card_k_3_r))

    private val mCardList = ArrayList<CardData>()//卡牌List

    companion object {
        val delayDuration = 233L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewsOnClickListener(btn_start, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_10, btn_11, btn_12, btn_13,
                btn_add, btn_subtraction, btn_multiplication, btn_division, btn_bracket_start, btn_bracket_end, btn_calculate, btn_c, btn_ac, btn_copy)
    }

    private fun initAnimSet(vararg views: View): AnimatorSet {
        val set = AnimatorSet()
        val animList = ArrayList<Animator>()
        for (position in views.indices) {
            val itemView = views[position]
            val realRotationY = itemView.rotationY
            val rotationY = if (realRotationY >= 360) {
                realRotationY - (realRotationY / 360).toInt() * 360
            } else {
                realRotationY
            }

            val anim = ObjectAnimator.ofFloat(itemView, "rotationY", rotationY, rotationY + 180f).setDuration(1000)
            anim.startDelay = position % 4 * delayDuration
            anim.addUpdateListener {
                val realFl = it.animatedValue as Float
                val fl = if (realFl >= 180) {
                    Pair(true, realFl - 180)

                } else {
                    Pair(false, realFl)
                }
                if (Math.abs(fl.second) >= 90) {
                    if (position < mCardList.size) {
                        val itemCardData = mCardList[position]
                        when (position) {
                            0 -> {
                                if (fl.first) {
                                    iv_card_0.setImageResource(itemCardData.resourceId)
                                } else {
                                    iv_card_0.setImageResource(itemCardData.resourceIdr)
                                }
                            }
                            1 -> {
                                if (fl.first) {
                                    iv_card_1.setImageResource(itemCardData.resourceId)
                                } else {
                                    iv_card_1.setImageResource(itemCardData.resourceIdr)
                                }
                            }
                            2 -> {
                                if (fl.first) {
                                    iv_card_2.setImageResource(itemCardData.resourceId)
                                } else {
                                    iv_card_2.setImageResource(itemCardData.resourceIdr)
                                }
                            }
                            3 -> {
                                if (fl.first) {
                                    iv_card_3.setImageResource(itemCardData.resourceId)
                                } else {
                                    iv_card_3.setImageResource(itemCardData.resourceIdr)
                                }
                            }
                        }
                    }
                }
            }
            animList.add(anim)
        }
        set.playTogether(animList)
        return set
    }

    private fun setViewsOnClickListener(vararg views: View) {
        views.forEach { it.setOnClickListener(this) }
    }

    private fun randomCard() {
        mCardList.clear()
        for (position in 0..3) {
            val realNum = mRandom.nextInt(13)
            mCardList.add(getCard(realNum))
        }
        initAnimSet(iv_card_0, iv_card_1, iv_card_2, iv_card_3).start()
    }

    private fun setText(str: String) {
        when (str) {
            resources.getString(R.string.keyboard_c) -> {
                val resultStr = tv_calculator_result.text
                if (resultStr.isEmpty()) {
                    val currentStr = tv_calculator.text
                    if (currentStr.isNotEmpty()) {
                        tv_calculator.text = currentStr.substring(0, currentStr.length - 1)
                    }
                } else {
                    tv_calculator_result.text = ""
                }
            }
            resources.getString(R.string.keyboard_ac) -> {
                tv_calculator.text = ""
                tv_calculator_result.text = ""
            }
            resources.getString(R.string.keyboard_calculate) -> {//计算
                //TODO 根据前面是否可以组成公式计算值
            }
            else -> {
                val oldStr = tv_calculator.text.toString()
                val currentStr = if (oldStr.isNotEmpty()) {
                    if (oldStr.substring(oldStr.length - 1, oldStr.length) == str) {
                        oldStr
                    } else {
                        oldStr + str
                    }
                } else {
                    oldStr + str
                }
                tv_calculator.text = currentStr
            }
        }
    }

    /**
     * 获取卡片对应的资源id
     */
    private fun getCard(realNum: Int): CardData {
        val num = realNum + 1
        val (resourceId, resourceIdr) = when (num) {
            1 -> {
                val randomN = mRandom.nextInt(4)
                mCardListA[randomN]
            }
            11 -> {
                val randomN = mRandom.nextInt(4)
                mCardListJ[randomN]
            }
            12 -> {
                val randomN = mRandom.nextInt(4)
                mCardListQ[randomN]
            }
            13 -> {
                val randomN = mRandom.nextInt(4)
                mCardListK[randomN]
            }
            2 -> Pair(R.mipmap.card_2, R.mipmap.card_2_r)
            3 -> Pair(R.mipmap.card_3, R.mipmap.card_3_r)
            4 -> Pair(R.mipmap.card_4, R.mipmap.card_4_r)
            5 -> Pair(R.mipmap.card_5, R.mipmap.card_5_r)
            6 -> Pair(R.mipmap.card_6, R.mipmap.card_6_r)
            7 -> Pair(R.mipmap.card_7, R.mipmap.card_7_r)
            8 -> Pair(R.mipmap.card_8, R.mipmap.card_8_r)
            9 -> Pair(R.mipmap.card_9, R.mipmap.card_9_r)
            10 -> Pair(R.mipmap.card_10, R.mipmap.card_10_r)
            else -> {
                Pair(R.mipmap.card_back_1, R.mipmap.card_back_1_r)
            }
        }
        return CardData(num, resourceId, resourceIdr)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_start -> {//开始
                randomCard()
                TransitionManager.beginDelayedTransition(container, Fade())
                ll_keyboard.visibility = View.VISIBLE
                btn_start.visibility = View.GONE
            }
            R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9, R.id.btn_10,
            R.id.btn_11, R.id.btn_12, R.id.btn_13, R.id.btn_bracket_start, R.id.btn_bracket_end, R.id.btn_add, R.id.btn_subtraction,
            R.id.btn_multiplication, R.id.btn_division, R.id.btn_c, R.id.btn_ac, R.id.btn_calculate
            -> {//keyboard按键对应操作
                setText((v as Button).text.toString())
            }
            R.id.btn_copy -> {//复制
                val calculatorStr = tv_calculator.text.toString() + tv_calculator_result.text.toString()
                if (calculatorStr.isNotEmpty()) {
                    val cm = getSystemService(android.content.Context.CLIPBOARD_SERVICE) as ClipboardManager
                    cm.primaryClip = ClipData.newPlainText("Label", calculatorStr)
                    Toast.makeText(this, "已保存到剪贴板", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
