package com.example.flipp.presentation.itemedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.flipp.databinding.FragmentItemEditBinding
import com.example.flipp.domain.models.Item
import com.example.flipp.presentation.itemdetail.ItemDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ItemEditFragment : Fragment() {

    private var _binding: FragmentItemEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ItemEditViewModel by viewModels()
    private val args: ItemEditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentItemEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeViewModel()
        viewModel.loadItem(args.itemId)
    }

    private fun setupUI() {
        binding.btnSaveChanges.setOnClickListener {
            if (validateInput()) {
                binding.btnSaveChanges.isEnabled = false
                binding.btnSaveChanges.text = "Saving..."
                saveChanges(args.itemId)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.itemUiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ItemDetailViewModel.ItemUiState.Loading -> {}
                is ItemDetailViewModel.ItemUiState.Success -> {
                    displayItem(state.item)
                }

                is ItemDetailViewModel.ItemUiState.Error -> {
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.editUiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ItemEditViewModel.EditUiState.Loading -> {}
                is ItemEditViewModel.EditUiState.Success -> {
                    binding.btnSaveChanges.isEnabled = true
                    binding.btnSaveChanges.text = "Changes Saved"
                    Toast.makeText(context, "Item updated successfully", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }

                is ItemEditViewModel.EditUiState.Error -> {
                    binding.btnSaveChanges.isEnabled = true
                    binding.btnSaveChanges.text = "Save Changes"
                    Toast.makeText(context, "Failed to update item", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun displayItem(item: Item) {
        binding.apply {
            etName.setText(item.name)
            etDescription.setText(item.description)
            etQuantity.setText(item.quantity.toString())
            etPrice.setText(item.price.toString())
            etCategory.setText(item.category)
            etSupplierName.setText(item.supplierName)
            etSupplierContact.setText(item.supplierContact)
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

            if (etQuantity.text.toString().isEmpty() || etQuantity.text.toString()
                    .toIntOrNull() == null
            ) {
                etQuantity.error = "Quantity must be a valid number"
                isValid = false
            } else {
                etQuantity.error = null
            }

            if (etPrice.text.toString().isEmpty() || etPrice.text.toString()
                    .toDoubleOrNull() == null
            ) {
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

    private fun saveChanges(itemId: Long) {
        binding.apply {
            val updatedItem = Item(
                id = itemId,
                name = etName.text.toString(),
                description = etDescription.text.toString(),
                quantity = etQuantity.text.toString().toInt(),
                price = etPrice.text.toString().toDouble(),
                category = etCategory.text.toString(),
                supplierName = etSupplierName.text.toString(),
                supplierContact = etSupplierContact.text.toString(),
                lastUpdated = System.currentTimeMillis(),
                imageUrl = "https://picsum.photos/id/${itemId + 10}/200/200"
            )

            viewModel.updateItem(updatedItem)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
