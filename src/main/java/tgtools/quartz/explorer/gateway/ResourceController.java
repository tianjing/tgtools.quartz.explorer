package tgtools.quartz.explorer.gateway;

import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import tgtools.util.FileUtil;
import tgtools.util.ReflectionUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 12:33
 */
@RequestMapping("/quartz/explorer/manage")
public class ResourceController {

    @RequestMapping(value = "/resource/**")
    public void get(HttpServletRequest pRequest, HttpServletResponse pResponse) {
        setContentType(pRequest, pResponse);
        try {
            copyAndClose(ReflectionUtil.getResourceAsStream(getResourcePath(pRequest, pResponse)), pResponse.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String getResourcePath(HttpServletRequest pRequest, HttpServletResponse pResponse) {
        String url = pRequest.getRequestURI();
        String file = url.substring(url.indexOf("manage"));
        int end = file.indexOf("?");
        if (end >= 0) {
            file = file.substring(0, file.indexOf("?"));
        }
        return "tgtools/quartz/explorer/" + file;
    }

    protected void setContentType(HttpServletRequest pRequest, HttpServletResponse pResponse) {
        String vUrl = pRequest.getRequestURI();
        String vExtName = FileUtil.getExtensionName(vUrl);
        if ("js".equals(vExtName)) {
            pResponse.setContentType("application/x-javascript");
        } else if ("css".equals(vExtName)) {
            pResponse.setContentType("text/css");
        } else if ("html".equals(vExtName)) {
            pResponse.setContentType(MimeTypeUtils.TEXT_HTML_VALUE);
        } else {
            pResponse.setContentType(MimeTypeUtils.TEXT_PLAIN_VALUE);
        }

    }

    protected void copyAndClose(InputStream pInputStream, OutputStream pOutputStream) {
        if (null == pOutputStream) {
            closeStream(pInputStream);
            return;
        }

        if (null == pInputStream && null != pOutputStream) {
            closeStream(pOutputStream);
            return;
        }

        try {
            int len = 0;
            byte[] data = new byte[1024];
            while ((len = pInputStream.read(data)) > 0) {
                pOutputStream.write(data, 0, len);
            }

        } catch (Exception e) {

        } finally {
            closeStream(pInputStream);
            closeStream(pOutputStream);
        }
    }

    private void closeStream(Closeable pCloseable) {
        if (null != pCloseable) {
            try {
                pCloseable.close();
            } catch (IOException e) {
            }
        }
    }
}
