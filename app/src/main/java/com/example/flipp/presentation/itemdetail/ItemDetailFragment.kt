package com.example.flipp.presentation.itemdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.flipp.R
import com.example.flipp.databinding.FragmentItemDetailBinding
import com.example.flipp.domain.models.Item
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class ItemDetailFragment : Fragment() {

    private var _binding: FragmentItemDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ItemDetailViewModel by viewModels()
    private val args: ItemDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeViewModel()
        viewModel.loadItem(args.itemId)
    }

    private fun setupUI() {
        binding.apply {
            btnEdit.setOnClickListener {
                val action =
                    ItemDetailFragmentDirections.actionItemDetailFragmentToItemEditFragment(args.itemId)
                findNavController().navigate(action)
            }

            btnDelete.setOnClickListener {
                showDeleteConfirmation()
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

        viewModel.deleteState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ItemDetailViewModel.DeleteUiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is ItemDetailViewModel.DeleteUiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }

                is ItemDetailViewModel.DeleteUiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun displayItem(item: Item) {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        binding.apply {
            titleItemDetail.text = item.name
            textItemPrice.text = "$${item.price}"
            textItemQuantity.text = item.quantity.toString()
            textItemCategory.text = item.category
            textItemDescription.text = item.description
            textSupplierName.text = item.supplierName
            textSupplierContact.text = item.supplierContact
            textItemUpdate.text = dateFormat.format(Date(item.lastUpdated))

            Glide.with(requireContext())
                .load(item.imageUrl)
                .placeholder(R.drawable.round_image_24)
                .error(R.drawable.round_broken_image)
                .into(imageItem)

        }
    }

    private fun showDeleteConfirmation() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Item")
            .setMessage("Are you sure you want to delete this item?")
            .setPositiveButton(android.R.string.ok) { _, _ ->
                viewModel.deleteItem(args.itemId)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .setIcon(R.drawable.ic_delete)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
