package army.org.dmbtimer.fragments.add

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
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
    private var userPhotoPath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_photo, container, false)
    }

    private fun onPhotoChoose(file: String) {
        listener?.onPhotoChoose(file)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPhotoPath = arguments?.getString(ARG_PHOTO)
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
        if (userPhotoPath != null)
            initPhoto()

        userPhoto.setOnClickListener {
            Dexter.withActivity(activity)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        val intent = Intent()
                        intent.type = "image/*"
                        intent.action = Intent.ACTION_PICK
                        startActivityForResult(Intent.createChooser(intent, "Select photo"),
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
            val cursor = activity?.contentResolver?.query(selectedImage!!, null, null, null, null)
            cursor?.moveToFirst()
            userPhotoPath = cursor?.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            cursor?.close()

            initPhoto()
            onPhotoChoose(userPhotoPath!!)
        }
    }

    private fun initPhoto() {
        userPhoto.borderColor = Color.WHITE
        userPhoto.borderWidth = 10
        userPhoto.setImageBitmap(BitmapFactory.decodeFile(userPhotoPath))
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnUserChoosePhotoListener {
        fun onPhotoChoose(file: String)
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
