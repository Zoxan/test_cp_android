package com.example.unity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.cloudpayments.sdk.configuration.CloudpaymentsSDK
import ru.cloudpayments.sdk.configuration.PaymentConfiguration
import ru.cloudpayments.sdk.configuration.PaymentData


class CloudPaymentsActivity : AppCompatActivity() {

    companion object {
        lateinit var publicId: String;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("xxx", "===onCreate Kotlin 1")
        val cpSdkLauncher = CloudpaymentsSDK.getInstance().launcher(this, result = {
            if (it.status != null) {
                if (it.status == CloudpaymentsSDK.TransactionStatus.Succeeded) {
                    Log.d("xxx", "===Успешно! Транзакция №${it.transactionId}")
                } else {
                    if (it.reasonCode != 0) {
                        Log.d("xxx", "===Ошибка! Транзакция №${it.transactionId}. Код ошибки ${it.reasonCode}")
                    } else {
                        Log.d("xxx", "===Ошибка! Транзакция №${it.transactionId}")
                    }
                }
            }
        })

        Log.d("xxx", "===onCreate Kotlin 2")

        val jsonData = HashMap<String, Any>()
//        jsonData["asdf"] = "adf";

        Log.d("xxx", "===onCreate Kotlin 3")

        val paymentData = PaymentData(
            publicId, // PublicId который вы получили в CloudPayments
            "10.00", // Сумма транзакции
            currency = "RUB", // Валюта
            jsonData = jsonData, // Данные в формате Json
        )

        Log.d("xxx", "===onCreate Kotlin 4")

        val configuration = PaymentConfiguration(
            paymentData, // Данные транзакции
            null, // Сканер банковских карт
            useDualMessagePayment = false, // Использовать двухстадийную схему проведения платежа, по умолчанию используется одностадийная схема
            disableGPay = false, // Выключить Google Pay, по умолчанию Google Pay включен
            disableYandexPay = false, // Выключить Yandex Pay, по умолчанию Yandex Pay включен
            yandexPayMerchantID = "asdf" // Yandex Pay Merchant id
        )

        Log.d("xxx", "===onCreate Kotlin 5")
        cpSdkLauncher.launch(configuration)
//        CloudpaymentsSDK.getInstance().start(configuration, this, 777)

        Log.d("xxx", "===onCreate Kotlin 6")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) = when (requestCode) {
        777 -> {
            val transactionId = data?.getIntExtra(CloudpaymentsSDK.IntentKeys.TransactionId.name, 0) ?: 0
            val transactionStatus = data?.getSerializableExtra(CloudpaymentsSDK.IntentKeys.TransactionStatus.name) as? CloudpaymentsSDK.TransactionStatus


            if (transactionStatus == null) {
            } else {
                if (transactionStatus == CloudpaymentsSDK.TransactionStatus.Succeeded) {
                    Toast.makeText(this, "Успешно! Транзакция №$transactionId", Toast.LENGTH_SHORT).show()
                } else {
                    val reasonCode = data.getIntExtra(CloudpaymentsSDK.IntentKeys.TransactionReasonCode.name, 0) ?: 0
                    if (reasonCode > 0) {
                        Toast.makeText(this, "Ошибка! Транзакция №$transactionId. Код ошибки $reasonCode", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Ошибка! Транзакция №$transactionId.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        else -> super.onActivityResult(requestCode, resultCode, data)
    }
}

fun debug2(msg : String, msg2 : String) {
    Log.d("xxx", "===debug from Kotlin: $msg, +++ $msg2")
}

fun CreateCPActivity(publicId: String, fromActivity: Activity) {
    Log.d("xxx", "===CreateCPActivity from Kotlin 1")
    val myIntent = Intent(fromActivity, CloudPaymentsActivity::class.java)
    fromActivity.startActivity(myIntent)
    CloudPaymentsActivity.publicId = publicId;
    Log.d("xxx", "===CreateCPActivity from Kotlin 2")
}