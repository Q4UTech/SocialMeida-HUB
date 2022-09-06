package com.pds.socialmediahub.ui.directchat

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.WebStorage
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pds.socialmediahub.R
import com.pds.socialmediahub.adapter.NumberAdapter
import com.pds.socialmediahub.helper.Pref
import com.pds.socialmediahub.model.PersonNumber
import com.pds.socialmediahub.util.SetClick
import com.rilixtech.CountryCodePicker
import engine.app.adshandler.AHandler


class DirectChatFragment : Fragment(R.layout.fragment_notifications), SetClick {

    private var bottomSheetDialog: BottomSheetDialog? = null
    private var numberAdapter: NumberAdapter? = null
    private var tempNumberList: ArrayList<PersonNumber>? = null
    private var recyclerView: RecyclerView? = null
    private var openList: ImageView? = null
    private var msg: EditText? = null
    private var numberPref: Pref? = null
    private var rlWhatApp: RelativeLayout? = null
    private var senMsg: TextView? = null
    private var tvMsg: TextView? = null
    private var phNumber: EditText? = null
    private var ccp: CountryCodePicker? = null
    var clicked: Boolean = false
    private var tempList: ArrayList<PersonNumber> = ArrayList()


    // This property is only valid between onCreateView and
    // onDestroyView.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        numberPref = Pref(requireActivity())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tempNumberList = ArrayList()

        rlWhatApp = view.findViewById(R.id.rl_whats_app)
        ccp = view.findViewById(R.id.ccp)
        phNumber = view.findViewById(R.id.phNumber)
        recyclerView = view.findViewById(R.id.recycler_View)
        openList = view.findViewById(R.id.contact_history)
        tvMsg = view.findViewById(R.id.tvMsg)

        openList?.setOnClickListener {
            Log.d("TAG", "onViewCreated: ")
            if (!clicked) {
                clicked = true
                loadData()

            } else {
                recyclerView?.visibility = View.GONE
                clicked = false
            }

        }
        rlWhatApp?.setOnClickListener {

            showBottomSheetDialog()
        }
        senMsg = view.findViewById(R.id.send_msg)
        senMsg?.setOnClickListener {

            tempNumberList?.add(
                PersonNumber(
                    phNumber?.text.toString(),
                    System.currentTimeMillis().toString()
                )
            )
            numberPref?.setNumberList(tempNumberList)
            numberPref?.getNumberList()?.let {
                tempList.clear()
                tempList.addAll(it)

            }
            Log.d("TAG", "onCreateView22: " + numberPref?.getNumberList()?.size)


            if (phNumber != null && phNumber?.length()!! > 9) {
                sendMessage(
                    ccp?.selectedCountryCode + phNumber?.text.toString(),
                    tvMsg?.text.toString()
                )
            }
        }
        view.findViewById<LinearLayout>(R.id.ll_ads)
            .addView(AHandler.getInstance().getNativeLarge(requireActivity()))
        /*  if (numberPref?.getNumberList() != null) {*/

        tvMsg?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

                if (p0?.length!! > 0) {
                    senMsg?.background = resources.getDrawable(R.drawable.ic_send_active, null)
                    senMsg?.setTextColor(resources.getColor(R.color.white))
                } else {
                    senMsg?.background = resources.getDrawable(R.drawable.ic_senc_inactive, null)
                    senMsg?.setTextColor(resources.getColor(R.color.button_light))
                }
            }
        })




        ccp?.setOnCountryChangeListener {
            Toast.makeText(
                requireActivity(),
                "Updated " + it.name,
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun loadData() {

        tempNumberList = numberPref?.getNumberList()
        Log.d("TAG", "onCreateView11: " + tempNumberList?.size)
        if (tempNumberList != null && tempNumberList?.size!! > 0) {
            recyclerView?.visibility = View.VISIBLE
            Log.d("TAG", "onCreateView: " + tempNumberList?.size)
            numberAdapter = NumberAdapter(requireActivity(), tempNumberList!!, this)
            val layoutManager = LinearLayoutManager(requireActivity())
            recyclerView?.layoutManager = layoutManager
            recyclerView?.adapter = numberAdapter

        } else {
            recyclerView?.visibility = View.GONE
        }
    }


    override fun onResume() {
        super.onResume()

    }

    private fun sendMessage(mobilenumber: String, text: String) {
        val browserIntent: Intent
        val isWhatsappInstalled: Boolean = whatsappInstalledOrNot("com.whatsapp")
        if (isWhatsappInstalled) {
            browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://api.whatsapp.com/send?phone=$mobilenumber&text=$text")
            )
            startActivity(browserIntent)
        } else {
            val uri: Uri = Uri.parse("market://details?id=com.whatsapp")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            Toast.makeText(
                requireActivity(), "WhatsApp not Installed",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(goToMarket)
        }
        tvMsg?.setText("")
        phNumber?.setText("")
    }

    fun showBottomSheetDialog() {


        bottomSheetDialog = BottomSheetDialog(requireActivity(), R.style.BottomSheetDialog)
        bottomSheetDialog?.setContentView(R.layout.login_bottom_sheet)
        val login = bottomSheetDialog?.findViewById<CheckBox>(R.id.login)
        val logout = bottomSheetDialog?.findViewById<CheckBox>(R.id.logout)
        val close = bottomSheetDialog?.findViewById<ImageView>(R.id.close)
        close?.setOnClickListener {
            bottomSheetDialog?.dismiss()
        }
        login?.setOnClickListener {
            startWhatsWebScan()
            bottomSheetDialog?.dismiss()
        }
        logout?.setOnClickListener {
            WebStorage.getInstance().deleteAllData();
            requireActivity().finish()
            startWhatsWebScan()
            bottomSheetDialog?.dismiss()
        }


        bottomSheetDialog?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    private fun startWhatsWebScan() {
        val intent = Intent(requireActivity(), WhatsAppWebView::class.java)
        startActivity(intent)
    }

    private fun whatsappInstalledOrNot(uri: String): Boolean {
        val pm: PackageManager = requireActivity().packageManager
        var app_installed = false
        app_installed = try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return app_installed
    }

    override fun onClick(view: View, position: Int) {
        tempNumberList = numberPref?.getNumberList();

        if (view.id == R.id.cross) {
            Log.d("TAG", "onClick: " + tempNumberList?.size + "," + tempList.size)
            tempList.remove(tempNumberList!![position])
            numberPref?.setNumberList(tempList)
            numberAdapter?.updateList(tempList)
            recyclerView?.visibility = View.GONE
        } else if (view.id == R.id.rl) {
            Log.d("TAG", "onClick1: ")
            numberPref?.getNumberList()?.let {
                phNumber?.setText(it[position].contactNumber)
            }
            recyclerView?.visibility = View.GONE
        }
    }

    override fun onLongClcik(view: View, position: Int) {
        TODO("Not yet implemented")
    }
}