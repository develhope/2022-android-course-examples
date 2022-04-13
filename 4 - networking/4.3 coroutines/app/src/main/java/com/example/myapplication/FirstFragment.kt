package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.FragmentFirstBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            val tempValue1 = provideFirstValue()
            val tempValue2 = provideSecondValue()
            printValue(tempValue1, tempValue2)
        }
    }

    suspend fun provideFirstValue(): Int {
        delay(1000)
        return 2
    }

    suspend fun provideSecondValue(): Int {
        delay(2000)
        return 5
    }

    fun printValue(value1: Int, value2: Int) {
        Log.v("MainActivity", "value1: $value1, value2: $value2")
        binding.textviewFirst.text = "value is: ${value1 + value2}"
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                Toast.makeText(requireContext(), "Settings menu item chosen!", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_new -> {
                Toast.makeText(requireContext(), "I want to create a new profile!", Toast.LENGTH_SHORT).show()
                true
            }
            else -> {
                Toast.makeText(requireContext(), "To be implemented!", Toast.LENGTH_SHORT).show()
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}