package com.jatpack.socialmediahub.ui.status

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.os.storage.StorageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.tabs.TabLayout
import com.jatpack.socialmediahub.R
import com.jatpack.socialmediahub.adapter.StoryViewPagerAdapter
import com.jatpack.socialmediahub.helper.MediaPreferences


class StatusFragment : Fragment() {

//    private var _binding: FragmentHomeBinding? = null

    private var preferences: MediaPreferences? = null

    private var tabcountvideo = false
    private var pager: ViewPager? = null
    private var tabs: TabLayout? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
//    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)

//        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//        val root: View = binding.root

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }


        println("StatusFragment.onCreateView hello 001")
        val root = inflater.inflate(R.layout.fragment_status, container, false)
        pager = root.findViewById(R.id.pager)
        tabs = root.findViewById(R.id.tabs)
        preferences = MediaPreferences(activity)


        val adapter = StoryViewPagerAdapter(childFragmentManager)
        pager!!.adapter = adapter

        tabs!!.post {
            tabs!!.setupWithViewPager(pager)
            tabs!!.getTabAt(0)?.setIcon(R.drawable.ic_recent_status_tab_icon)
            //                tabs.getTabAt(1).setIcon(R.drawable.ic_tab_video);
        }




        pager!!.setOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {
                println("onscrool 1 gfhafHga $i")
                tabcountvideo = if (i == 1) {
                    true
                } else {
                    false
                }
            }

            override fun onPageSelected(i: Int) {
                if (i == 0) {
                    tabs!!.getTabAt(0)?.setIcon(R.drawable.ic_recent_status_tab_icon)
                    tabs!!.getTabAt(1)?.setIcon(null)
//                    adapter.disableSelection()
                } /*else if (i == 1) {
                    tabs!!.getTabAt(1)?.setIcon(R.drawable.ic_tranding_tab_icon)
                    tabs!!.getTabAt(0)?.setIcon(null)
//                    adapter.disableSelection()
                }*/
            }

            override fun onPageScrollStateChanged(i: Int) {}
        })


        return root
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    fun getAllFile() {
        val intent: Intent
        val sm = requireActivity().getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val statusDir = "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2F.Statuses"
        val str = "android.provider.extra.INITIAL_URI"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            intent = sm.primaryStorageVolume.createOpenDocumentTreeIntent()
            val scheme = (intent.getParcelableExtra<Parcelable>(str) as Uri?).toString()
                .replace("/root/", "/document/")
            val stringBuilder = scheme +
                    "%3A" +
                    statusDir
            intent.putExtra(str, Uri.parse(stringBuilder))
            startActivity(intent)
            println("FindPathActivity.onActivityResult fileUri0022bbb: " + Uri.parse(stringBuilder))
        } else {
            intent = Intent("android.intent.action.OPEN_DOCUMENT_TREE")
            intent.putExtra(str, Uri.parse(statusDir))
        }
        println("FindPathActivity.onActivityResult fileUri00helloooo123: " + Uri.parse(statusDir))
        startActivityForResult(intent, 101)

//        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("StatusFragment.onCreateView hello 004 iuiuiu")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        println("StatusFragment.onCreateView hello 003")

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("StatusFragment.onCreateView hello 002")


    }

    fun fetchFile() {
//        val arrayList = ArrayList<DocumentFile>()
//        val strmyuri: String = preferences?.getFileUri()!!
//        val myUri = Uri.parse(strmyuri)
//        val docFile = DocumentFile.fromTreeUri(requireActivity(), myUri)
//        println("temp = ck1" + docFile!!.canRead() + " " + docFile.canWrite() + " " + " " + docFile.toString())
//        for (file in docFile.listFiles()) {
//            arrayList.add(file)
//        }


//        System.out.println("temp = chk111" + documentFile.get(2).canRead()+" "+documentFile.get(2).canWrite()+ " ");


    }



}