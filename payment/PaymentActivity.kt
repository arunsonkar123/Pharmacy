package com.example.pharmacyapp.payment

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class PaymentActivity : Activity(), PaymentResultListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val amount = intent.getDoubleExtra("amount", 0.0)
        startPayment(amount)
    }

    private fun startPayment(amount: Double) {
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_Sce086IvO5Xfas")

        try {
            val options = JSONObject()
            options.put("name", "Pharmacy App")
            options.put("description", "Medicine Order")
            options.put("currency", "INR")
            options.put("amount", (amount * 100).toInt())

            val prefill = JSONObject()
            prefill.put("email", "test@razorpay.com")
            prefill.put("contact", "9999999999")
            options.put("prefill", prefill)

            checkout.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onPaymentSuccess(paymentId: String?) {
        Toast.makeText(this, "Payment Success", Toast.LENGTH_LONG).show()
        finish()
    }

    override fun onPaymentError(code: Int, response: String?) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_LONG).show()
        finish()
    }
}