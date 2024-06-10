package com.jovan.artscape.ui.login.address

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jovan.artscape.R
import com.jovan.artscape.ViewModelFactory
import com.jovan.artscape.databinding.ActivityAddAddressBinding
import com.jovan.artscape.remote.request.AddUserRequest
import com.jovan.artscape.ui.login.interest.InterestActivity


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
        val autoCompleteRegency = binding.autoCompleteRegency
        val autoCompleteDistrict = binding.autoCompleteDistrict
        val autoCompleteVillage = binding.autoCompleteVillage

        viewModel.getProvinces().observe(this) { provinces ->
            val item = provinces.map { it.name }
            val adapterItems = ArrayAdapter(this, R.layout.list_item_address, item)
            autoCompleteProvince.setAdapter(adapterItems)

            autoCompleteProvince.setOnItemClickListener { adapterView, _, i, _ ->
                showLoading(true)
                val itemList = adapterView.getItemAtPosition(i).toString()
                Toast.makeText(this@AddAddressActivity, "Item: $itemList", Toast.LENGTH_SHORT).show()
                clearRegencyAdapter()
                viewModel.setRegencies(provinces[i].id)
                showLoading(false)
            }
        }
        viewModel.getRegencies().observe(this) { regency ->
            val item = regency.map { it.name }
            val adapterItems = ArrayAdapter(this, R.layout.list_item_address, item)
            autoCompleteRegency.setAdapter(adapterItems)
            autoCompleteRegency.setOnItemClickListener { adapterView, _, i, _ ->
                showLoading(true)

                val itemList = adapterView.getItemAtPosition(i).toString()
                Toast.makeText(this@AddAddressActivity, "Item: $itemList", Toast.LENGTH_SHORT)
                    .show()
                clearDistrictAdapter()
                viewModel.setDistricts(regency[i].id)
                showLoading(false)
            }
        }
        viewModel.getDistricts().observe(this) { district ->
            val item = district.map { it.name }
            val adapterItems = ArrayAdapter(this, R.layout.list_item_address, item)
            autoCompleteDistrict.setAdapter(adapterItems)
            autoCompleteDistrict.setOnItemClickListener { adapterView, _, i, _ ->
                showLoading(true)

                val itemList = adapterView.getItemAtPosition(i).toString()
                Toast.makeText(this@AddAddressActivity, "Item: $itemList", Toast.LENGTH_SHORT)
                    .show()
                clearVillageAdapter()
                viewModel.setVillages(district[i].id)
                showLoading(false)
            }
        }
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
        actionButton()
    }

    private fun clearRegencyAdapter() {
        val emptyAdapter = ArrayAdapter<String>(this, R.layout.list_item_address, listOf())
        val autoCompleteRegency = binding.autoCompleteRegency
        autoCompleteRegency.setAdapter(emptyAdapter)
        autoCompleteRegency.setText("")
        clearDistrictAdapter()
    }

    private fun clearDistrictAdapter() {
        val emptyAdapter = ArrayAdapter<String>(this, R.layout.list_item_address, listOf())
        val autoCompleteDistrict = binding.autoCompleteDistrict
        autoCompleteDistrict.setAdapter(emptyAdapter)
        autoCompleteDistrict.setText("")
        clearVillageAdapter()
    }

    private fun clearVillageAdapter() {
        val emptyAdapter = ArrayAdapter<String>(this, R.layout.list_item_address, listOf())
        val autoCompleteVillage = binding.autoCompleteVillage
        autoCompleteVillage.setAdapter(emptyAdapter)
        autoCompleteVillage.setText("")
    }

    private fun actionButton() {
        binding.apply {
            buttonAddress.setOnClickListener {
                val token = intent.getStringExtra(EXTRA_ID_TOKEN)
                val userData = AddUserRequest(
                    idToken = token.toString(),
                    name = intent.getStringExtra(EXTRA_NAME).toString(),
                    bio = intent.getStringExtra(EXTRA_BIO).toString(),
                    address = "${autoCompleteProvince.text}/${autoCompleteRegency.text}/${autoCompleteDistrict.text}/${autoCompleteVillage.text}",
                    interest = listOf(),
                    phoneNumber = intent.getStringExtra(EXTRA_PHONE_NUMBER).toString()
                )

                val intent = Intent(this@AddAddressActivity, InterestActivity::class.java)
                intent.putExtra(InterestActivity.EXTRA_USER_WITH_ADDRESS, userData)
                startActivity(intent)
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    companion object {
        const val EXTRA_ID_TOKEN = "extra_id_token"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_BIO = "extra_bio"
        const val EXTRA_PHONE_NUMBER = "extra_phone_number"
    }
}

