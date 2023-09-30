package com.example.room_sample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.room_sample.databinding.ListItemBinding
import com.example.room_sample.db.Contact

class MyRecyclerViewAdapter(
    private val contacts: List<Contact>,
    private val clickListener: (Contact) -> Unit
) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.list_item, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(contacts[position], clickListener)
    }
}

class MyViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(contact: Contact, clickListener: (Contact) -> Unit) {
        binding.nameTextView.text = contact.name
        binding.phoneNoTextView.text = contact.phoneNo
        binding.listItemLayout.setOnClickListener {
            clickListener(contact)
        }
    }

}