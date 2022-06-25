package es.marcmauri.kliklet.features.commercesviewer.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import es.marcmauri.kliklet.R
import es.marcmauri.kliklet.app.KlikletApp
import es.marcmauri.kliklet.databinding.FragmentCommercesViewerDetailBinding
import es.marcmauri.kliklet.features.commercesviewer.CommercesViewerDetailMVP
import es.marcmauri.kliklet.features.commercesviewer.model.entities.Commerce
import es.marcmauri.kliklet.features.commercesviewer.view.activity.CommercesViewerActivity
import es.marcmauri.kliklet.utils.Constants
import es.marcmauri.kliklet.utils.asSentence
import es.marcmauri.kliklet.utils.snackBar
import javax.inject.Inject

private const val EXTRA_PARAM_COMMERCE = "extra_param_commerce"

class CommercesViewerDetailFragment : Fragment(), CommercesViewerDetailMVP.View {

    companion object {
        @JvmStatic
        fun newInstance(selectedCommerce: Commerce) =
            CommercesViewerDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_PARAM_COMMERCE, selectedCommerce)
                }
            }
    }

    @Inject
    lateinit var presenter: CommercesViewerDetailMVP.Presenter
    private lateinit var binding: FragmentCommercesViewerDetailBinding
    private var currentCommerce: Commerce? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as KlikletApp).getComponent().inject(this)

        arguments?.let {
            currentCommerce = it.getParcelable<Commerce>(EXTRA_PARAM_COMMERCE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCommercesViewerDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setView(this)
        presenter.onFragmentReady()
    }

    override fun configureUI() {
        binding.tvBringMeThereTitle.setOnClickListener {
            presenter.onBringMeThereButtonClick()
        }
    }

    override fun getCommerceFromExtras(): Commerce? = currentCommerce

    override fun showCommerceDetails(commerce: Commerce) {
        // Populate toolbar title
        (activity as CommercesViewerActivity).putCommerceNameToToolbar(commerce.name!!)

        // Populate images
        Glide.with(requireContext())
            .load(R.drawable.ic_only_image)
            .centerCrop()
            .placeholder(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_placeholder_fullimage
                )
            )
            .into(binding.ivCommerceDetailImage)

        Glide.with(requireContext())
            .load(commerce.logo?.thumbnails?.small)
            .centerInside()
            .placeholder(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_placeholder
                )
            )
            .into(binding.ivCommerceThumb)

        // Populate texts
        binding.tvMapDetailTitle.text = commerce.name

        val commerceAddressText =
            if (commerce.address?.street == null || commerce.address.city == null) {
                Constants.Literals.UNDEFINED
            } else {
                """
                    ${commerce.address.street.asSentence()}
                    ${commerce.address.city.asSentence()}
                """.trimIndent()
            }
        binding.tvMapDetailSubtitle.text = commerceAddressText

        val latLongText = "${commerce.latitude}, ${commerce.longitude}"
        binding.tvMapDetailCoords.text = latLongText
        binding.tvCommerceDetailedInfo.text = commerce.description

        // Add the marker on the map
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync { googleMap ->
            if (commerce.latitude != null && commerce.longitude != null) {
                val currentLocation = LatLng(commerce.latitude, commerce.longitude)
                googleMap.addMarker(MarkerOptions().position(currentLocation).title(commerce.name))
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
            } else {
                showError("This commerce does not have its location record")
            }
        }
    }

    override fun goToGoogleMaps(uri: Uri) {
        val mapIntent = Intent(Intent.ACTION_VIEW, uri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(requireActivity().packageManager)?.let {
            startActivity(mapIntent)
        } ?: run {
            showError("Google Maps was not found!")
        }
    }

    override fun goToCommerceList() {
        activity?.onBackPressed()
    }

    override fun showLoading() {
        binding.progressBarLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.progressBarLoading.visibility = View.GONE
    }

    override fun showError(message: String) {
        snackBar(
            message = message,
            view = binding.rootView
        )
    }


}