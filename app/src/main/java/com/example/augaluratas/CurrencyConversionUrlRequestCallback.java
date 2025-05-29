package com.example.augaluratas;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Currency;
import java.util.Locale;

public class CurrencyConversionUrlRequestCallback extends UrlRequest.Callback {

    private ByteBuffer buffer = ByteBuffer.allocateDirect(32 * 1024); //32kb
    ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
    Context context;
    String currency_code;

    public CurrencyConversionUrlRequestCallback(Context context, String currency_code){
        super();
        this.context = context;
        this.currency_code = currency_code;
    }

    @Override
    public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) throws Exception {
        request.followRedirect();
    }

    @Override
    public void onResponseStarted(UrlRequest request, UrlResponseInfo info) throws Exception {
        int httpStatusCode = info.getHttpStatusCode();
        if (httpStatusCode == 200) {
            // The request was fulfilled. Start reading the response.
            request.read(buffer);
        } else if (httpStatusCode == 503) {
            // The service is unavailable. You should still check if the request
            // contains some data.
            request.read(buffer);
        }
    }

    @Override
    public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) throws Exception {

        byteBuffer.flip(); // switch to read mode
        byte[] data = new byte[byteBuffer.remaining()];
        byteBuffer.get(data); // copy from buffer to byte[]
        responseStream.write(data, 0, data.length); // accumulate

        // Continue reading the response body by reusing the same buffer
        // until the response has been completed.
        byteBuffer.clear();
        request.read(buffer);
    }

    @Override
    public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
        // Convert collected bytes to String
        String responseJson = responseStream.toString();

        // You can now parse the JSON response
        try {
            JSONObject jsonObject = new JSONObject(responseJson);
            JSONObject rates = jsonObject.getJSONObject("conversion_rates");
            float rate = (float)rates.getDouble(currency_code);

            SharedPreferences sharedPref = context.getSharedPreferences("augalu_ratas.CURRENT_CURRENCY", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putFloat("current_conversion_rate", rate);
            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {
        Log.e("API", "The request failed.", error);
    }

    public double convertCurrency(double amountInEuro, double rate) {
        return amountInEuro * rate;
    }
}
