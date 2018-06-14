package com.izettle.wrench.oss;

import android.content.Context;
import android.content.res.Resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class OssLoading {
    public static ArrayList<LicenceMetadata> getThirdPartyLicenceMetadata(Context context) {
        String thirdPartyLicenseMetadata = resourcePartToString(context.getApplicationContext(), "third_party_license_metadata", 0L, -1);
        String[] thirdPartyLicences = thirdPartyLicenseMetadata.split("\n");
        ArrayList<LicenceMetadata> licenceMetadataArrayList = new ArrayList<>(thirdPartyLicences.length);

        for (String licenceString : thirdPartyLicences) {
            int indexOfSpace = licenceString.indexOf(' ');
            String[] byteInformation = licenceString.substring(0, indexOfSpace).split(":");
            boolean validByteInformation = byteInformation.length == 2 && indexOfSpace > 0;

            if (!validByteInformation) {
                if (licenceString.length() != 0) {
                    throw new IllegalStateException("Invalid license meta-data line:\n" + licenceString);
                } else {
                    throw new IllegalStateException("Invalid license meta-data line:\n");
                }
            }

            long byteStart = Long.parseLong(byteInformation[0]);
            int byteEnd = Integer.parseInt(byteInformation[1]);
            licenceMetadataArrayList.add(new LicenceMetadata(licenceString.substring(indexOfSpace + 1), byteStart, byteEnd));
        }

        return licenceMetadataArrayList;
    }

    public static String getThirdPartyLicence(Context context, LicenceMetadata licenceMetadata) {
        long skipBytes = licenceMetadata.getSkipBytes();
        int length = licenceMetadata.getLength();
        return resourcePartToString(context, "third_party_licenses", skipBytes, length);
    }

    private static String resourcePartToString(Context context, String resourceName, long skipBytes, int length) {
        Resources resources = context.getResources();
        return inputStreamPartToString(resources.openRawResource(resources.getIdentifier(resourceName, "raw", context.getPackageName())), skipBytes, length);
    }

    private static String inputStreamPartToString(InputStream inputStream, long skipBytes, int length) {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            inputStream.skip(skipBytes);
            int var10000 = length > 0 ? length : Integer.MAX_VALUE;

            while (true) {
                int var6 = var10000;
                int bytesRead;
                if (var10000 <= 0 || (bytesRead = inputStream.read(buffer, 0, Math.min(var6, 1024))) == -1) {
                    inputStream.close();
                    break;
                }

                byteArrayOutputStream.write(buffer, 0, bytesRead);
                var10000 = var6 - bytesRead;
            }
        } catch (IOException exception) {
            throw new RuntimeException("Failed to read license or metadata text.", exception);
        }

        try {
            return byteArrayOutputStream.toString("UTF-8");
        } catch (UnsupportedEncodingException exception) {
            throw new RuntimeException("Unsupported encoding UTF8. This should always be supported.", exception);
        }
    }

}
