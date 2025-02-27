package com.masiis.shop.api.controller.base;

import com.masiis.shop.common.util.AESUtils;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 基础controller,用来编写一些基础方法
 * <p/>
 * Created by lzh on 2016/2/27.
 */
public class BaseController {
    public final static String SUCCESS = "success";

    public final static String ERROR = "error";

    public final static String MSG = "msg";

    public final static String DATA = "data";

    private Logger log = Logger.getLogger(this.getClass());

    /**
     * 获取IP地址
     *
     * @param request
     * @return
     */
    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    protected String createRedirectRes(String uri) {
        return "redirect:" + uri;
    }

    protected String createForwardRes(String uri) {
        return "forward:" + uri;
    }

    protected String aesEncryptBySalt(String content, String password, String salt) {
        return AESUtils.encrypt(content + salt, password);
    }

    protected String aesDecryptBySalt(String content, String password, String salt) {
        String res = AESUtils.decrypt(content, password);
        int end = res.lastIndexOf(salt);
        if (end > 0) {
            res = res.substring(0, end);
        }

        return res;
    }

    protected String getRequestBody(HttpServletRequest request) throws IOException {
        InputStream is = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        String res = null;
        is = request.getInputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }

        res = new String(os.toByteArray(), "UTF-8");
        System.out.println("requestBody:" + res);
        if (is != null) {
            is.close();
        }
        os.close();

        return res;
    }

    protected void sendResponseBody(HttpServletResponse response, String res) {
        try {
            OutputStream os = response.getOutputStream();
            ByteArrayInputStream is = new ByteArrayInputStream(res.getBytes("UTF-8"));
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            if (is != null) {
                is.close();
            }
            os.flush();
            os.close();
        } catch (UnsupportedEncodingException e) {

        } catch (IOException e) {

        }
    }

    protected void sendResponseBody(HttpServletResponse response, byte[] res) {
        try {
            OutputStream os = response.getOutputStream();
            ByteArrayInputStream is = new ByteArrayInputStream(res);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            if (is != null) {
                is.close();
            }
            os.flush();
            os.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 错误日志记录
     *
     * @author ZhaoLiang
     * @date 2016/3/24 15:07
     */
    protected void setErrorLog(Object message, Throwable t) {
        log.error(message, t);
    }

    protected void setWarnLog(Object message) {
        log.warn(message);
    }

    protected void setInfoLog(Object message) {
        log.info(message);
    }

    /**
     * 获取web项目webapp或者webroot的根目录绝对磁盘路径
     *
     * @param request
     * @return
     */
    protected String getWebRootPath(HttpServletRequest request) {
        return request.getSession().getServletContext().getRealPath("/");
    }

    protected String getBasePath(HttpServletRequest request) {
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        return basePath;
    }
}
