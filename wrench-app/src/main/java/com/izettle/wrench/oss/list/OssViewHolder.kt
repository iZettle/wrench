package com.izettle.wrench.oss.list

import android.support.v7.widget.RecyclerView
import com.izettle.wrench.databinding.OssListItemBinding
import com.izettle.wrench.oss.LicenceMetadata

class OssViewHolder(val binding: OssListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(licenceMetadata: LicenceMetadata) {
        binding.licenceMetadata = licenceMetadata
    }

}