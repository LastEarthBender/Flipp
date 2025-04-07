package com.example.flipp.presentation.itemadd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.flipp.databinding.FragmentItemAddBinding
import com.example.flipp.domain.models.Item
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ItemAddFragment : Fragment() {

    private var _binding: FragmentItemAddBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ItemAddViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.btnAddItem.setOnClickListener {
            if (validateInput()) {
                binding.btnAddItem.isEnabled = false
                binding.btnAddItem.text = "Saving..."
                saveItem()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.addUiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ItemAddViewModel.AddUiState.Loading -> {}
                is ItemAddViewModel.AddUiState.Success -> {
                    binding.btnAddItem.isEnabled = true
                    binding.btnAddItem.text = "Item Added"
                    Toast.makeText(context, "Item added successfully", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                is ItemAddViewModel.AddUiState.Error -> {}
            }
        }
    }

    private fun validateInput(): Boolean {
        binding.apply {
            var isValid = true

            if (etName.text.toString().isEmpty()) {
                etName.error = "Name is required"
                isValid = false
            } else {
                etName.error = null
            }

            if (etDescription.text.toString().isEmpty()) {
                etDescription.error = "Description is required"
                isValid = false
            } else {
                etDescription.error = null
            }

            if (etQuantity.text.toString().isEmpty() || etQuantity.text.toString().toIntOrNull() == null) {
                etQuantity.error = "Quantity must be a valid number"
                isValid = false
            } else {
                etQuantity.error = null
            }

            if (etPrice.text.toString().isEmpty() || etPrice.text.toString().toDoubleOrNull() == null) {
                etPrice.error = "Price must be a valid number"
                isValid = false
            } else {
                etPrice.error = null
            }

            if (etCategory.text.toString().isEmpty()) {
                etCategory.error = "Category is required"
                isValid = false
            } else {
                etCategory.error = null
            }

            if (etSupplierName.text.toString().isEmpty()) {
                etSupplierName.error = "Supplier name is required"
                isValid = false
            } else {
                etSupplierName.error = null
            }

            if (etSupplierContact.text.toString().isEmpty()) {
                etSupplierContact.error = "Supplier contact is required"
                isValid = false
            } else {
                etSupplierContact.error = null
            }

            return isValid
        }
    }

    private fun saveItem() {
        val newItem = Item(
            name = binding.tlName.editText?.text.toString(),
            description = binding.tlDescription.editText?.text.toString(),
            quantity = binding.tlQuantity.editText?.text.toString().toIntOrNull() ?: 0,
            price = binding.tlPrice.editText?.text.toString().toDoubleOrNull() ?: 0.0,
            category = binding.tlCategory.editText?.text.toString(),
            supplierName = binding.tlSupplierName.editText?.text.toString(),
            supplierContact = binding.tlSupplierContact.editText?.text.toString(),
            lastUpdated = System.currentTimeMillis(),
            imageUrl = "https://picsum.photos/id/${(4..50).random() + 10}/200/200"
        )

        viewModel.addItem(newItem)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
