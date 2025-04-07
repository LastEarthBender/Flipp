package com.example.flipp.presentation.reports

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.flipp.R
import com.example.flipp.databinding.FragmentReportsBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class ReportFragment : Fragment() {

    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!
    private lateinit var pieChart: PieChart
    private val viewModel: ReportViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        viewModel.loadReportData()
    }

    private fun observeViewModel() {
        viewModel.reportUiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ReportViewModel.ReportUiState.Loading -> {}
                is ReportViewModel.ReportUiState.Success -> {
                    setupQuantityTrendsChart(state.quantityTrends)
                    setupTopCategoriesChart(state.topCategories)
                    setupLowStockWarnings(state.lowStockItems)
                }
                is ReportViewModel.ReportUiState.Error -> {}
            }
        }
    }

    private fun setupQuantityTrendsChart(quantityTrends: List<Pair<Date, Int>>) {
        val entries = mutableListOf<Entry>()
        val dateFormat = java.text.SimpleDateFormat("MMM dd", java.util.Locale.getDefault())

        quantityTrends.forEachIndexed { index, (date, quantity) ->
            entries.add(Entry(index.toFloat(), quantity.toFloat()))
        }

        val dataSet = LineDataSet(entries, "Quantity").apply {
            color = Color.RED
            valueTextColor = resources.getColor(R.color.md_theme_secondary_highContrast)
            valueTextSize = 12f
            setDrawCircles(false)
        }

        val lineData = LineData(dataSet)
        binding.quantityTrendsChart.apply {
            data = lineData
            description.isEnabled = false
            xAxis.labelRotationAngle = -45f
            xAxis.valueFormatter = object : IndexAxisValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val index = value.toInt()
                    return if (index >= 0 && index < quantityTrends.size) {
                        dateFormat.format(quantityTrends[index].first)
                    } else {
                        ""
                    }
                }
            }
           invalidate()
        }
    }

    private fun setupTopCategoriesChart(topCategories: List<Pair<String, Int>>) {
        binding.apply {
            pieChart = topCategoriesChart
            pieChart.apply {
                description.isEnabled = false
                setUsePercentValues(true)
                setEntryLabelTextSize(12f)
                setEntryLabelColor(Color.WHITE)
                centerText = "Categories"
                setCenterTextSize(16f)
                legend.isEnabled = true
            }

            val entries = mutableListOf<PieEntry>()
            topCategories.forEach { (category, count) ->
                entries.add(PieEntry(count.toFloat(), category))
            }
            if (entries.isNotEmpty()) {

                val dataSet = PieDataSet(entries, "Categories").apply {
                    setColors(*ColorTemplate.MATERIAL_COLORS)
                    setDrawIcons(false)
                    sliceSpace = 4f
                    selectionShift = 5f
                }

                val data = PieData(dataSet).apply {
                    setValueFormatter(PercentFormatter(topCategoriesChart))
                    setValueTextSize(11f)
                    setValueTextColor(Color.WHITE)
                }

                pieChart.data = data
                pieChart.highlightValues(null)
                pieChart.invalidate()
                pieChart.animateY(1000, Easing.EaseInOutQuad)
            }
        }
    }

    private fun setupLowStockWarnings(lowStockItems: List<String>) {
        if (lowStockItems.isEmpty()) {
            binding.lowStockWarningsTextView.text = "No low stock warnings."
        } else {
            binding.lowStockWarningsTextView.text = lowStockItems.joinToString("\n")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
