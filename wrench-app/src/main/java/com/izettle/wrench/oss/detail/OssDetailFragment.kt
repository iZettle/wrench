package com.izettle.wrench.oss.detail

import android.app.Dialog
import android.os.Bundle
import android.text.util.Linkify
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.text.util.LinkifyCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.izettle.wrench.databinding.FragmentOssDetailBinding
import com.izettle.wrench.oss.LicenceMetadata
import org.koin.androidx.viewmodel.ext.android.viewModel

class OssDetailFragment : DialogFragment() {

    private lateinit var binding: FragmentOssDetailBinding

    private val viewModel: OssDetailViewModel by viewModel()

    companion object {
        @JvmStatic
        fun newInstance(args: OssDetailFragmentArgs): OssDetailFragment {
            val fragment = OssDetailFragment()
            fragment.arguments = args.toBundle()
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val ossDetailFragmentArgs = OssDetailFragmentArgs.fromBundle(arguments!!)

        binding = FragmentOssDetailBinding.inflate(LayoutInflater.from(context))

        val licenceMetadata = LicenceMetadata(ossDetailFragmentArgs.dependency, ossDetailFragmentArgs.skip.toLong(), ossDetailFragmentArgs.length)

        viewModel.getThirdPartyMetadata(licenceMetadata).observe(this, Observer {
            binding.text.text = it
            LinkifyCompat.addLinks(binding.text, Linkify.WEB_URLS)
        })

        return AlertDialog.Builder(activity!!)
                .setTitle(licenceMetadata.dependency)
                .setView(binding.root)
                .setPositiveButton("dismiss") { _, _ ->
                    dismiss()
                }
                .create()
    }
}