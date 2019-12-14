package army.org.dmbtimer.fragments.add.soldier

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import army.org.dmbtimer.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.fragment_user_photo.*

class UserPhotoFragment : Fragment() {
    private var listener: OnUserChoosePhotoListener? = null
    private var userPhotoUri: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_photo, container, false)
    }

    private fun onPhotoChoose(uri: String) {
        listener?.onPhotoChoose(uri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPhotoUri = arguments?.getString(ARG_PHOTO)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnUserChoosePhotoListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnUserChoosePhotoListener")
        }
    }

    override fun onStart() {
        super.onStart()
        userPhotoUri?.let {
            userPhoto.setImageURI(Uri.parse(userPhotoUri))
        }

        userPhoto.setOnClickListener {
            Dexter.withActivity(activity)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        val intent = Intent()
                        intent.type = "image/*"
                        intent.action = Intent.ACTION_GET_CONTENT
                        startActivityForResult(
                            Intent.createChooser(intent, "Выбрать фото"),
                            USER_PHOTO_CODE
                        )
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest?,
                        token: PermissionToken?
                    ) {
                        token?.continuePermissionRequest()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                        Toast.makeText(
                            context,
                            "Чтобы добавить фотографию необходимо разрешить доступ к внешней памяти",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }).onSameThread()
                .check()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == USER_PHOTO_CODE && data != null) {
            val selectedImage = data.data

            selectedImage?.let {
                userPhoto.setImageURI(it)

                onPhotoChoose(it.toString())
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnUserChoosePhotoListener {
        fun onPhotoChoose(uri: String)
    }

    companion object {
        private const val USER_PHOTO_CODE = 1
        private const val ARG_PHOTO = "user_photo"

        @JvmStatic
        fun getInstance(file: String?): UserPhotoFragment {

            val args = Bundle()
            args.putString(ARG_PHOTO, file)
            val fragment = UserPhotoFragment()
            fragment.arguments = args
            return fragment
        }
    }

}
