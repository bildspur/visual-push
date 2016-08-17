package ch.bildspur.visualpush.push;

import ch.bildspur.visualpush.Constants;

import javax.usb.*;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by cansik on 16/08/16.
 */
public class DisplayDriver {

    char[] header = new char[] {0xFF, 0xCC, 0xAA, 0x88,
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0x00};

    public void test() throws UsbException, UnsupportedEncodingException {
        final UsbServices services = UsbHostManager.getUsbServices();
        UsbDevice device = findDevice(services.getRootUsbHub(), Constants.ABLETON_VENDOR_ID, Constants.PUSH2_PRODUCT_ID);

        System.out.println(device.getSerialNumberString());
    }

    public UsbDevice findDevice(UsbHub hub, short vendorId, short productId)
    {
        for (UsbDevice device : (List<UsbDevice>) hub.getAttachedUsbDevices())
        {
            UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
            if (desc.idVendor() == vendorId && desc.idProduct() == productId) return device;
            if (device.isUsbHub())
            {
                device = findDevice((UsbHub) device, vendorId, productId);
                if (device != null) return device;
            }
        }
        return null;
    }
}
