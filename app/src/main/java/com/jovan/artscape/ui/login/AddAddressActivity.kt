package com.jovan.artscape.ui.login

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jovan.artscape.R
import com.jovan.artscape.ViewModelFactory
import com.jovan.artscape.databinding.ActivityAddAddressBinding


class AddAddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAddressBinding
    private val viewModel by viewModels<AddressViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val autoCompleteProvince = binding.autoCompleteProvince
        viewModel.getProvinces().observe(this) { provinces ->
            val item = provinces.map { it.name }
            val adapterItems = ArrayAdapter(this, R.layout.list_item_address, item)
            autoCompleteProvince.setAdapter(adapterItems)

            autoCompleteProvince.setOnItemClickListener { adapterView, _, i, _ ->
                val item = adapterView.getItemAtPosition(i).toString()
                Toast.makeText(this@AddAddressActivity, "Item: $item", Toast.LENGTH_SHORT).show()
                viewModel.setRegencies(provinces[i].id.toString())
            }
        }

        val autoCompleteRegency = binding.autoCompleteRegency
        viewModel.getRegencies().observe(this) { regency ->
            val item = regency.map { it.name }
            val adapterItems = ArrayAdapter(this, R.layout.list_item_address, item)
            autoCompleteRegency.setAdapter(adapterItems)
            autoCompleteRegency.setOnItemClickListener { adapterView, _, i, _ ->

                val itemList = adapterView.getItemAtPosition(i).toString()
                Toast.makeText(this@AddAddressActivity, "Item: $itemList", Toast.LENGTH_SHORT)
                    .show()
                viewModel.setDistricts(regency[i].id.toString())
            }
        }

        val autoCompleteDistrict = binding.autoCompleteDistrict
        viewModel.getDistricts().observe(this) { district ->
            val item = district.map { it.name }
            val adapterItems = ArrayAdapter(this, R.layout.list_item_address, item)
            autoCompleteDistrict.setAdapter(adapterItems)
            autoCompleteDistrict.setOnItemClickListener { adapterView, _, i, _ ->

                val itemList = adapterView.getItemAtPosition(i).toString()
                Toast.makeText(this@AddAddressActivity, "Item: $itemList", Toast.LENGTH_SHORT)
                    .show()
                viewModel.setVillages(district[i].id.toString())
            }
        }

        val autoCompleteVillage = binding.autoCompleteVillage
        viewModel.getVillages().observe(this) { village ->
            val item = village.map { it.name }
            val adapterItems = ArrayAdapter(this, R.layout.list_item_address, item)
            autoCompleteVillage.setAdapter(adapterItems)
            autoCompleteVillage.setOnItemClickListener { adapterView, _, i, _ ->

                val itemList = adapterView.getItemAtPosition(i).toString()
                Toast.makeText(this@AddAddressActivity, "Item: $itemList", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}