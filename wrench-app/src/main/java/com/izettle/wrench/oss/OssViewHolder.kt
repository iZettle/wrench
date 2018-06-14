package com.izettle.wrench.oss

import android.support.v7.widget.RecyclerView
import com.izettle.wrench.databinding.OssListItemBinding

class OssViewHolder(val binding: OssListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(licenceMetadata: LicenceMetadata) {
        binding.licenceMetadata = licenceMetadata
    }

}