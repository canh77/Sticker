package com.lutech.stickerwhatsapp.activity

import android.Manifest
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.lutech.stickerwhatsapp.R
import com.lutech.stickerwhatsapp.utils.Utils
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.dialog_confirm_logout.*
import kotlinx.android.synthetic.main.dialog_info_sticker.*
import kotlinx.android.synthetic.main.dialog_login_facebook_gg.*
import kotlinx.android.synthetic.main.dialog_login_facebook_gg.btnClose
import kotlinx.android.synthetic.main.dialog_login_facebook_gg.tvServiceAndPrivacy
import kotlinx.android.synthetic.main.dialog_register_facebook_gg.*
import java.util.*


class SettingActivity : AppCompatActivity() {
    private var mDialogLogin: Dialog? = null
    private var mDialogRegister: Dialog? = null
    private var mDialogLogout: Dialog? = null
    private lateinit var mAuth: FirebaseAuth
    var callbackManager: CallbackManager? = null
    var loginManager: LoginManager? = null
    private val RC_SIGN_IN = 123
    private lateinit var googleSignInClient: GoogleSignInClient
    private var progressDialog: ProgressDialog? = null
    private val CAMERA_REQUEST_CODE = 200
    private val STORAGE_REQUEST_CODE = 400
    private val IMAGE_PICK_GALLERY_CODE = 1000
    private val IMAGE_PICK_CAMERA_CODE = 1001
    lateinit var cameraPermission: Array<String>
    lateinit var storagePermission: Array<String>
    var image_uri: Uri? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initData()
        handlerEvents()
    }


    private fun initData() {
        cameraPermission =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        storagePermission = arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
        progressDialog = ProgressDialog(this)
        val gson = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gson)
        mAuth = FirebaseAuth.getInstance()
        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun handlerEvents() {
        btnBackSettings.setOnClickListener {
            finish()
        }
        lnLoginRegistration.setOnClickListener {
            dialogLogin()
        }

        btnLogOut.setOnClickListener {
            dialogConfirmLogOut()
        }

    }


    override fun onStart() {
        super.onStart()
        checkUserHasLogin()
    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            Log.d("1211155454", "onActivityResult: task " + task)
            try {
                progressDialog?.show()
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!.idToken!!)
                mDialogLogin?.dismiss()

                Log.d("1211155454", "onActivityResult: account!!.idToken!! " + account!!.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
            }
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                CropImage.activity(data!!.data).setGuidelines(CropImageView.Guidelines.ON)
                mDialogRegister?.ivAvatarRegister?.setImageURI(data!!.data)
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                CropImage.activity(image_uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this)
            }
        }
        //get corpped image
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri // get image uri
                mDialogRegister?.ivAvatarRegister?.setImageURI(resultUri)
            }
        }
    }

    private fun checkUserHasLogin() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            setViewNotLogin()
        } else {
            setViewLogin()
        }
    }

    private fun setViewNotLogin() {
        btnLogOut.visibility = View.GONE
        ivAvatar.setImageResource(R.drawable.ic_avatar)
        tvNameEmail.text = getString(R.string.txt_login_or_register_a_new_account_to)
        tvIdEmail.text =
            getString(R.string.txt_keep_your_sticker_packs_safe_and_access_more_features)
        tvLoginRegister.text = getString(R.string.txt_login_amp_registration)
    }

    private fun setViewLogin() {
        btnLogOut.visibility = View.VISIBLE
        Glide.with(this).load(Objects.requireNonNull(mAuth.currentUser?.photoUrl)).into(ivAvatar)
        tvNameEmail.text = mAuth.currentUser?.displayName
        tvIdEmail.text = mAuth.currentUser?.email
        tvLoginRegister.text = getString(R.string.txt_login_as)
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    progressDialog?.dismiss()
                    btnLogOut.visibility = View.VISIBLE
                    Glide.with(this).load(Objects.requireNonNull(mAuth.currentUser?.photoUrl))
                        .into(ivAvatar)
                    tvNameEmail.text = mAuth.currentUser?.displayName
                    tvIdEmail.text = mAuth.currentUser?.email
                    tvLoginRegister.text = getString(R.string.txt_login_as)
                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun dialogConfirmLogOut() {
        mDialogLogout = Utils.onCreateDialog(this, R.layout.dialog_confirm_logout, true)
        mDialogLogout?.btnNo?.setOnClickListener {
            mDialogLogout?.dismiss()
        }
        mDialogLogout?.btnYes?.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            setViewNotLogin()
            mDialogLogout?.dismiss()
        }
        mDialogLogout?.show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun dialogLogin() {
        mDialogLogin = Utils.onCreateDialog(this, R.layout.dialog_login_facebook_gg, true)
        mDialogLogin?.btnClose?.setOnClickListener {
            mDialogLogin?.dismiss()
        }

        mDialogLogin?.btnRegisterEmail?.setOnClickListener {
            dialogRegister()
        }

        mDialogLogin?.btnSignFaceBook?.setOnClickListener {
            faceBookLogin();
        }

        mDialogLogin?.btnSignGoogle?.setOnClickListener {
            googleSignInClient.signOut().addOnCompleteListener(this) {
                signInGoogle()
            }
        }

        val html =
            "<a href='https://storage.lutech.vn/app/sticker/meme_pack1/sticker_11.png'><b>Terms of Service</b></a> " +
                    "and <a href='https://storage.lutech.vn/app/sticker/meme_pack1/sticker_11.png'><b>Privacy Policy</b></a>."
        val styledText = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)

        mDialogLogin?.tvServiceAndPrivacy?.text = styledText
        mDialogLogin?.tvServiceAndPrivacy?.movementMethod =
            LinkMovementMethod.getInstance()
        mDialogLogin?.show()
    }

    private fun faceBookLogin() {
        loginManager = LoginManager.getInstance()
        loginManager?.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    val request = GraphRequest.newMeRequest(
                        loginResult.accessToken
                    ) { `object`, response ->
                        if (`object` != null) {
                            try {
                                val logger = AppEventsLogger.newLogger(this@SettingActivity)
                                logger.logEvent("Facebook login suceess")
                                handleFacebookAccessToken(loginResult.accessToken)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                    val parameters = Bundle()
                    parameters.putString("fields", "id,name,email,gender, birthday, about")
                    request.parameters = parameters
                    request.executeAsync()
                }
            }

            override fun onCancel() {
            }

            override fun onError(error: FacebookException) {
            }
        })
        loginManager?.logInWithReadPermissions(this@SettingActivity,
            listOf("email", "public_profile"))
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, object : OnCompleteListener<AuthResult?> {
                override fun onComplete(@NonNull task: Task<AuthResult?>) {
                    if (task.isSuccessful()) {
                        val user: FirebaseUser = mAuth.getCurrentUser()!!
                        if (user != null) {

                        }
                    } else {


                    }
                }
            })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun dialogRegister() {
        mDialogRegister =
            Utils.onCreateDialog(this, R.layout.dialog_register_facebook_gg, true)
        mDialogRegister?.btnClose?.setOnClickListener {
            mDialogRegister?.dismiss()
        }
        mDialogRegister?.tvBirthday?.setOnClickListener {
            showDatePickerDialog()
        }

        mDialogRegister?.ivAvatarRegister?.setOnClickListener {
            showOpenLibraryOrCamera()
        }

        mDialogRegister?.btnRegister?.setOnClickListener {
            progressDialog?.show()
            register()
        }



        mDialogRegister?.edtUsername?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val charCount = s?.length ?: 0
                mDialogRegister?.tvNumberCharacter?.text = "$charCount/30"
                if (charCount >= 30) {
                    mDialogRegister?.tvNumberCharacter?.setTextColor(Color.RED)
                    mDialogRegister?.edtUsername?.setBackgroundResource(R.drawable.bg_edt_max_character)
                } else {
                    mDialogRegister?.tvNumberCharacter?.setTextColor(Color.BLACK)
                    mDialogRegister?.edtUsername?.setBackgroundResource(R.drawable.bg_edt_email)
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        val html =
            "<a href='https://storage.lutech.vn/app/sticker/meme_pack1/sticker_11.png'><b>Terms of Service</b></a> " +
                    "and <a href='https://storage.lutech.vn/app/sticker/meme_pack1/sticker_11.png'><b>Privacy Policy</b></a>."
        val styledText = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)

        mDialogRegister?.tvServiceAndPrivacy?.text = styledText
        mDialogRegister?.tvServiceAndPrivacy?.movementMethod =
            LinkMovementMethod.getInstance()
        mDialogRegister?.show()
    }


    private fun register() {
        var userName: String = mDialogRegister?.edtUsername?.text.toString().trim()
        var birthday: String = mDialogRegister?.tvBirthday?.text.toString().trim()
        var email: String = mDialogRegister?.edtEmailRegister?.text.toString().trim()
        var password: String = mDialogRegister?.edtPasswordRegister?.toString()!!.trim()

        if (email == "" && password == "") {
            Toast.makeText(this, "Vui lòng nhập đầy đủ email và password", Toast.LENGTH_SHORT)
                .show()
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        progressDialog?.dismiss()
                        Toast.makeText(baseContext, "Register Authentication success.", Toast.LENGTH_SHORT).show()
                        mDialogLogin?.dismiss()
                        mDialogRegister?.dismiss()
                    } else {
                        Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT)
                            .show()

                    }
                }
        }
    }


    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            DatePickerDialog.OnDateSetListener { view: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
                // Xử lý ngày bạn đã chọn ở đây
                val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                // Lưu giá trị vào trường birthday
                mDialogRegister?.tvBirthday?.text = selectedDate
            },
            year,
            month,
            dayOfMonth)

        // Hiển thị dialog
        datePickerDialog.show()
    }


    //open gallery and open camera
    private fun showOpenLibraryOrCamera() {
        val items = arrayOf("Camera", "Gallary")
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Select Image")
        dialog.setItems(items) { dialog, which ->
            if (which == 0) {
                if (!checkCameraPermission()) {
                    requestPermissionCamera()
                } else {
                    pickCamera()
                }
            }
            if (which == 1) {
                if (!checkStoragePermission()) {
                   requestPermissionStorage()
                } else {
                    pickGallery()
                }
            }
        }
        dialog.create().show()
    }

    private fun pickGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE)
    }

    private fun pickCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "NewPic") //title of the picture
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image to text ") //description
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE)
    }

    private fun requestPermissionStorage() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                getPermission13AndDefault(),//13
                Manifest.permission.CAMERA,
            ),STORAGE_REQUEST_CODE
        )
    }
    private fun requestPermissionCamera() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.CAMERA,
            ),CAMERA_REQUEST_CODE
        )
    }


    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, getPermission13AndDefault()) != PackageManager.PERMISSION_GRANTED
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED

    }


    private fun getPermission13AndDefault(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        }
    }

    //handle permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> if (grantResults.isNotEmpty()) {
                val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (cameraAccepted && writeStorageAccepted) {
                    pickCamera()
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            STORAGE_REQUEST_CODE -> if (grantResults.isNotEmpty()) {
                val writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (writeStorageAccepted) {
                    pickGallery()
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}