package com.example.flipp.presentation.itemslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flipp.databinding.FragmentItemListBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels


@AndroidEntryPoint
class ItemListFragment : Fragment() {

    private var _binding: FragmentItemListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ItemListViewModel by viewModels()
    private lateinit var adapter: ItemListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupFab()
        observeViewModel()
        viewModel.loadItems()
    }

    private fun setupRecyclerView() {
        adapter = ItemListAdapter { item ->
            val action = ItemListFragmentDirections.actionItemListFragmentToItemDetailFragment(item.id)
            findNavController().navigate(action)
        }
        binding.recyclerItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ItemListFragment.adapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.loadItems()
        }
    }

    private fun setupFab() {
        binding.fabAddItem.setOnClickListener {
            val action = ItemListFragmentDirections.actionItemListFragmentToItemAddFragment()
            findNavController().navigate(action)
        }
    }

    private fun observeViewModel() {
        viewModel.itemsUiState.observe(viewLifecycleOwner) { state ->
            binding.swipeRefreshLayout.isRefreshing = state is ItemListViewModel.ItemsUiState.Loading

            when (state) {
                is ItemListViewModel.ItemsUiState.Loading -> {
                    binding.recyclerItems.visibility = View.GONE
                }

                is ItemListViewModel.ItemsUiState.Success -> {
                    if (state.items.isEmpty()) {
                        binding.recyclerItems.visibility = View.GONE
                        binding.emptyMessage.visibility = View.VISIBLE
                    } else {
                        binding.recyclerItems.visibility = View.VISIBLE
                        binding.emptyMessage.visibility = View.GONE
                        adapter.submitList(state.items)
                    }
                }
                is ItemListViewModel.ItemsUiState.Error -> {
                    binding.recyclerItems.visibility = View.GONE
                    binding.emptyMessage.text = state.message
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
