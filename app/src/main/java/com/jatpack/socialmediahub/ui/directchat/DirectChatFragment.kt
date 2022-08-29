package com.jatpack.socialmediahub.ui.directchat

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebStorage
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jatpack.socialmediahub.R
import com.jatpack.socialmediahub.adapter.NumberAdapter
import com.jatpack.socialmediahub.helper.Pref
import com.jatpack.socialmediahub.model.PersonNumber
import com.jatpack.socialmediahub.util.SetClick
import com.rilixtech.CountryCodePicker
import kotlin.collections.ArrayList


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
    private var cpp: CountryCodePicker? = null


    // This property is only valid between onCreateView and
    // onDestroyView.


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        numberPref = Pref(requireActivity())
        tempNumberList = ArrayList()

        rlWhatApp = view.findViewById(R.id.rl_whats_app)
        cpp = view.findViewById(R.id.ccp)
        phNumber = view.findViewById(R.id.phNumber)
        recyclerView = view.findViewById(R.id.recycler_View)
        openList = view.findViewById(R.id.contact_history)
        tvMsg = view.findViewById(R.id.tvMsg)
        openList?.setOnClickListener {
            loadData()
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
            Log.d("TAG", "onCreateView22: " + numberPref?.getNumberList()?.size)
            phNumber?.setText("")
            sendMessage(
                cpp?.selectedCountryCode + phNumber?.text.toString(),
                tvMsg?.text.toString()
            )
        }

        /*  if (numberPref?.getNumberList() != null) {*/






        cpp?.setOnCountryChangeListener {
            Toast.makeText(
                requireActivity(),
                "Updated " + it.name,
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun loadData() {
        tempNumberList = numberPref?.getNumberList()

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

        /*   SharedPreferences sh = getSharedPreferences("pref", MODE_PRIVATE);
        boolean status = sh.getBoolean("status",false);
*/
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
        TODO("Not yet implemented")
    }

    override fun onLongClcik(view: View, position: Int) {
        TODO("Not yet implemented")
    }
}