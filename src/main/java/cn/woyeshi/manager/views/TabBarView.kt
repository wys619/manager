package cn.woyeshi.manager.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import android.widget.LinearLayout
import cn.woyeshi.manager.R
import kotlinx.android.synthetic.main.view_tab_bar.view.*

class TabBarView : LinearLayout, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    var onTabSelectedListener: IOnTabSelectedListener? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.view_tab_bar, this, true)
        initViews()
    }

    private fun initViews() {
        llMainTab.setOnClickListener(this)
        llTaskTab.setOnClickListener(this)
        llNewsTab.setOnClickListener(this)
        llMineTab.setOnClickListener(this)
        llSendTask.setOnClickListener(this)
        cbMainTab.setOnCheckedChangeListener(this)
        cbTaskTab.setOnCheckedChangeListener(this)
        cbNewsTab.setOnCheckedChangeListener(this)
        cbMineTab.setOnCheckedChangeListener(this)
    }

    override fun onClick(v: View) {
        val id = v.id
        when (id) {
            R.id.llMainTab -> {
                cbMainTab.isChecked = true
                cbTaskTab.isChecked = false
                cbNewsTab.isChecked = false
                cbMineTab.isChecked = false
            }
            R.id.llTaskTab -> {
                cbMainTab.isChecked = false
                cbTaskTab.isChecked = true
                cbNewsTab.isChecked = false
                cbMineTab.isChecked = false
            }
            R.id.llNewsTab -> {
                cbMainTab.isChecked = false
                cbTaskTab.isChecked = false
                cbNewsTab.isChecked = true
                cbMineTab.isChecked = false
            }
            R.id.llMineTab -> {
                cbMainTab.isChecked = false
                cbTaskTab.isChecked = false
                cbNewsTab.isChecked = false
                cbMineTab.isChecked = true
            }
            R.id.llSendTask -> {
                onTabSelectedListener?.onTabSelected(R.id.llSendTask)
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (!isChecked) {
            return
        }
        val id = buttonView.id
        onTabSelectedListener?.onTabSelected(id)
    }

    fun checkTab(cbId: Int) {
        when (cbId) {
            R.id.cbMainTab -> {
                llMainTab.performClick()
            }
            R.id.cbTaskTab -> {
                llTaskTab.performClick()
            }
            R.id.cbNewsTab -> {
                llNewsTab.performClick()
            }
            R.id.cbMineTab -> {
                llMineTab.performClick()
            }
        }
    }

    fun showRedPoint(flag: Boolean) {
        if (flag) {
            newsRedPoint.visibility = View.VISIBLE
        } else {
            newsRedPoint.visibility = View.GONE
        }
    }

    interface IOnTabSelectedListener {
        fun onTabSelected(tabId: Int)
    }

}