package ru.brauer.weather.ui.contacts

import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import ru.brauer.weather.R
import ru.brauer.weather.databinding.FragmentContactsBinding

class ContactsFragment : Fragment() {

    private var binding: FragmentContactsBinding? = null
    private val adapter: ContactsAdapter by lazy { ContactsAdapter() }
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    getContacts()
                } else {
                    context?.let { it ->
                        AlertDialog.Builder(it)
                            .setTitle(R.string.title_of_request_permission_to_contact_access)
                            .setMessage(R.string.explanation_of_request_permission_to_contact_access)
                            .setNegativeButton(R.string.close) { dialog, _ ->
                                dialog.dismiss()
                                binding?.let { bnd ->
                                    Navigation.findNavController(bnd.root)
                                        .navigate(R.id.navigation_home)
                                }
                            }
                            .setCancelable(false)
                            .create()
                            .show()
                    }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            listOfContacts.adapter = adapter
        }
        checkPermission()
    }

    private fun checkPermission() {
        context?.let {
            when {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    getContacts()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                    AlertDialog.Builder(it)
                        .setTitle(R.string.explanation_of_request_permission_to_contact_access)
                        .setMessage(R.string.explanation_of_request_permission_to_contact_access)
                        .setPositiveButton(R.string.grant_access) { _, _ ->
                            requestPermission()
                        }
                        .setNegativeButton(R.string.do_not) { dialog, _ ->
                            dialog.dismiss()
                            binding?.let { bnd ->
                                Navigation.findNavController(bnd.root)
                                    .navigate(R.id.navigation_home)
                            }
                        }
                        .setCancelable(false)
                        .create()
                        .show()
                }
                else -> {
                    requestPermission()
                }
            }
        }
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
    }

    private fun getContacts() {
        context?.let {
            val contentResolver: ContentResolver = it.contentResolver
            val cursorWithContacts: Cursor? = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )
            val contacts = mutableListOf<String>()
            cursorWithContacts?.let { cursor ->
                for (i in 0..cursor.count) {
                    if (cursor.moveToPosition(i)) {
                        contacts += cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    }
                }
            }
            cursorWithContacts?.close()
            adapter.data = contacts
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}