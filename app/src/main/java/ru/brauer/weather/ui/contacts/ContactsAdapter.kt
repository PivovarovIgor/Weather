package ru.brauer.weather.ui.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.brauer.weather.databinding.RecyclerItemContactBinding

class ContactsAdapter : RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    var data: MutableList<String> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            RecyclerItemContactBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(val binding: RecyclerItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: String) {
            binding.contactName.text = contact
        }
    }
}
