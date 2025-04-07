package com.example.flipp.presentation.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.flipp.databinding.FragmentDashboardBinding
import com.example.flipp.domain.models.Category
import com.example.flipp.domain.models.DashboardSummary
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by viewModels()
    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.apply {
            pieChart = categoryPieChart
            pieChart.apply {
                description.isEnabled = false
                setUsePercentValues(true)
                setEntryLabelTextSize(12f)
                setEntryLabelColor(Color.WHITE)
                centerText = "Categories"
                setCenterTextSize(16f)
                legend.isEnabled = true
            }

            barChart = stockLevelBarChart
            barChart.apply {
                description.isEnabled = false
                setDrawGridBackground(false)
                legend.isEnabled = true
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.granularity = 1f
                axisLeft.setDrawGridLines(false)
                axisRight.isEnabled = false
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.loadDashboardData()
            }
        }

    }

    private fun observeViewModel() {
        viewModel.dashboardUiState.observe(viewLifecycleOwner) { state ->
            binding.swipeRefreshLayout.isRefreshing =
                state is DashboardViewModel.DashboardUiState.Loading

            when (state) {
                is DashboardViewModel.DashboardUiState.Loading -> {
                    binding.apply {
                        contentGroup.visibility = View.GONE
                    }
                }

                is DashboardViewModel.DashboardUiState.Success -> {
                    binding.apply {
                        contentGroup.visibility = View.VISIBLE
                    }

                    updateSummaryCards(state.summary)
                    updateCategoryPieChart(state.categories)
                    updateStockLevelBarChart(state.categories)
                }

                is DashboardViewModel.DashboardUiState.Error -> {
                    binding.apply {
                        contentGroup.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun updateSummaryCards(summary: DashboardSummary) {
        binding.apply {
            textTotalItems.text = summary.totalItems.toString()
            textOutOfStock.text = summary.outOfStockItems.toString()
            textLowStock.text = summary.lowStockItems.toString()
            textRecentActivity.text = summary.recentlyAddedItems.toString()
        }
    }

    private fun updateCategoryPieChart(categories: List<Category>) {
        val entries = mutableListOf<PieEntry>()
        categories.forEach { category ->
            entries.add(PieEntry(category.itemCount.toFloat(), category.name))
        }

        if (entries.isNotEmpty()) {

            val dataSet = PieDataSet(entries, "Categories").apply {
                setColors(*ColorTemplate.MATERIAL_COLORS)
                setDrawIcons(false)
                sliceSpace = 4f
                selectionShift = 5f
            }

            val data = PieData(dataSet).apply {
                setValueFormatter(PercentFormatter(pieChart))
                setValueTextSize(11f)
                setValueTextColor(Color.WHITE)
            }

            pieChart.data = data
            pieChart.highlightValues(null)
            pieChart.invalidate()
            pieChart.animateY(1000, Easing.EaseInOutQuad)

        }
    }

    private fun updateStockLevelBarChart(categories: List<Category>) {
        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()

        for ((index, category) in categories.withIndex()) {
            entries.add(BarEntry(index.toFloat(), category.itemCount.toFloat()))
            labels.add(category.name)
        }

        if (entries.isNotEmpty()) {

            val dataSet = BarDataSet(entries, "Categories")
            dataSet.setColors(*ColorTemplate.MATERIAL_COLORS)

            val data = BarData(dataSet)
            data.setValueTextSize(8f)
            data.barWidth = 0.9f

            barChart.data = data
            barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            barChart.xAxis.labelCount = labels.size
            barChart.setFitBars(true)
            barChart.invalidate()
            barChart.animateY(1000)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}