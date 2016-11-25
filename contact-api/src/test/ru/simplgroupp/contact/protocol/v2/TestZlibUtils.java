package ru.simplgroupp.contcact.protocol.v2;

import junit.framework.Assert;
import org.apache.commons.net.util.Base64;
import org.junit.Test;
import ru.simplgroupp.contact.util.ZlibUtils;

/**
 * Created by aro on 13.09.2014.
 */
public class TestZlibUtils {

    @Test
    public void testCompress() throws Exception{
        String text = "<?xml version=\"1.0\" encoding=\"US-ASCII\"?><REQUEST action=\"GET_CHANGES\" obj_class=\"TAbonentObjectAssembler\" pack=\"ZLIB\" part=\"0\" point_code=\"AFRM\" portion=\"0\"/>";
        System.out.print("Текст для сжатия" + text);
        byte packed[] = ZlibUtils.compress(text.getBytes());
        String pc = Base64.encodeBase64String(packed);

        byte[] dc = Base64.decodeBase64(pc);

        byte[] unpaked = ZlibUtils.decompress(dc);

        Assert.assertEquals(text, new String(unpaked));

    }
}
