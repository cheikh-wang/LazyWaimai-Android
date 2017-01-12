package com.cheikh.lazywaimai.network;

import android.text.TextUtils;
import android.util.Log;

import com.google.common.net.PercentEscaper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import com.cheikh.lazywaimai.context.AppConfig;
import com.cheikh.lazywaimai.util.Base64;
import com.cheikh.lazywaimai.util.Constants.Header;
import com.cheikh.lazywaimai.util.StringUtil;

public class RequestSignInterceptor implements Interceptor {

    private static final String LOG_TAG = RequestSignInterceptor.class.getSimpleName();
    private static final String VERSION_CODE_REGEX = "v\\d+";
    private static final PercentEscaper percentEncoder = new PercentEscaper("-._~", false);
    private static final String APP_SECRET = AppConfig.APP_SECRET;
    private static final String ENCODING = "UTF-8";
    private static final String MAC_NAME = "HmacSHA256";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        return chain.proceed(sign(request));
    }

    /**
     * 对请求进行签名
     * @param request 请求
     * @return 签名后的请求
     */
    private synchronized Request sign(Request request) {
        try {
            Map<String, String> params = new HashMap<>();
            collectQueryParameters(request, params);
            collectBodyParameters(request, params);

            // 生成源串
            String path = getRequestPath(request);
            String serialParameters = getSerialParameters(params, false);
            String source = path + '&' + serialParameters + '&' + APP_SECRET;

            // 使用HMAC-SHA1算法将源串进行加密
            byte[] binary = hmacSha1WithSecret(source, APP_SECRET);
            String encrypted = StringUtil.toHex(binary); // 加密后的数据是二进制的,需要转换

            // 将加密后的字符串进行Base64编码
            String signature = Base64.encode(encrypted.getBytes(ENCODING));

            // 将签名写入参数中
            return writeSignature(signature, request);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "请求签名失败");
        }
        return request;
    }

    /**
     * 手机get请求参数
     * @param request 请求
     * @param out 参数对
     */
    private void collectQueryParameters(Request request, Map<String, String> out) {
        String url = request.url().toString();
        int q = url.indexOf('?');
        if (q >= 0) {
            out.putAll(decodeForm(url.substring(q + 1)));
        }
    }

    /**
     * 收集post请求参数
     * @param request 请求
     * @param out 参数对
     */
    private void collectBodyParameters(Request request, Map<String, String> out) throws IOException {
        if (request.body() != null && request.body().contentType() != null) {
            String contentType = request.body().contentType().toString();
            if (contentType.equals("application/x-www-form-urlencoded")) {
                Buffer buf = new Buffer();
                request.body().writeTo(buf);
                InputStream payload = buf.inputStream();
                out.putAll(decodeForm(payload));
            }
        }
    }

    /**
     * 获取请求路径
     * @param request
     * @return
     */
    private String getRequestPath(Request request) throws URISyntaxException {
        URI uri = new URI(request.url().toString());
        String path = uri.getRawPath();
        // 只保留版本号及以后的内容为路径
        Matcher matcher = Pattern.compile(VERSION_CODE_REGEX).matcher(path);
        if (matcher.find()) {
            int index = matcher.start();
            if (index > 0) {
                path = path.substring(index);
            }
        }

        return path;
    }

    /**
     * 获取键值对形式的参数字符串
     * @param parameters
     * @param onlySerialValue
     * @return
     * @throws IOException
     */
    private String getSerialParameters(Map<String, String> parameters, boolean onlySerialValue) throws IOException {
        if (parameters == null) {
            return "";
        }
        // 将所有参数按key进行字典升序排列
        List<Map.Entry<String, String>> list = new ArrayList<>(parameters.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> lhs, Map.Entry<String, String> rhs) {
                return lhs.getKey().compareTo(rhs.getKey());
            }
        });
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append("&");
            }
            Map.Entry<String, String> entry = list.get(i);
            if (onlySerialValue) {
                sb.append(entry.getKey()).append("=").append(percentEncode(entry.getValue()));
            } else {
                // 可能value已经被urlEncode过了,所以需要先Decode
                String value = percentDecode(entry.getValue());
                sb.append(entry.getKey()).append("=").append(value);
            }
        }
        if (!onlySerialValue) {
            return percentEncode(sb.toString());
        } else {
            return sb.toString();
        }
    }

    /**
     * 将数据进行HmacSHA256加密
     * @param data
     * @param secret
     * @return
     * @throws Exception
     */
    private byte[] hmacSha1WithSecret(String data, String secret) throws Exception {
        Mac mac = Mac.getInstance(MAC_NAME);
        SecretKeySpec spec = new SecretKeySpec(secret.getBytes(ENCODING), MAC_NAME);
        mac.init(spec);
        return mac.doFinal(data.getBytes(ENCODING));
    }

    /**
     * 将签名添加到请求后返回新的请求
     * @param signature 签名
     * @param request 请求
     */
    private Request writeSignature(String signature, Request request) {
        Request.Builder builder = request.newBuilder();
        builder.addHeader(Header.HTTP_SIGNATURE, signature);
        return builder.build();
    }

    /**
     * url编码
     * @param s
     * @return
     */
    private String percentEncode(String s) {
        if (s == null) {
            return "";
        }
        return percentEncoder.escape(s);
    }

    /**
     * url解码
     * @param s
     * @return
     */
    private String percentDecode(String s) {
        try {
            if (s == null) {
                return "";
            }
            return URLDecoder.decode(s, ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 从字符串中解析出参数对
     * @param form 字符串
     * @return 解析后的参数对
     */
    private Map<String, String> decodeForm(String form) {
        Map<String, String> params = new HashMap<>();
        if (TextUtils.isEmpty(form)) {
            return params;
        }
        for (String nvp : form.split("\\&")) {
            int equals = nvp.indexOf('=');
            String name;
            String value;
            if (equals < 0) {
                name = nvp;
                value = null;
            } else {
                name = nvp.substring(0, equals);
                value = nvp.substring(equals + 1);
            }
            params.put(name, value);
        }
        return params;
    }

    /**
     * 从输入流中解析出参数对
     * @param inputStream 输入流
     * @return 解析后的参数对
     * @throws IOException
     */
    private Map<String, String> decodeForm(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
            sb.append(line);
            line = reader.readLine();
        }
        return decodeForm(sb.toString());
    }
}
