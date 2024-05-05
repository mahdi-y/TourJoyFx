package Entities;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {

    public static byte[] generateQRCodeImage(Subscription subscription, int width, int height) throws WriterException, IOException {
        String subscriptionDetails = "Subscription Details:" + "\n"+ "ID: " +subscription.getId() + "\n" +
                "Plan: "+subscription.getPlan() + "\n" +
                "Duration: "+subscription.getDuration() + "\n" +
                "Type of transport: "+subscription.getTypeT().getType_t();

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(subscriptionDetails, BarcodeFormat.QR_CODE, width, height, hints);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        return outputStream.toByteArray();
    }
}
